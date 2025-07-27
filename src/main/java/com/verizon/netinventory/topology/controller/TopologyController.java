package com.verizon.netinventory.topology.controller;

import com.verizon.netinventory.topology.model.Alarm;
import com.verizon.netinventory.topology.model.NetworkElement;
import com.verizon.netinventory.topology.model.Port;
import com.verizon.netinventory.topology.model.Service;
import com.verizon.netinventory.topology.model.Router; // Import Router
import com.verizon.netinventory.topology.model.Shelf; // Import Shelf
import com.verizon.netinventory.topology.model.Slot; // Import Slot
import com.verizon.netinventory.topology.model.Card; // Import Card
import com.verizon.netinventory.topology.model.Circuit; // Import Circuit
import com.verizon.netinventory.topology.model.RAN_Node; // Import RAN_Node
import com.verizon.netinventory.topology.model.Core_NF; // Import Core_NF

import com.verizon.netinventory.topology.repository.AlarmRepository;
import com.verizon.netinventory.topology.repository.NetworkElementRepository;
import com.verizon.netinventory.topology.repository.PortRepository;
import com.verizon.netinventory.topology.repository.ServiceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topology")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Network Topology & Analysis", description = "APIs for querying network topology, performing impact analysis, and root cause analysis.")
public class TopologyController {

    private final NetworkElementRepository networkElementRepository;
    private final ServiceRepository serviceRepository;
    private final PortRepository portRepository;
    private final AlarmRepository alarmRepository;
    private final Neo4jClient neo4jClient;

    @Operation(summary = "Get all network elements", description = "Retrieves a list of all generic network elements (NEs) in the topology.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of network elements")
    @GetMapping("/elements")
    public ResponseEntity<List<NetworkElement>> getAllNetworkElements() {
        List<NetworkElement> elements = networkElementRepository.findAll();
        return ResponseEntity.ok(elements);
    }

    @Operation(summary = "Get a network element by its element ID", description = "Retrieves a specific network element by its unique elementId (e.g., ROUTER_HQ_001).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved network element"),
            @ApiResponse(responseCode = "404", description = "Network element not found")
    })
    @GetMapping("/elements/{elementId}")
    public ResponseEntity<NetworkElement> getNetworkElementById(@Parameter(description = "ID of the network element to retrieve") @PathVariable String elementId) {
        return networkElementRepository.findByElementId(elementId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Perform Impact Analysis", description = "Given a failing network element (by ID), find all directly or indirectly affected services. Simulates a fault scenario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully identified affected services",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Service.class))),
            @ApiResponse(responseCode = "404", description = "Source network element not found")
    })
    @GetMapping("/analysis/impact/{elementId}")
    public ResponseEntity<Set<Service>> getImpactedServices(@Parameter(description = "ID of the failing network element (e.g., ROUTER_HQ_001, CARD_ETH_001, PORT_RA_GE0/0/1)") @PathVariable String elementId) {
        Optional<NetworkElement> optionalElement = networkElementRepository.findByElementId(elementId);
        if (optionalElement.isEmpty()) {
            Optional<Port> optionalPort = portRepository.findByPortId(elementId); // Check if it's a Port ID
            if (optionalPort.isEmpty()) {
                log.warn("Element with ID {} not found for impact analysis.", elementId);
                return ResponseEntity.notFound().build();
            }
        }

        String cypherQuery = """
                MATCH (startNode)
                WHERE startNode.elementId = $elementId OR startNode.portId = $elementId
                OPTIONAL MATCH path = (startNode)-[r*]->(downstreamNode)
                OPTIONAL MATCH (affectedService:Service)-[:DEPENDS_ON]->(downstreamNode)
                RETURN affectedService.serviceId AS serviceId, affectedService.name AS serviceName, affectedService.type AS serviceType, affectedService.status AS serviceStatus
                UNION
                MATCH (startNode)
                WHERE startNode.elementId = $elementId OR startNode.portId = $elementId
                OPTIONAL MATCH (directAffectedService:Service)-[:DEPENDS_ON]->(startNode)
                RETURN directAffectedService.serviceId AS serviceId, directAffectedService.name AS serviceName, directAffectedService.type AS serviceType, directAffectedService.status AS serviceStatus
                """;

        Collection<Map<String, Object>> results = neo4jClient.query(cypherQuery)
                .bind(elementId).to("elementId")
                .fetch().all();

        Set<Service> impactedServices = results.stream()
                .filter(map -> map.get("serviceId") != null)
                .map(map -> Service.builder()
                        .serviceId((String) map.get("serviceId"))
                        .name((String) map.get("serviceName"))
                        .type((String) map.get("serviceType"))
                        .status((String) map.get("serviceStatus"))
                        .build())
                .collect(Collectors.toSet());

        if (impactedServices.isEmpty()) {
            log.info("No direct or indirect services found to be impacted by element ID: {}", elementId);
            return ResponseEntity.ok(Collections.emptySet());
        }
        return ResponseEntity.ok(impactedServices);
    }

    @Operation(summary = "Perform Root Cause Analysis", description = "Given a degraded/down service (by ID), trace back through its dependencies to find potential root cause network elements. Returns a simplified NetworkElement representation of potential root causes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully identified potential root cause elements",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NetworkElement.class))),
            @ApiResponse(responseCode = "404", description = "Source service not found")
    })
    @GetMapping("/analysis/rootcause/{serviceId}")
    public ResponseEntity<Set<NetworkElement>> getRootCauseElements(@Parameter(description = "ID of the impacted service (e.g., VOIP_SERVICE_US)") @PathVariable String serviceId) {
        Optional<Service> optionalService = serviceRepository.findByServiceId(serviceId);
        if (optionalService.isEmpty()) {
            log.warn("Service with ID {} not found for root cause analysis.", serviceId);
            return ResponseEntity.notFound().build();
        }

        String cypherQuery = """
                MATCH (impactedService:Service {serviceId: $serviceId})
                MATCH path = (impactedService)-[:DEPENDS_ON*]->(rootCauseNode)
                WHERE rootCauseNode:NE OR rootCauseNode:Port
                RETURN rootCauseNode.elementId AS elementId, rootCauseNode.name AS name, labels(rootCauseNode) AS types, rootCauseNode.status AS status, rootCauseNode.portId AS portId
                """;

        Collection<Map<String, Object>> results = neo4jClient.query(cypherQuery)
                .bind(serviceId).to("serviceId")
                .fetch().all();

        Set<NetworkElement> rootCauseElements = results.stream()
                .map(map -> {
                    List<String> labels = (List<String>) map.get("types");
                    String primaryType = (labels != null && !labels.isEmpty()) ? labels.get(0) : "Unknown"; // Take first label as primary type
                    String elementId = (String) map.get("elementId");
                    String name = (String) map.get("name");
                    String status = (String) map.get("status");
                    String portId = (String) map.get("portId"); // Will be null for non-Port NEs

                    // CRUCIAL FIX HERE: Instantiate concrete NetworkElement subclasses based on type
                    switch (primaryType) {
                        case "Router":
                            // You might populate more Router-specific fields if needed
                            return Router.builder().elementId(elementId).name(name).type(primaryType).status(status).build();
                        case "Shelf":
                            return Shelf.builder().elementId(elementId).name(name).type(primaryType).status(status).build();
                        case "Slot":
                            return Slot.builder().elementId(elementId).name(name).type(primaryType).status(status).build();
                        case "Card":
                            return Card.builder().elementId(elementId).name(name).type(primaryType).status(status).build();
                        case "Circuit":
                            return Circuit.builder().elementId(elementId).name(name).type(primaryType).status(status).build();
                        case "RAN_Node":
                            return RAN_Node.builder().elementId(elementId).name(name).type(primaryType).status(status).build();
                        case "Core_NF":
                            return Core_NF.builder().elementId(elementId).name(name).type(primaryType).status(status).build();
                        case "Port":
                            // Port is a separate node, but we need to return a NetworkElement type.
                            // Create a dummy NetworkElement or a generic NetworkElement wrapper for it.
                            // For this API's return type, we are mapping Port's data to a NetworkElement structure.
                            return Router.builder() // Using Router as a concrete placeholder for Port in NE set
                                    .elementId(portId) // Use portId as its identifying elementId
                                    .name(name)
                                    .type(primaryType) // Keep type as "Port"
                                    .status(status)
                                    .build();
                        default:
                            // Fallback for any unknown type or for generic NE if not explicitly mapped above.
                            // Since NetworkElement is abstract, we cannot build it directly.
                            // Use Router as a generic concrete NetworkElement placeholder for all other cases.
                            return Router.builder()
                                    .elementId(elementId != null ? elementId : portId)
                                    .name(name)
                                    .type(primaryType)
                                    .status(status)
                                    .build();
                    }
                })
                .collect(Collectors.toSet());

        if (rootCauseElements.isEmpty()) {
            log.info("No direct or indirect root cause elements found for service ID: {}", serviceId);
            return ResponseEntity.ok(Collections.emptySet());
        }
        return ResponseEntity.ok(rootCauseElements);
    }
}
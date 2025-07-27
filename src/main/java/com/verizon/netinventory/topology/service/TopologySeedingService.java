package com.verizon.netinventory.topology.service;

import com.verizon.netinventory.topology.model.*;
import com.verizon.netinventory.topology.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopologySeedingService {

    private final RouterRepository routerRepository;
    private final PortRepository portRepository;
    private final ServiceRepository serviceRepository;
    private final ShelfRepository shelfRepository;
    private final SlotRepository slotRepository;
    private final CardRepository cardRepository;
    private final CircuitRepository circuitRepository;
    private final RANNodeRepository ranNodeRepository;
    private final CoreNFRepository coreNFRepository;
    private final AlarmRepository alarmRepository;

    @PostConstruct
    @Transactional
    public void seedTopologyData() {
        log.info("Starting Neo4j topology data seeding...");

        clearDatabase();

        // --- 1. Create Network Elements (NEs) ---

        // Wireline NEs
        Router routerA = Router.builder()
                .elementId("ROUTER_HQ_001")
                .name("HQ Core Router A")
                .status("Operational")
                .ipAddress("10.0.0.1")
                .model("Cisco ASR9K")
                .vendor("Cisco")
                .build();
        routerA = routerRepository.save(routerA);
        log.info("Created Router: {}", routerA.getName());

        Router routerB = Router.builder()
                .elementId("ROUTER_DR_002")
                .name("DR Core Router B")
                .status("Operational")
                .ipAddress("10.0.0.2")
                .model("Juniper MX2020")
                .vendor("Juniper")
                .build();
        routerB = routerRepository.save(routerB);
        log.info("Created Router: {}", routerB.getName());

        Shelf shelf1 = Shelf.builder()
                .elementId("ROUTER_HQ_SHELF_01")
                .name("Router A Shelf 1")
                .status("Operational")
                .shelfId("SHELF-01-RA")
                .numberOfSlots(10)
                .build();
        shelf1 = shelfRepository.save(shelf1);
        log.info("Created Shelf: {}", shelf1.getName());

        Slot slotA = Slot.builder()
                .elementId("SHELF_01_SLOT_05")
                .name("Shelf 1 Slot 5")
                .status("Operational")
                .slotId("SLOT-05-S1")
                .slotNumber(5)
                .build();
        slotA = slotRepository.save(slotA);
        log.info("Created Slot: {}", slotA.getName());

        Card cardEth = Card.builder()
                .elementId("CARD_ETH_001")
                .name("Ethernet Line Card")
                .status("Operational")
                .cardId("CARD-ETH-001")
                .cardType("Ethernet")
                .serialNumber("SN-ETHA-1234")
                .build();
        cardEth = cardRepository.save(cardEth);
        log.info("Created Card: {}", cardEth.getName());

        // Port creation now uses portId directly as it's not an inherited elementId
        Port port1 = Port.builder()
                .portId("PORT_RA_GE0/0/1") // Use portId for Port
                .name("GigabitEthernet0/0/1")
                .interfaceType("Fiber")
                .speedGbps(10.0)
                .status("Up")
                .build();
        port1 = portRepository.save(port1);
        log.info("Created Port: {}", port1.getName());

        Port port2 = Port.builder()
                .portId("PORT_RB_GE0/0/2") // Use portId for Port
                .name("GigabitEthernet0/0/2")
                .interfaceType("Fiber")
                .speedGbps(10.0)
                .status("Up")
                .build();
        port2 = portRepository.save(port2);
        log.info("Created Port: {}", port2.getName());

        Circuit circuit = Circuit.builder()
                .elementId("CIRCUIT_HQ_DR_001")
                .name("HQ-DR Interconnect Circuit")
                .status("Operational")
                .circuitId("CIRC-HQ-DR-001")
                .circuitType("FiberLink")
                .capacityMbps(10000.0) // 10 Gbps
                .build();
        circuit = circuitRepository.save(circuit);
        log.info("Created Circuit: {}", circuit.getName());

        // Wireless NEs
        RAN_Node gNodeB_SF = RAN_Node.builder()
                .elementId("GNB_SF_001")
                .name("San Francisco gNodeB")
                .status("Operational")
                .ipAddress("172.16.1.10")
                .ranNodeId("GNB-SF-001")
                .cellId("SF-CELL-001")
                .band("n78")
                .technology("5G")
                .build();
        gNodeB_SF = ranNodeRepository.save(gNodeB_SF);
        log.info("Created gNodeB: {}", gNodeB_SF.getName());

        Core_NF amf_east = Core_NF.builder()
                .elementId("AMF_EAST_01")
                .name("AMF East Region 1")
                .status("Operational")
                .ipAddress("10.100.1.5")
                .nfId("AMF-EAST-01")
                .functionType("AMF")
                .version("5.1.0")
                .build();
        amf_east = coreNFRepository.save(amf_east);
        log.info("Created AMF: {}", amf_east.getName());

        // --- 2. Create Services ---

        com.verizon.netinventory.topology.model.Service voipService = com.verizon.netinventory.topology.model.Service.builder()
                .serviceId("VOIP_SERVICE_US")
                .name("National VoIP Service")
                .type("VoIP_Service")
                .description("Core Voice over IP service for all users.")
                .status("Active")
                .build();
        voipService = serviceRepository.save(voipService);
        log.info("Created Service: {}", voipService.getName());

        com.verizon.netinventory.topology.model.Service mobileDataService = com.verizon.netinventory.topology.model.Service.builder()
                .serviceId("MOBILE_DATA_SERVICE_SF")
                .name("SF 5G Mobile Data Service")
                .type("Mobile_Data_Service")
                .description("High-speed 5G mobile data for San Francisco users.")
                .status("Active")
                .build();
        mobileDataService = serviceRepository.save(mobileDataService);
        log.info("Created Service: {}", mobileDataService.getName());

        // --- 3. Establish Relationships ---

        // Wireline Hierarchy: Router -> Shelf -> Slot -> Card -> Port
        routerA.getContainsShelves().add(shelf1);
        routerRepository.save(routerA);

        shelf1.getContainsSlots().add(slotA);
        shelfRepository.save(shelf1);

        slotA.getHasCards().add(cardEth);
        slotRepository.save(slotA);

        cardEth.getHasPorts().add(port1);
        cardRepository.save(cardEth);

        // Port to Port connection via Circuit
        port1.addConnection(port2, "Circuit", circuit.getCapacityMbps(), "Up");
        portRepository.save(port1);

        // Service Dependencies - NOW USING DIRECT RELATIONSHIPS
        // VoIP Service depends on Router A
        voipService.getDependsOnNetworkElements().add(routerA); // Direct add
        serviceRepository.save(voipService);

        // Mobile Data Service depends on gNodeB and AMF
        mobileDataService.getDependsOnNetworkElements().add(gNodeB_SF); // Direct add
        mobileDataService.getDependsOnNetworkElements().add(amf_east);   // Direct add
        serviceRepository.save(mobileDataService);

        // Example of an Alarm
        Alarm routerA_alarm = Alarm.builder()
                .alarmId("ALARM_RA_PORT_001_DOWN")
                .message("Port GE0/0/1 on Router HQ_001 is down.")
                .severity("Critical")
                .timestamp(LocalDateTime.now())
                .impactedEntityId(port1.getPortId()) // Link to Port using its portId
                .status("Active")
                .build();
        alarmRepository.save(routerA_alarm);
        log.info("Created Alarm: {}", routerA_alarm.getMessage());

        log.info("Neo4j topology data seeding complete.");
    }

    private void clearDatabase() {
        log.info("Clearing existing Neo4j data...");
        routerRepository.deleteAll();
        portRepository.deleteAll();
        serviceRepository.deleteAll();
        shelfRepository.deleteAll();
        slotRepository.deleteAll();
        cardRepository.deleteAll();
        circuitRepository.deleteAll();
        ranNodeRepository.deleteAll();
        coreNFRepository.deleteAll();
        alarmRepository.deleteAll();
        log.info("Neo4j data cleared.");
    }
}
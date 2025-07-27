package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

import java.util.HashSet;
import java.util.Set;

@Node("Service")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {
    @Id @GeneratedValue
    private Long id;

    @Property("serviceId")
    private String serviceId;

    private String name;
    private String type;
    private String description;
    private String status;

    @Default
    @Relationship(type = "DEPENDS_ON", direction = Relationship.Direction.OUTGOING)
    private Set<NetworkElement> dependsOnNetworkElements = new HashSet<>(); // Service depends on NetworkElement

    @Default
    @Relationship(type = "DEPENDS_ON_SERVICE", direction = Relationship.Direction.OUTGOING) // Service depends on another Service
    private Set<Service> dependsOnServices = new HashSet<>();
}
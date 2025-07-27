package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;

import java.util.HashSet;
import java.util.Set;

@Node("Port") // This is a standalone Node
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Port {
    @Id @GeneratedValue
    private Long id;

    @Property("portId") // Explicitly map to 'portId' property in Neo4j
    private String portId;

    private String name;
    private String interfaceType;
    private double speedGbps;
    private String status;

    @Default
    @Relationship(type = "CONNECTS_TO", direction = Relationship.Direction.OUTGOING)
    private Set<ConnectsToRelationship> outgoingConnections = new HashSet<>();

    public Port(String portId, String name, String interfaceType, double speedGbps, String status) {
        this.portId = portId;
        this.name = name;
        this.interfaceType = interfaceType;
        this.speedGbps = speedGbps;
        this.status = status;
    }

    public void addConnection(Port targetPort, String linkType, double bandwidthMbps, String status) {
        this.outgoingConnections.add(ConnectsToRelationship.builder()
                .targetPort(targetPort)
                .linkType(linkType)
                .bandwidthMbps(bandwidthMbps)
                .status(status)
                .build());
    }
}
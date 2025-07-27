package com.verizon.netinventory.topology.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Node("RAN_Node") // Will also have :NE label
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RAN_Node extends NetworkElement {
    private String ranNodeId; // e.g., "GNB_SF_001", "ENB_DALLAS_002"
    private String cellId;
    private String band;
    private String technology; // e.g., "4G", "5G"

    @Relationship(type = "HAS_SECTOR", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<NetworkElement> hasSectors = new HashSet<>(); // Initialized

    @Relationship(type = "USES_BACKHAUL", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<NetworkElement> usesBackhauls = new HashSet<>(); // Initialized

    public RAN_Node(String elementId, String name, String status, String ipAddress, String ranNodeId, String cellId, String band, String technology) {
        super(elementId, name, "RAN_Node", status, ipAddress);
        this.ranNodeId = ranNodeId;
        this.cellId = cellId;
        this.band = band;
        this.technology = technology;
    }
}
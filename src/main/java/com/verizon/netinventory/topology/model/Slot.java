package com.verizon.netinventory.topology.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Node("Slot") // Will also have :NE label
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Slot extends NetworkElement {
    private String slotId; // e.g., "SHELF_A_SLOT_1"
    private int slotNumber;

    @Relationship(type = "HAS_CARD", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<NetworkElement> hasCards = new HashSet<>(); // Initialized

    public Slot(String elementId, String name, String status, String ipAddress, String slotId, int slotNumber) {
        super(elementId, name, "Slot", status, ipAddress);
        this.slotId = slotId;
        this.slotNumber = slotNumber;
    }
}
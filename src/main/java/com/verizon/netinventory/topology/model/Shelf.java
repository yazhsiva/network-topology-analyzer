package com.verizon.netinventory.topology.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Node("Shelf") // Will also have :NE label due to inheritance
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Shelf extends NetworkElement {
    private String shelfId; // e.g., "NE_001_SHELF_A"
    private int numberOfSlots;

    @Relationship(type = "CONTAINS_SLOT", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<NetworkElement> containsSlots = new HashSet<>(); // Initialized

    public Shelf(String elementId, String name, String status, String ipAddress, String shelfId, int numberOfSlots) {
        super(elementId, name, "Shelf", status, ipAddress);
        this.shelfId = shelfId;
        this.numberOfSlots = numberOfSlots;
    }
}
package com.verizon.netinventory.topology.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Node("Card") // Will also have :NE label
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Card extends NetworkElement {
    private String cardId; // e.g., "CARD_ETHA_001"
    private String cardType; // e.g., "Ethernet", "Optical"
    private String serialNumber;

    @Relationship(type = "HAS_PORT", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Port> hasPorts = new HashSet<>(); // Initialized

    public Card(String elementId, String name, String status, String ipAddress, String cardId, String cardType, String serialNumber) {
        super(elementId, name, "Card", status, ipAddress);
        this.cardId = cardId;
        this.cardType = cardType;
        this.serialNumber = serialNumber;
    }
}
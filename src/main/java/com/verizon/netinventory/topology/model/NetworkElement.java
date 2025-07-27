package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder.Default; // Make sure this is imported

import java.util.HashSet;
import java.util.Set;

@Node("NE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class NetworkElement {

    @Id @GeneratedValue
    private Long id;

    @Property("elementId")
    private String elementId;

    private String name;
    private String type;
    private String status;
    private String ipAddress;

    @Default
    @Relationship(type = "CONNECTS_TO", direction = Relationship.Direction.OUTGOING)
    private Set<ConnectsToRelationship> connectsTo = new HashSet<>();

    // IMPORTANT: THE 'dependsOn' FIELD FOR DependsOnRelationship IS REMOVED FROM HERE
    // Service dependencies are now handled directly on the Service model class.

    @Default
    @Relationship(type = "CONTAINS_SHELF", direction = Relationship.Direction.OUTGOING)
    private Set<NetworkElement> containsShelves = new HashSet<>();

    @Default
    @Relationship(type = "HAS_PORT", direction = Relationship.Direction.OUTGOING) // This relationship is for Card -> Port
    private Set<Port> hasPorts = new HashSet<>();

    public NetworkElement(String elementId, String name, String type, String status, String ipAddress) {
        this.elementId = elementId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.ipAddress = ipAddress;
    }
}
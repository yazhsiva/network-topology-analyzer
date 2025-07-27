package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectsToRelationship {

    @RelationshipId
    private Long id;

    private String linkType;
    private double bandwidthMbps;
    private String status;

    @TargetNode
    private Port targetPort; // Correct, targets a Port node
}
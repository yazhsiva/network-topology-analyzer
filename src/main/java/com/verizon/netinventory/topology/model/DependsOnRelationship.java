//package com.verizon.netinventory.topology.model;
//
//import org.springframework.data.neo4j.core.schema.RelationshipId;
//import org.springframework.data.neo4j.core.schema.RelationshipProperties;
//import org.springframework.data.neo4j.core.schema.TargetNode;
//import lombok.AllArgsConstructor; // Added for @Builder
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@RelationshipProperties
//@Data
//@NoArgsConstructor
//@AllArgsConstructor // Needed for @Builder
//@Builder
//public class DependsOnRelationship {
//
//    @RelationshipId
//    private Long id;
//
//    private String dependencyType; // e.g., "network", "compute", "software"
//    private String criticality; // e.g., "High", "Medium", "Low"
//
//    @TargetNode
//    private Object targetNode; // Can be NetworkElement or Service, so use Object for now
//
//    // Removed the manual constructor if @AllArgsConstructor handles it.
//}
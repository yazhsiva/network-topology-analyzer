package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.NetworkElement;
import com.verizon.netinventory.topology.model.Service;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.Set;

@Repository
public interface NetworkElementRepository extends Neo4jRepository<NetworkElement, Long> {

    Optional<NetworkElement> findByElementId(String elementId);

    // Custom query to find network elements by name (example)
    List<NetworkElement> findByNameContainingIgnoreCase(String name);

    // Example custom query for finding affected services, if you want to use the repository directly
    // This query is a simpler version; the Neo4jClient version in the controller is more comprehensive.
    @Query("MATCH (n:NE {elementId: $elementId})-[*1..]->(downstream) MATCH (s:Service)-[:DEPENDS_ON]->(downstream) RETURN s")
    Set<Service> findAffectedServicesByElementId(String elementId);
}
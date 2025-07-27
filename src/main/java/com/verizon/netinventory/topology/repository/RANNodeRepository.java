package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.RAN_Node;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RANNodeRepository extends Neo4jRepository<RAN_Node, Long> {
    Optional<RAN_Node> findByRanNodeId(String ranNodeId);
}
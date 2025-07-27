package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.Router;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouterRepository extends Neo4jRepository<Router, Long> {
    Optional<Router> findByElementId(String elementId);
}
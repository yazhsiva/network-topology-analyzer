package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.Circuit;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CircuitRepository extends Neo4jRepository<Circuit, Long> {
    Optional<Circuit> findByCircuitId(String circuitId);
}
package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.Port;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortRepository extends Neo4jRepository<Port, Long> {
    Optional<Port> findByPortId(String portId); // This method is correct for Port's portId property
}
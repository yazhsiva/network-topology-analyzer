package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.Service;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends Neo4jRepository<Service, Long> {
    Optional<Service> findByServiceId(String serviceId);
}
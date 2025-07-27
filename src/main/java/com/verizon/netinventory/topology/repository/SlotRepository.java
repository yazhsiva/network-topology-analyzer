package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.Slot;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlotRepository extends Neo4jRepository<Slot, Long> {
    Optional<Slot> findBySlotId(String slotId);
}
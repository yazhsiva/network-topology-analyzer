package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.Shelf;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelfRepository extends Neo4jRepository<Shelf, Long> {
    Optional<Shelf> findByShelfId(String shelfId);
}
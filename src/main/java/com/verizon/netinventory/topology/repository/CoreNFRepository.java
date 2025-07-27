package com.verizon.netinventory.topology.repository;

import com.verizon.netinventory.topology.model.Core_NF;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoreNFRepository extends Neo4jRepository<Core_NF, Long> {
    Optional<Core_NF> findByNfId(String nfId);
}
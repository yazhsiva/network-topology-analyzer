package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Node("Circuit") // Circuits are also network elements in a broad sense, facilitating connections
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Circuit extends NetworkElement {
    private String circuitId; // e.g., "CIRCUIT_LAX_NYC_001"
    private String circuitType; // e.g., "E1", "T1", "FiberLink"
    private double capacityMbps;

    public Circuit(String elementId, String name, String status, String ipAddress, String circuitId, String circuitType, double capacityMbps) {
        super(elementId, name, "Circuit", status, ipAddress);
        this.circuitId = circuitId;
        this.circuitType = circuitType;
        this.capacityMbps = capacityMbps;
    }
}
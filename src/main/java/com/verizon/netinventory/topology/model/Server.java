package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor; // Added for @SuperBuilder

@Node("Server")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor // Needed for @SuperBuilder if custom constructor exists
@SuperBuilder
public class Server extends NetworkElement {
    private String os;
    private int cpuCores;
    private int ramGB;

    public Server(String elementId, String name, String status, String ipAddress, String os, int cpuCores, int ramGB) {
        super(elementId, name, "Server", status, ipAddress);
        this.os = os;
        this.cpuCores = cpuCores;
        this.ramGB = ramGB;
    }
}
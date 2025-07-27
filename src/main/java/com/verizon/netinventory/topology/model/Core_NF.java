package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Node("Core_NF") // Will also have :NE label
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Core_NF extends NetworkElement {
    private String nfId; // e.g., "AMF_EAST_01"
    private String functionType; // e.g., "AMF", "SMF", "UPF"
    private String version;

    public Core_NF(String elementId, String name, String status, String ipAddress, String nfId, String functionType, String version) {
        super(elementId, name, "Core_NF", status, ipAddress);
        this.nfId = nfId;
        this.functionType = functionType;
        this.version = version;
    }
}
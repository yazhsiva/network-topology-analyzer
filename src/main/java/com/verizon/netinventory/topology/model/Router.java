package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor; // Added for @SuperBuilder

@Node("Router") // This node will have both :NE and :Router labels
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor // Needed for @SuperBuilder if custom constructor exists
@SuperBuilder
public class Router extends NetworkElement {
    private String model;
    private String vendor;

    public Router(String elementId, String name, String status, String ipAddress, String model, String vendor) {
        super(elementId, name, "Router", status, ipAddress);
        this.model = model;
        this.vendor = vendor;
    }
}
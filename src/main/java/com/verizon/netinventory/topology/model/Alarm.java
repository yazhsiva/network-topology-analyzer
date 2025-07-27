package com.verizon.netinventory.topology.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Node("Alarm")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alarm {
    @Id @GeneratedValue
    private Long id;

    @Property("alarmId")
    private String alarmId; // e.g., "ALARM_PORT_DOWN_ROUTER_001"

    private String message;
    private String severity; // e.g., "Critical", "Major", "Minor", "Warning"
    private LocalDateTime timestamp;
    private String impactedEntityId; // The ID of the NE/Port that directly raised the alarm
    private String status; // e.g., "Active", "Cleared", "Acknowledged"

    // Removed manual constructor, relying on @AllArgsConstructor for @Builder compatibility.
}
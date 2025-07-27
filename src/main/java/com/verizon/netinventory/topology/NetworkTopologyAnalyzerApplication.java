package com.verizon.netinventory.topology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories; // Add this import

@SpringBootApplication
@EnableNeo4jRepositories(basePackages = "com.verizon.netinventory.topology.repository") // Add this annotation
public class NetworkTopologyAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetworkTopologyAnalyzerApplication.class, args);
	}
}
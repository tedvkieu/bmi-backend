package com.example.inspection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.example.inspection.config.CorsProperties;

@SpringBootApplication
@EntityScan(basePackages = "com.example.inspection.entity")
@EnableConfigurationProperties(CorsProperties.class)
public class InspectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(InspectionApplication.class, args);

		System.out.println("Inspection application started successfully.");
	}

}

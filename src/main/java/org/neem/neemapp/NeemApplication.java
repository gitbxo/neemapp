package org.neem.neemapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
 * To run this application:
 * 
 * mvn clean spring-boot:run
 * 
 */

@SpringBootApplication
@EntityScan(basePackages = { "org.neem.neemapp.model" })
@EnableJpaRepositories("org.neem.neemapp.jpa")
public class NeemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeemApplication.class, args);
	}

}

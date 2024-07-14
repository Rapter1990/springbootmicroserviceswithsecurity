package com.springbootmicroservices.commonservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The entry point for the Commonservice Spring Boot application.
 * This application is a Eureka client that registers itself with a Eureka server.
 * The application is configured with the {@link SpringBootApplication} annotation
 * and enabled as a Eureka client with the {@link EnableDiscoveryClient} annotation.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CommonserviceApplication {

	/**
	 * Main method to run the Spring Boot application.
	 *
	 * @param args Command-line arguments passed during the application startup.
	 */
	public static void main(String[] args) {
		SpringApplication.run(CommonserviceApplication.class, args);
	}

}

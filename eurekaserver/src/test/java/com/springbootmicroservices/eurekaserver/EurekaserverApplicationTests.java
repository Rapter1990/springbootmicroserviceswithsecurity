package com.springbootmicroservices.eurekaserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the {@link EurekaserverApplication}.
 * This class contains unit tests to ensure that the application context loads correctly
 * and that the Spring Boot application starts up without any issues.
 * The {@link SpringBootTest} annotation indicates that this is a Spring Boot test
 * that will start the full application context.
 */
@SpringBootTest
class EurekaserverApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Test to verify that the application context loads correctly.
	 * This test method asserts that the {@link #applicationContext} is not null,
	 * indicating that the Spring Boot application context has been successfully loaded.
	 */
	@Test
	void contextLoads() {
		assertThat(applicationContext).isNotNull();
	}

}

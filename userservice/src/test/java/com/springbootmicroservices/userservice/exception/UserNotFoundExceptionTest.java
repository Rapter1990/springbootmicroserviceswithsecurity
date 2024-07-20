package com.springbootmicroservices.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {

        // Arrange
        String errorMessage = """
            User not found!
            """;

        // Act
        UserNotFoundException exception = new UserNotFoundException();

        // Assert
        assertEquals(errorMessage, exception.getMessage());

    }

    @Test
    void testParameterizedConstructor() {

        // Arrange
        String customMessage = "Custom message";
        String expectedMessage = """
            User not found!
             Custom message""";

        // Act
        UserNotFoundException exception = new UserNotFoundException(customMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());

    }

}
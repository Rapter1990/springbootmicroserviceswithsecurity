package com.springbootmicroservices.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyExistExceptionTest {

    @Test
    void testDefaultConstructor() {

        // Arrange
        String errorMessage = """
            User already exist!
            """;

        // Act
        UserAlreadyExistException exception = new UserAlreadyExistException();

        // Assert
        assertEquals(errorMessage, exception.getMessage());

    }

    @Test
    void testParameterizedConstructor() {

        // Arrange
        String customMessage = "Custom message";
        String expectedMessage = """
            User already exist!
             Custom message""";

        // Act
        UserAlreadyExistException exception = new UserAlreadyExistException(customMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());

    }

}
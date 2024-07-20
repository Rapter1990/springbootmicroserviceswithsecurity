package com.springbootmicroservices.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordNotValidExceptionTest {

    @Test
    void testDefaultConstructor() {

        // Arrange
        String errorMessage = """
            Password is not valid!
            """;

        // Act
        PasswordNotValidException exception = new PasswordNotValidException();

        // Assert
        assertEquals(errorMessage, exception.getMessage());

    }

    @Test
    void testParameterizedConstructor() {

        // Arrange
        String customMessage = "Custom message";

        // Act
        PasswordNotValidException exception = new PasswordNotValidException(customMessage);

        // Assert
        assertEquals("Password is not valid!\n Custom message", exception.getMessage());

    }

}
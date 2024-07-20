package com.springbootmicroservices.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserStatusNotValidExceptionTest {

    @Test
    void testDefaultConstructor() {

        // Arrange
        String errorMessage = """
            User status is not valid!
            """;

        // Act
        UserStatusNotValidException exception = new UserStatusNotValidException();

        // Assert
        assertEquals(errorMessage, exception.getMessage());

    }

    @Test
    void testParameterizedConstructor() {

        // Arrange
        String customMessage = "Custom message";
        String expectedMessage = """
            User status is not valid!
             Custom message""";

        // Act
        UserStatusNotValidException exception = new UserStatusNotValidException(customMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());

    }

}
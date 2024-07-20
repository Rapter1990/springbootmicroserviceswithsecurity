package com.springbootmicroservices.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenAlreadyInvalidatedExceptionTest {

    @Test
    void testDefaultConstructor() {

        // Arrange
        String errorMessage = """
            Token is already invalidated!
            """;

        // Act
        TokenAlreadyInvalidatedException exception = new TokenAlreadyInvalidatedException();

        // Assert
        assertEquals(errorMessage, exception.getMessage());

    }

    @Test
    void testParameterizedConstructor() {

        // Arrange
        String tokenId = "12345";
        String expectedMessage = """
            Token is already invalidated!
             TokenID = 12345""";

        // Act
        TokenAlreadyInvalidatedException exception = new TokenAlreadyInvalidatedException(tokenId);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());

    }

}
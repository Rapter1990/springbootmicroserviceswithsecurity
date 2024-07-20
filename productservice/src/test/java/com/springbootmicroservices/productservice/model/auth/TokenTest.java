package com.springbootmicroservices.productservice.model.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void testTokenBuilder_WithAllFields() {

        // Given
        String accessToken = "sampleAccessToken";
        Long accessTokenExpiresAt = System.currentTimeMillis() + 3600;
        String refreshToken = "sampleRefreshToken";

        // When
        Token token = Token.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshToken(refreshToken)
                .build();

        // Then
        assertNotNull(token);
        assertEquals(accessToken, token.getAccessToken());
        assertEquals(accessTokenExpiresAt, token.getAccessTokenExpiresAt());
        assertEquals(refreshToken, token.getRefreshToken());

    }

    @Test
    void testTokenBuilder_DefaultValues() {

        // When
        Token token = Token.builder().build();

        // Then
        assertNotNull(token);
        assertNull(token.getAccessToken());
        assertNull(token.getAccessTokenExpiresAt());
        assertNull(token.getRefreshToken());

    }

    @Test
    void testIsBearerToken_WithValidBearerToken() {

        // Given
        String authorizationHeader = "Bearer sampleAccessToken";

        // When
        boolean result = Token.isBearerToken(authorizationHeader);

        // Then
        assertTrue(result);

    }

    @Test
    void testIsBearerToken_WithInvalidBearerToken() {

        // Given
        String authorizationHeader = "sampleAccessToken";

        // When
        boolean result = Token.isBearerToken(authorizationHeader);

        // Then
        assertFalse(result);

    }

    @Test
    void testIsBearerToken_WithEmptyHeader() {

        // Given
        String authorizationHeader = "";

        // When
        boolean result = Token.isBearerToken(authorizationHeader);

        // Then
        assertFalse(result);

    }

    @Test
    void testGetJwt_WithBearerToken() {

        // Given
        String authorizationHeader = "Bearer sampleAccessToken";

        // When
        String jwt = Token.getJwt(authorizationHeader);

        // Then
        assertEquals("sampleAccessToken", jwt);

    }

    @Test
    void testGetJwt_WithInvalidTokenFormat() {

        // Given
        String authorizationHeader = "sampleAccessToken";

        // When
        String jwt = Token.getJwt(authorizationHeader);

        // Then
        assertEquals("sampleAccessToken", jwt);

    }

    @Test
    void testGetJwt_WithEmptyHeader() {

        // Given
        String authorizationHeader = "";

        // When
        String jwt = Token.getJwt(authorizationHeader);

        // Then
        assertEquals("", jwt);

    }

}
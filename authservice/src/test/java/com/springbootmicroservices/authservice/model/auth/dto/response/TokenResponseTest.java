package com.springbootmicroservices.authservice.model.auth.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenResponseTest {

    @Test
    void testTokenResponseBuilder_WithAllFields() {

        // Given
        String accessToken = "sampleAccessToken";
        Long accessTokenExpiresAt = System.currentTimeMillis() + 3600;
        String refreshToken = "sampleRefreshToken";

        // When
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshToken(refreshToken)
                .build();

        // Then
        assertNotNull(tokenResponse);
        assertEquals(accessToken, tokenResponse.getAccessToken());
        assertEquals(accessTokenExpiresAt, tokenResponse.getAccessTokenExpiresAt());
        assertEquals(refreshToken, tokenResponse.getRefreshToken());

    }

    @Test
    void testTokenResponseBuilder_DefaultValues() {

        // When
        TokenResponse tokenResponse = TokenResponse.builder().build();

        // Then
        assertNotNull(tokenResponse);
        assertNull(tokenResponse.getAccessToken());
        assertNull(tokenResponse.getAccessTokenExpiresAt());
        assertNull(tokenResponse.getRefreshToken());

    }

    @Test
    void testTokenResponseSettersAndGetters() {

        // Given
        TokenResponse tokenResponse = new TokenResponse();

        // When
        tokenResponse.setAccessToken("newAccessToken");
        tokenResponse.setAccessTokenExpiresAt(System.currentTimeMillis() + 3600);
        tokenResponse.setRefreshToken("newRefreshToken");

        // Then
        assertEquals("newAccessToken", tokenResponse.getAccessToken());
        assertNotNull(tokenResponse.getAccessTokenExpiresAt());
        assertEquals("newRefreshToken", tokenResponse.getRefreshToken());

    }

    @Test
    void testTokenResponseEquality() {

        // Given
        TokenResponse tokenResponse1 = TokenResponse.builder()
                .accessToken("token1")
                .accessTokenExpiresAt(1234567890L)
                .refreshToken("refreshToken1")
                .build();

        TokenResponse tokenResponse3 = TokenResponse.builder()
                .accessToken("token2")
                .accessTokenExpiresAt(9876543210L)
                .refreshToken("refreshToken2")
                .build();

        // When & Then

        assertNotEquals(tokenResponse1, tokenResponse3);

    }

}
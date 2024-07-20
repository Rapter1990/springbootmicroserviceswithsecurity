package com.springbootmicroservices.authservice.service.impl;

import com.springbootmicroservices.authservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.authservice.client.UserServiceClient;
import com.springbootmicroservices.authservice.model.auth.dto.request.TokenRefreshRequest;
import com.springbootmicroservices.authservice.model.auth.dto.response.TokenResponse;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class RefreshTokenServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private UserServiceClient userServiceClient;


    @Test
    void refreshToken_ValidTokenRefreshRequest_ReturnsCustomResponse() {

        // Given
        TokenRefreshRequest tokenRefreshRequest = TokenRefreshRequest.builder()
                .refreshToken("validRefreshToken")
                .build();

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken("newAccessToken")
                .accessTokenExpiresAt(System.currentTimeMillis() + 3600)
                .refreshToken("newRefreshToken")
                .build();

        CustomResponse<TokenResponse> expectedResponse = CustomResponse.successOf(tokenResponse);

        // When
        when(userServiceClient.refreshToken(any(TokenRefreshRequest.class)))
                .thenReturn(expectedResponse);

        // Then
        CustomResponse<TokenResponse> result = refreshTokenService.refreshToken(tokenRefreshRequest);

        assertNotNull(result);
        assertTrue(result.getIsSuccess());
        assertEquals(HttpStatus.OK, result.getHttpStatus());
        assertEquals(tokenResponse, result.getResponse());

        // Verify
        verify(userServiceClient, times(1)).refreshToken(any(TokenRefreshRequest.class));

    }

    @Test
    void refreshToken_InvalidTokenRefreshRequest_ReturnsErrorResponse() {

        // Given
        TokenRefreshRequest tokenRefreshRequest = TokenRefreshRequest.builder()
                .refreshToken("invalidRefreshToken")
                .build();

        CustomResponse<TokenResponse> errorResponse = CustomResponse.<TokenResponse>builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .build();

        // When
        when(userServiceClient.refreshToken(any(TokenRefreshRequest.class)))
                .thenReturn(errorResponse);

        // Then
        CustomResponse<TokenResponse> result = refreshTokenService.refreshToken(tokenRefreshRequest);

        assertNotNull(result);
        assertFalse(result.getIsSuccess());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getHttpStatus());
        assertNull(result.getResponse());

        // Verify
        verify(userServiceClient, times(1)).refreshToken(any(TokenRefreshRequest.class));

    }

}
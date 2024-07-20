package com.springbootmicroservices.authservice.service.impl;

import com.springbootmicroservices.authservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.authservice.client.UserServiceClient;
import com.springbootmicroservices.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LogoutServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private LogoutServiceImpl logoutService;

    @Mock
    private UserServiceClient userServiceClient;

    @Test
    void logout_ValidTokenInvalidateRequest_ReturnsCustomResponse() {

        // Given
        TokenInvalidateRequest tokenInvalidateRequest = TokenInvalidateRequest.builder()
                .accessToken("validAccessToken")
                .refreshToken("validRefreshToken")
                .build();

        CustomResponse<Void> expectedResponse = CustomResponse.<Void>builder()
                .httpStatus(HttpStatus.OK)
                .isSuccess(true)
                .build();

        // When
        when(userServiceClient.logout(any(TokenInvalidateRequest.class)))
                .thenReturn(expectedResponse);

        CustomResponse<Void> result = logoutService.logout(tokenInvalidateRequest);

        // Then
        assertNotNull(result);
        assertTrue(result.getIsSuccess());
        assertEquals(HttpStatus.OK, result.getHttpStatus());

        // Verify
        verify(userServiceClient, times(1)).logout(any(TokenInvalidateRequest.class));

    }

    @Test
    public void logout_InvalidTokenInvalidateRequest_ReturnsErrorResponse() {

        // Given
        TokenInvalidateRequest tokenInvalidateRequest = TokenInvalidateRequest.builder()
                .accessToken("invalidAccessToken")
                .refreshToken("invalidRefreshToken")
                .build();

        CustomResponse<Void> errorResponse = CustomResponse.<Void>builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .build();

        // When
        when(userServiceClient.logout(any(TokenInvalidateRequest.class)))
                .thenReturn(errorResponse);

        // Then
        CustomResponse<Void> result = logoutService.logout(tokenInvalidateRequest);

        assertNotNull(result);
        assertFalse(result.getIsSuccess());
        assertEquals(HttpStatus.UNAUTHORIZED, result.getHttpStatus());

        // Verify
        verify(userServiceClient, times(1)).logout(any(TokenInvalidateRequest.class));

    }

}
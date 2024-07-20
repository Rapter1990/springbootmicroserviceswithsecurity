package com.springbootmicroservices.authservice.service.impl;

import com.springbootmicroservices.authservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.authservice.client.UserServiceClient;
import com.springbootmicroservices.authservice.model.auth.dto.request.LoginRequest;
import com.springbootmicroservices.authservice.model.auth.dto.response.TokenResponse;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserLoginServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private UserLoginServiceImpl userLoginService;

    @Mock
    private UserServiceClient userServiceClient;

    @Test
    void givenValidLoginRequest_whenLogin_ReturnsCustomResponse() {

        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("valid.email@example.com")
                .password("validPassword123")
                .build();
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken("access-token")
                .accessTokenExpiresAt(System.currentTimeMillis() + 3600)
                .refreshToken("refresh-token")
                .build();
        CustomResponse<TokenResponse> customResponse = CustomResponse.successOf(tokenResponse);

        // When
        when(userServiceClient.loginUser(any(LoginRequest.class))).thenReturn(customResponse);

        // Then
        CustomResponse<TokenResponse> response = userLoginService.login(loginRequest);

        assertNotNull(response);
        assertTrue(response.getIsSuccess());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(tokenResponse, response.getResponse());

        // Verify
        verify(userServiceClient, times(1)).loginUser(any(LoginRequest.class));

    }

    @Test
    void givenInvalidLoginRequest_whenLogin_ReturnsErrorResponse() {

        // Given
        LoginRequest loginRequest = new LoginRequest();
        CustomResponse<TokenResponse> errorResponse = CustomResponse.<TokenResponse>builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .build();

        // When
        when(userServiceClient.loginUser(any(LoginRequest.class))).thenReturn(errorResponse);


        // Then
        CustomResponse<TokenResponse> response = userLoginService.login(loginRequest);

        assertNotNull(response);
        assertFalse(response.getIsSuccess());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getHttpStatus());
        assertNull(response.getResponse());

        // Verify
        verify(userServiceClient, times(1)).loginUser(any(LoginRequest.class));

    }

}
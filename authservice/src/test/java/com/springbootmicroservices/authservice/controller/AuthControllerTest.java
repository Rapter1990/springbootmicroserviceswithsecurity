package com.springbootmicroservices.authservice.controller;

import com.springbootmicroservices.authservice.base.AbstractRestControllerTest;
import com.springbootmicroservices.authservice.model.auth.dto.request.LoginRequest;
import com.springbootmicroservices.authservice.model.auth.dto.request.RegisterRequest;
import com.springbootmicroservices.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.authservice.model.auth.dto.request.TokenRefreshRequest;
import com.springbootmicroservices.authservice.model.auth.dto.response.TokenResponse;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.authservice.service.LogoutService;
import com.springbootmicroservices.authservice.service.RefreshTokenService;
import com.springbootmicroservices.authservice.service.RegisterService;
import com.springbootmicroservices.authservice.service.UserLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends AbstractRestControllerTest {

    @MockBean
    private RegisterService registerService;

    @MockBean
    private UserLoginService userLoginService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private LogoutService logoutService;

    @Test
    void registerAdmin_ValidRequest_ReturnsSuccess() throws Exception {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("valid.email@example.com")
                .password("validPassword123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890100")
                .role("user")
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/authentication/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.httpStatus").value("OK"));

        // Verify
        verify(registerService, times(1)).registerUser(any(RegisterRequest.class));

    }

    @Test
    void loginUser_ValidRequest_ReturnsTokenResponse() throws Exception {

        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("valid.email@example.com")
                .password("validPassword123")
                .build();

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken("newAccessToken")
                .accessTokenExpiresAt(System.currentTimeMillis() + 3600)
                .refreshToken("newRefreshToken")
                .build();

        CustomResponse<TokenResponse> expectedResponse = CustomResponse.successOf(tokenResponse);

        // When
        when(userLoginService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        // Then
        mockMvc.perform(post("/api/v1/authentication/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.response.accessToken").value("newAccessToken"));

        // Verify
        verify(userLoginService, times(1)).login(any(LoginRequest.class));

    }

    @Test
    void refreshToken_ValidRequest_ReturnsTokenResponse() throws Exception {

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
        when(refreshTokenService.refreshToken(any(TokenRefreshRequest.class))).thenReturn(expectedResponse);

        // Then
        mockMvc.perform(post("/api/v1/authentication/users/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.response.accessToken").value("newAccessToken"));

        verify(refreshTokenService, times(1)).refreshToken(any(TokenRefreshRequest.class));
    }

    @Test
    void logout_ValidRequest_ReturnsSuccess() throws Exception {

        // Given
        TokenInvalidateRequest tokenInvalidateRequest = TokenInvalidateRequest.builder()
                .accessToken("validAccessToken")
                .refreshToken("validRefreshToken")
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/authentication/users/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenInvalidateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.httpStatus").value("OK"));

        // Verify
        verify(logoutService, times(1)).logout(any(TokenInvalidateRequest.class));

    }


}
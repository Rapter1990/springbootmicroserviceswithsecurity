package com.springbootmicroservices.userservice.controller;

import com.springbootmicroservices.userservice.base.AbstractRestControllerTest;
import com.springbootmicroservices.userservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.User;
import com.springbootmicroservices.userservice.model.user.dto.request.LoginRequest;
import com.springbootmicroservices.userservice.model.user.dto.request.RegisterRequest;
import com.springbootmicroservices.userservice.model.user.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.userservice.model.user.dto.request.TokenRefreshRequest;
import com.springbootmicroservices.userservice.model.user.dto.response.TokenResponse;
import com.springbootmicroservices.userservice.model.user.mapper.TokenToTokenResponseMapper;
import com.springbootmicroservices.userservice.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest extends AbstractRestControllerTest {

    @MockBean
    private RegisterService userRegisterService;

    @MockBean
    private UserLoginService userLoginService;

    @MockBean
    private RefreshTokenService userRefreshTokenService;

    @MockBean
    private LogoutService userLogoutService;

    @MockBean
    private TokenService tokenService;

    private final TokenToTokenResponseMapper tokenToTokenResponseMapper = TokenToTokenResponseMapper.initialize();

    @Test
    void givenValidAdminRegisterRequest_whenRegisterUser_thenSuccess() throws Exception {

        // Given
        final RegisterRequest userRegisterRequest = RegisterRequest.builder()
                .email("admin@example.com")
                .password("password")
                .firstName("Admin")
                .lastName("User")
                .phoneNumber("12345678910")
                .role("admin")
                .build();

        final User mockUser = User.builder()
                .id(UUID.randomUUID().toString())
                .email(userRegisterRequest.getEmail())
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .phoneNumber(userRegisterRequest.getPhoneNumber())
                .password(userRegisterRequest.getPassword())
                .build();

        // When
        when(userRegisterService.registerUser(any(RegisterRequest.class))).thenReturn(mockUser);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(CustomResponse.SUCCESS)));

        // Verify
        verify(userRegisterService, times(1)).registerUser(any(RegisterRequest.class));

    }

    @Test
    void givenLoginRequest_WhenLoginForUser_ThenReturnToken() throws Exception {

        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@example.com")
                .password("password")
                .build();

        Token mockToken = Token.builder()
                .accessToken("mockAccessToken")
                .accessTokenExpiresAt(3600L)
                .refreshToken("mockRefreshToken")
                .build();

        TokenResponse expectedTokenResponse = tokenToTokenResponseMapper.map(mockToken);

        // When
        when(userLoginService.login(any(LoginRequest.class))).thenReturn(mockToken);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken").value(expectedTokenResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt").value(expectedTokenResponse.getAccessTokenExpiresAt()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken").value(expectedTokenResponse.getRefreshToken()));

        // Verify
        verify(userLoginService, times(1)).login(any(LoginRequest.class));

    }


    @Test
    void givenTokenRefreshRequest_WhenRefreshTokenForUser_ThenReturnTokenResponse() throws Exception {

        // Given
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest("refreshToken");

        Token mockToken = Token.builder()
                .accessToken("mockAccessToken")
                .accessTokenExpiresAt(3600L)
                .refreshToken("mockRefreshToken")
                .build();

        TokenResponse expectedTokenResponse = tokenToTokenResponseMapper.map(mockToken);

        // When
        when(userRefreshTokenService.refreshToken(any(TokenRefreshRequest.class))).thenReturn(mockToken);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRefreshRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken").value(expectedTokenResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt").value(expectedTokenResponse.getAccessTokenExpiresAt()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken").value(expectedTokenResponse.getRefreshToken()));

        // Verify
        verify(userRefreshTokenService, times(1)).refreshToken(any(TokenRefreshRequest.class));

    }


    @Test
    void givenTokenInvalidateRequest_WhenLogoutForUser_ThenReturnInvalidateToken() throws Exception {

        // Given
        TokenInvalidateRequest tokenInvalidateRequest = TokenInvalidateRequest.builder()
                .accessToken("Bearer " + mockAdminToken.getAccessToken())
                .refreshToken(mockAdminToken.getRefreshToken())
                .build();

        // When
        doNothing().when(userLogoutService).logout(any(TokenInvalidateRequest.class));

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(tokenInvalidateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(CustomResponse.SUCCESS)));

        // Verify
        verify(userLogoutService, times(1)).logout(any(TokenInvalidateRequest.class));

    }

    @Test
    void givenValidToken_whenValidateToken_thenReturnOk() throws Exception {

        // Given
        String validToken = "validToken";

        // When
        doNothing().when(tokenService).verifyAndValidate(validToken);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/validate-token")
                        .param("token", validToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        verify(tokenService, times(1)).verifyAndValidate(validToken);

    }

    @Test
    void givenValidToken_whenGetAuthentication_thenReturnAuthentication() throws Exception {

        // Given
        String validToken = "validToken";
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("user", "password");

        // When
        when(tokenService.getAuthentication(validToken)).thenReturn(authenticationToken);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/authenticate")
                        .param("token", validToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.principal").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.credentials").value("password"));

        // Verify
        verify(tokenService, times(1)).getAuthentication(validToken);

    }

}
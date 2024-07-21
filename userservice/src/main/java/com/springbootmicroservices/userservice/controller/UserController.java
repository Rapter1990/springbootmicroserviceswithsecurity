package com.springbootmicroservices.userservice.controller;

import com.springbootmicroservices.userservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.dto.request.LoginRequest;
import com.springbootmicroservices.userservice.model.user.dto.request.RegisterRequest;
import com.springbootmicroservices.userservice.model.user.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.userservice.model.user.dto.request.TokenRefreshRequest;
import com.springbootmicroservices.userservice.model.user.dto.response.TokenResponse;
import com.springbootmicroservices.userservice.model.user.mapper.TokenToTokenResponseMapper;
import com.springbootmicroservices.userservice.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller named {@link UserController} for managing user-related operations.
 * Provides endpoints for user registration, token validation, login, token refresh, logout, and authentication retrieval.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final RegisterService registerService;

    private final TokenService tokenService;

    private final UserLoginService userLoginService;

    private final RefreshTokenService refreshTokenService;

    private final LogoutService logoutService;

    private final TokenToTokenResponseMapper tokenToTokenResponseMapper = TokenToTokenResponseMapper.initialize();

    /**
     * Registers a new user.
     *
     * @param registerRequest the user registration request containing user details
     * @return a {@link CustomResponse} indicating the success of the registration
     */
    @PostMapping("/register")
    public CustomResponse<Void> registerUser(@RequestBody @Validated final RegisterRequest registerRequest) {
        log.info("UserController | registerUser");
        registerService.registerUser(registerRequest);
        return CustomResponse.SUCCESS;
    }

    /**
     * Validates the provided token.
     *
     * @param token the token to be validated
     * @return a {@link ResponseEntity} with an HTTP status code indicating the validation result
     */
    @PostMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestParam String token) {
        log.info("UserController | validateToken");
        tokenService.verifyAndValidate(token);
        return ResponseEntity.ok().build();
    }

    /**
     * Logs in a user and generates a new token.
     *
     * @param loginRequest the login request containing user credentials
     * @return a {@link CustomResponse} containing the generated {@link TokenResponse}
     */
    @PostMapping("/login")
    public CustomResponse<TokenResponse> loginUser(@RequestBody @Valid final LoginRequest loginRequest) {
        log.info("UserController | validateToken");
        final Token token = userLoginService.login(loginRequest);
        final TokenResponse tokenResponse = tokenToTokenResponseMapper.map(token);
        return CustomResponse.successOf(tokenResponse);
    }

    /**
     * Refreshes the token based on the provided refresh request.
     *
     * @param tokenRefreshRequest the token refresh request containing the refresh token
     * @return a {@link CustomResponse} containing the new {@link TokenResponse}
     */
    @PostMapping("/refresh-token")
    public CustomResponse<TokenResponse> refreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest) {
        log.info("UserController | refreshToken");
        final Token token = refreshTokenService.refreshToken(tokenRefreshRequest);
        final TokenResponse tokenResponse = tokenToTokenResponseMapper.map(token);
        return CustomResponse.successOf(tokenResponse);
    }

    /**
     * Logs out a user by invalidating the provided token.
     *
     * @param tokenInvalidateRequest the request containing the token to be invalidated
     * @return a {@link CustomResponse} indicating the success of the logout operation
     */
    @PostMapping("/logout")
    public CustomResponse<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest) {
        log.info("UserController | logout");
        logoutService.logout(tokenInvalidateRequest);
        return CustomResponse.SUCCESS;
    }

    /**
     * Retrieves the authentication details for the provided token.
     *
     * @param token the token for which to retrieve authentication details
     * @return a {@link ResponseEntity} containing the {@link UsernamePasswordAuthenticationToken} for the token
     */
    @GetMapping("/authenticate")
    public ResponseEntity<UsernamePasswordAuthenticationToken> getAuthentication(@RequestParam String token) {
        UsernamePasswordAuthenticationToken authentication = tokenService.getAuthentication(token);
        return ResponseEntity.ok(authentication);
    }

}

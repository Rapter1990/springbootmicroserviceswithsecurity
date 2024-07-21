package com.springbootmicroservices.authservice.client;

import com.springbootmicroservices.authservice.model.auth.User;
import com.springbootmicroservices.authservice.model.auth.dto.request.LoginRequest;
import com.springbootmicroservices.authservice.model.auth.dto.request.RegisterRequest;
import com.springbootmicroservices.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.authservice.model.auth.dto.request.TokenRefreshRequest;
import com.springbootmicroservices.authservice.model.auth.dto.response.TokenResponse;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client interface named {@link UserServiceClient} for interacting with the User Service.
 * This client is used to perform various operations related to user management,
 * such as registration, token validation, login, token refresh, and logout.
 */
@FeignClient(name = "userservice", path = "/api/v1/users")
public interface UserServiceClient {

    /**
     * Registers a new user with the provided registration request.
     *
     * @param request the registration request containing user details
     * @return the registered user
     */
    @PostMapping("/register")
    ResponseEntity<User> register(@RequestBody @Valid final RegisterRequest request);

    /**
     * Validates the given token by making a POST request to the User Service.
     *
     * @param token the token to be validated
     */
    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);

    /**
     * Logs in a user with the provided login request.
     *
     * @param loginRequest the login request containing user credentials
     * @return the token response containing access and refresh tokens
     */
    @PostMapping("/login")
    CustomResponse<TokenResponse> loginUser(@RequestBody @Valid final LoginRequest loginRequest);

    /**
     * Refreshes the access token using the provided token refresh request.
     *
     * @param tokenRefreshRequest the token refresh request containing the refresh token
     * @return the token response containing new access and refresh tokens
     */
    @PostMapping("/refresh-token")
    CustomResponse<TokenResponse> refreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest);

    /**
     * Logs out a user by invalidating the provided token.
     *
     * @param tokenInvalidateRequest the token invalidate request containing the token to be invalidated
     * @return a response indicating the result of the logout operation
     */
    @PostMapping("/logout")
    CustomResponse<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest);

}

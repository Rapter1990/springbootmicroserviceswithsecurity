package com.springbootmicroservices.authservice.service.impl;

import com.springbootmicroservices.authservice.client.UserServiceClient;
import com.springbootmicroservices.authservice.model.auth.dto.request.LoginRequest;
import com.springbootmicroservices.authservice.model.auth.dto.response.TokenResponse;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.authservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link UserLoginService} interface.
 * Handles user login by processing the login request and generating authentication tokens via the {@link UserServiceClient}.
 */
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserServiceClient userServiceClient;

    /**
     * Logs in a user by processing the provided login request and generating authentication tokens.
     *
     * @param loginRequest the login request containing user credentials (email and password)
     * @return a {@link CustomResponse} containing the {@link TokenResponse} with authentication tokens
     */
    @Override
    public CustomResponse<TokenResponse> login(LoginRequest loginRequest) {
        return userServiceClient.loginUser(loginRequest);
    }

}

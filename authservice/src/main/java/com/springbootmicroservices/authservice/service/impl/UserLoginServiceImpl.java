package com.springbootmicroservices.authservice.service.impl;

import com.springbootmicroservices.authservice.client.UserServiceClient;
import com.springbootmicroservices.authservice.model.auth.Token;
import com.springbootmicroservices.authservice.model.auth.dto.request.LoginRequest;
import com.springbootmicroservices.authservice.model.auth.dto.response.TokenResponse;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.authservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserServiceClient userServiceClient;

    @Override
    public CustomResponse<TokenResponse> login(LoginRequest loginRequest) {
        return userServiceClient.loginUser(loginRequest);
    }
}

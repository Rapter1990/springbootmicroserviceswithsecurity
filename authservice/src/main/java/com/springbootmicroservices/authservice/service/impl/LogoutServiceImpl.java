package com.springbootmicroservices.authservice.service.impl;

import com.springbootmicroservices.authservice.client.UserServiceClient;
import com.springbootmicroservices.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.authservice.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final UserServiceClient userServiceClient;

    @Override
    public CustomResponse<Void> logout(TokenInvalidateRequest tokenInvalidateRequest) {
        return userServiceClient.logout(tokenInvalidateRequest);
    }
}

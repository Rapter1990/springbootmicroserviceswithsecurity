package com.springbootmicroservices.authservice.service.impl;

import com.springbootmicroservices.authservice.client.UserServiceClient;
import com.springbootmicroservices.authservice.model.auth.User;
import com.springbootmicroservices.authservice.model.auth.dto.request.RegisterRequest;
import com.springbootmicroservices.authservice.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserServiceClient userServiceClient;

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        return userServiceClient.register(registerRequest).getBody();
    }

}

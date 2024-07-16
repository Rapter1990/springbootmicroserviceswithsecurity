package com.springbootmicroservices.authservice.controller;

import com.springbootmicroservices.authservice.model.auth.dto.request.RegisterRequest;
import com.springbootmicroservices.authservice.model.auth.mapper.TokenToTokenResponseMapper;
import com.springbootmicroservices.authservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.authservice.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication/users")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterService registerService;


    private final TokenToTokenResponseMapper tokenToTokenResponseMapper = TokenToTokenResponseMapper.initialize();

    @PostMapping("/register")
    public CustomResponse<Void> registerAdmin(@RequestBody @Valid final RegisterRequest registerRequest) {
        registerService.registerUser(registerRequest);
        return CustomResponse.SUCCESS;
    }

}

package com.springbootmicroservices.authservice.controller;

import com.springbootmicroservices.authservice.model.auth.Token;
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

    private final UserLoginService userLoginService;

    private final RefreshTokenService refreshTokenService;

    private final LogoutService logoutService;


    @PostMapping("/register")
    public CustomResponse<Void> registerAdmin(@RequestBody @Valid final RegisterRequest registerRequest) {
        registerService.registerUser(registerRequest);
        return CustomResponse.SUCCESS;
    }

    @PostMapping("/login")
    public CustomResponse<TokenResponse> loginUser(@RequestBody @Valid final LoginRequest loginRequest) {
        return userLoginService.login(loginRequest);
    }

    @PostMapping("/refresh-token")
    public CustomResponse<TokenResponse> refreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest) {
        return refreshTokenService.refreshToken(tokenRefreshRequest);
    }

    @PostMapping("/logout")
    public CustomResponse<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest) {
        logoutService.logout(tokenInvalidateRequest);
        return CustomResponse.SUCCESS;
    }

}

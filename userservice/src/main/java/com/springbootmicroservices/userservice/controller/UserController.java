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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final RegisterService registerService;

    private final TokenService tokenService;

    private final UserLoginService userLoginService;

    private final RefreshTokenService refreshTokenService;

    private final LogoutService logoutService;

    private final TokenToTokenResponseMapper tokenToTokenResponseMapper = TokenToTokenResponseMapper.initialize();

    @PostMapping("/register")
    public CustomResponse<Void> registerUser(@RequestBody @Validated final RegisterRequest registerRequest) {
        registerService.registerUser(registerRequest);
        return CustomResponse.SUCCESS;
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestParam String token) {
        tokenService.verifyAndValidate(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public CustomResponse<TokenResponse> loginUser(@RequestBody @Valid final LoginRequest loginRequest) {
        final Token token = userLoginService.login(loginRequest);
        final TokenResponse tokenResponse = tokenToTokenResponseMapper.map(token);
        return CustomResponse.successOf(tokenResponse);
    }

    @PostMapping("/refresh-token")
    public CustomResponse<TokenResponse> refreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest) {
        final Token token = refreshTokenService.refreshToken(tokenRefreshRequest);
        final TokenResponse tokenResponse = tokenToTokenResponseMapper.map(token);
        return CustomResponse.successOf(tokenResponse);
    }

    @PostMapping("/logout")
    public CustomResponse<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest) {
        logoutService.logout(tokenInvalidateRequest);
        return CustomResponse.SUCCESS;
    }

}

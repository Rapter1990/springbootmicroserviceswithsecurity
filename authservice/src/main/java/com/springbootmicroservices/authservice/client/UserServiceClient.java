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

@FeignClient(name = "userservice", path = "/api/v1/users")
public interface UserServiceClient {

    @PostMapping("/register")
    ResponseEntity<User> register(@RequestBody @Valid final RegisterRequest request);

    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);

    @PostMapping("/login")
    CustomResponse<TokenResponse> loginUser(@RequestBody @Valid final LoginRequest loginRequest);

    @PostMapping("/refresh-token")
    CustomResponse<TokenResponse> refreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest);

    @PostMapping("/logout")
    CustomResponse<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest);

}

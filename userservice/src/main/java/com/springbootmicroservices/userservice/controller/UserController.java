package com.springbootmicroservices.userservice.controller;

import com.springbootmicroservices.userservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.userservice.model.user.dto.request.RegisterRequest;
import com.springbootmicroservices.userservice.service.RegisterService;
import com.springbootmicroservices.userservice.service.TokenService;
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

}

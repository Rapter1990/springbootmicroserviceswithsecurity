package com.springbootmicroservices.authservice.service.impl;

import com.springbootmicroservices.authservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.authservice.client.UserServiceClient;
import com.springbootmicroservices.authservice.model.auth.User;
import com.springbootmicroservices.authservice.model.auth.dto.request.RegisterRequest;
import com.springbootmicroservices.authservice.model.auth.enums.UserStatus;
import com.springbootmicroservices.authservice.model.auth.enums.UserType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private RegisterServiceImpl registerService;

    @Mock
    private UserServiceClient userServiceClient;

    @Test
    void registerUser_ValidRegisterRequest_ReturnsUser() {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("valid.email@example.com")
                .password("validPassword123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890100")
                .role("user")
                .build();

        User expectedUser = User.builder()
                .id(UUID.randomUUID().toString())
                .email("valid.email@example.com")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890100")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.USER)
                .build();

        // When
        when(userServiceClient.register(any(RegisterRequest.class)))
                .thenReturn(ResponseEntity.ok(expectedUser));

        // Then
        User result = registerService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals(expectedUser, result);

        // Verify
        verify(userServiceClient, times(1)).register(any(RegisterRequest.class));

    }

    @Test
    void registerUser_InvalidRegisterRequest_ReturnsNull() {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("invalid.email")
                .password("short")
                .firstName("")
                .lastName("")
                .phoneNumber("123")
                .role("")
                .build();

        // When
        when(userServiceClient.register(any(RegisterRequest.class)))
                .thenReturn(ResponseEntity.badRequest().build());

        // Then
        User result = registerService.registerUser(registerRequest);

        assertNull(result);

        // Verify
        verify(userServiceClient, times(1)).register(any(RegisterRequest.class));

    }

}
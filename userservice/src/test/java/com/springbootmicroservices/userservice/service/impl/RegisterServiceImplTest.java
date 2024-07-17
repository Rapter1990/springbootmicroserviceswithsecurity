package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.userservice.exception.UserAlreadyExistException;
import com.springbootmicroservices.userservice.model.user.User;
import com.springbootmicroservices.userservice.model.user.dto.request.RegisterRequest;
import com.springbootmicroservices.userservice.model.user.entity.UserEntity;
import com.springbootmicroservices.userservice.model.user.mapper.RegisterRequestToUserEntityMapper;
import com.springbootmicroservices.userservice.model.user.mapper.UserEntityToUserMapper;
import com.springbootmicroservices.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private RegisterServiceImpl userRegisterService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterRequestToUserEntityMapper registerRequestToUserEntityMapper = RegisterRequestToUserEntityMapper.initialize();

    private UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();


    @Test
    void givenUserRegisterRequest_whenRegisterUser_thenReturnUser() {

        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .build();

        String encodedPassword = "encodedPassword";

        UserEntity userEntity = registerRequestToUserEntityMapper.mapForSaving(request);

        User expected = userEntityToUserMapper.map(userEntity);

        // When
        when(userRepository.existsUserEntityByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Then
        User result = userRegisterService.registerUser(request);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expected.getFirstName(), result.getFirstName());
        assertEquals(expected.getLastName(), result.getLastName());

        // Verify
        verify(userRepository).save(any(UserEntity.class));

    }

    @Test
    void givenUserRegisterRequest_whenEmailAlreadyExists_thenThrowUserAlreadyExistException() {

        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .build();

        // When
        when(userRepository.existsUserEntityByEmail(request.getEmail())).thenReturn(true);

        // Then
        assertThrows(UserAlreadyExistException.class, () -> userRegisterService.registerUser(request));

        // Verify
        verify(userRepository, never()).save(any(UserEntity.class));

    }

}
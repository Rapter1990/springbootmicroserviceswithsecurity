package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.userservice.builder.TokenBuilder;
import com.springbootmicroservices.userservice.builder.UserEntityBuilder;
import com.springbootmicroservices.userservice.exception.UserNotFoundException;
import com.springbootmicroservices.userservice.exception.UserStatusNotValidException;
import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.dto.request.TokenRefreshRequest;
import com.springbootmicroservices.userservice.model.user.entity.UserEntity;
import com.springbootmicroservices.userservice.model.user.enums.UserStatus;
import com.springbootmicroservices.userservice.repository.UserRepository;
import com.springbootmicroservices.userservice.service.TokenService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RefreshTokenServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private RefreshTokenServiceImpl userRefreshTokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;


    @Test
    void refreshToken_ValidRefreshToken_ReturnsToken() {

        // Given
        String refreshTokenString = "mockRefreshToken";
        TokenRefreshRequest tokenRefreshRequest = TokenRefreshRequest.builder()
                .refreshToken(refreshTokenString)
                .build();

        UserEntity mockUserEntity = new UserEntityBuilder().withValidUserFields().build();

        Claims mockClaims = TokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getFirstName()
        );

        Token expectedToken = Token.builder()
                .accessToken("mockAccessToken")
                .accessTokenExpiresAt(123456789L)
                .refreshToken("newMockRefreshToken")
                .build();

        doNothing().when(tokenService).verifyAndValidate(refreshTokenString);
        when(tokenService.getPayload(refreshTokenString)).thenReturn(mockClaims);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(mockUserEntity));
        when(tokenService.generateToken(mockUserEntity.getClaims(), refreshTokenString)).thenReturn(expectedToken);

        // When
        Token actualToken = userRefreshTokenService.refreshToken(tokenRefreshRequest);

        // Then
        assertNotNull(actualToken);
        assertEquals(expectedToken.getAccessToken(), actualToken.getAccessToken());
        assertEquals(expectedToken.getAccessTokenExpiresAt(), actualToken.getAccessTokenExpiresAt());
        assertEquals(expectedToken.getRefreshToken(), actualToken.getRefreshToken());

        // Verify
        verify(tokenService).verifyAndValidate(refreshTokenString);
        verify(tokenService).getPayload(refreshTokenString);
        verify(userRepository).findById(anyString());
        verify(tokenService).generateToken(mockUserEntity.getClaims(), refreshTokenString);

    }

    @Test
    void refreshToken_InvalidRefreshToken_ThrowsException() {

        // Given
        String refreshTokenString = "invalidRefreshToken";
        TokenRefreshRequest tokenRefreshRequest = TokenRefreshRequest.builder()
                .refreshToken(refreshTokenString)
                .build();

        // Mock the behavior of verifyAndValidate to throw an exception
        doThrow(RuntimeException.class).when(tokenService).verifyAndValidate(refreshTokenString);

        // When, Then & Verify
        assertThrows(RuntimeException.class,
                () -> userRefreshTokenService.refreshToken(tokenRefreshRequest));

        // Verify that verifyAndValidate method was called with the expected argument
        verify(tokenService).verifyAndValidate(refreshTokenString);

        // Ensure no other interactions occurred
        verifyNoInteractions(userRepository);

    }

    @Test
    void refreshToken_UserNotFound_ThrowsException() {
        // Given
        String refreshTokenString = "validRefreshToken";
        TokenRefreshRequest tokenRefreshRequest = TokenRefreshRequest.builder()
                .refreshToken(refreshTokenString)
                .build();

        Claims mockClaims = TokenBuilder.getValidClaims("nonExistentUserId", "John");

        doNothing().when(tokenService).verifyAndValidate(refreshTokenString);
        when(tokenService.getPayload(refreshTokenString)).thenReturn(mockClaims);
        when(userRepository.findById("nonExistentUserId")).thenReturn(Optional.empty());

        // When, Then & Verify
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userRefreshTokenService.refreshToken(tokenRefreshRequest));

        assertEquals("""
            User not found!
            """, exception.getMessage());

        verify(tokenService).verifyAndValidate(refreshTokenString);
        verify(tokenService).getPayload(refreshTokenString);
        verify(userRepository).findById("nonExistentUserId");

    }

    @Test
    void refreshToken_InactiveAdmin_ThrowsException() {

        // Given
        String refreshTokenString = "validRefreshToken";
        TokenRefreshRequest tokenRefreshRequest = TokenRefreshRequest.builder()
                .refreshToken(refreshTokenString)
                .build();

        UserEntity inactiveUser = new UserEntityBuilder().withValidUserFields().withUserStatus(UserStatus.PASSIVE).build();

        Claims mockClaims = TokenBuilder.getValidClaims(inactiveUser.getId(), inactiveUser.getFirstName());

        doNothing().when(tokenService).verifyAndValidate(refreshTokenString);
        when(tokenService.getPayload(refreshTokenString)).thenReturn(mockClaims);
        when(userRepository.findById(inactiveUser.getId())).thenReturn(Optional.of(inactiveUser));

        // When, Then & Verify
        UserStatusNotValidException exception = assertThrows(UserStatusNotValidException.class,
                () -> userRefreshTokenService.refreshToken(tokenRefreshRequest));

        assertEquals("User status is not valid!\n UserStatus = PASSIVE", exception.getMessage());

        verify(tokenService).verifyAndValidate(refreshTokenString);
        verify(tokenService).getPayload(refreshTokenString);
        verify(userRepository).findById(inactiveUser.getId());

    }

}
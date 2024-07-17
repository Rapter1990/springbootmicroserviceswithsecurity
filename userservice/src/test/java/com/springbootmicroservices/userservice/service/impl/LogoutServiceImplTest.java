package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.userservice.builder.TokenBuilder;
import com.springbootmicroservices.userservice.builder.UserEntityBuilder;
import com.springbootmicroservices.userservice.model.user.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.userservice.model.user.entity.UserEntity;
import com.springbootmicroservices.userservice.service.InvalidTokenService;
import com.springbootmicroservices.userservice.service.TokenService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogoutServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private LogoutServiceImpl userLogoutService;

    @Mock
    private TokenService tokenService;

    @Mock
    private InvalidTokenService invalidTokenService;

    @Test
    void givenAccessTokenAndRefreshToken_whenLogoutForUser_thenReturnLogout() {

        UserEntity mockUserEntity = new UserEntityBuilder().withValidUserFields().build();

        Claims mockAccessTokenClaims = TokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getFirstName()
        );

        Claims mockRefreshTokenClaims = TokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getFirstName()
        );

        String mockAccessTokenId = mockAccessTokenClaims.getId();
        String mockRefreshTokenId = mockRefreshTokenClaims.getId();

        // Given
        String accessToken = "validAccessToken";
        String refreshToken = "validRefreshToken";

        TokenInvalidateRequest tokenInvalidateRequest = TokenInvalidateRequest.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // When
        doNothing().when(tokenService).verifyAndValidate(Set.of(accessToken, refreshToken));
        when(tokenService.getPayload(accessToken)).thenReturn(mockAccessTokenClaims);
        doNothing().when(invalidTokenService).checkForInvalidityOfToken(mockAccessTokenId);
        when(tokenService.getPayload(refreshToken)).thenReturn(mockRefreshTokenClaims);
        doNothing().when(invalidTokenService).checkForInvalidityOfToken(mockRefreshTokenId);
        doNothing().when(invalidTokenService).invalidateTokens(Set.of(mockAccessTokenId, mockRefreshTokenId));

        // Then
        userLogoutService.logout(tokenInvalidateRequest);

        // Verify
        verify(tokenService).verifyAndValidate(Set.of(accessToken, refreshToken));
        verify(tokenService, times(2)).getPayload(anyString());
        verify(invalidTokenService, times(2)).checkForInvalidityOfToken(anyString());
        verify(invalidTokenService).invalidateTokens(anySet());

    }

}
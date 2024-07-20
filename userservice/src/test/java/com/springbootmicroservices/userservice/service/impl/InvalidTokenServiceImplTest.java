package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.userservice.exception.TokenAlreadyInvalidatedException;
import com.springbootmicroservices.userservice.model.user.entity.InvalidTokenEntity;
import com.springbootmicroservices.userservice.repository.InvalidTokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InvalidTokenServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private InvalidTokenServiceImpl invalidTokenService;

    @Mock
    private InvalidTokenRepository invalidTokenRepository;

    @Test
    void givenTokenIds_whenInvalidateTokens_thenSaveAllTokens() {

        // Given
        Set<String> tokenIds = Set.of("token1", "token2");

        // When
        invalidTokenService.invalidateTokens(tokenIds);

        // Then
        ArgumentCaptor<Set<InvalidTokenEntity>> captor = ArgumentCaptor.forClass(Set.class);
        verify(invalidTokenRepository).saveAll(captor.capture());
        Set<InvalidTokenEntity> capturedTokens = captor.getValue();

        assertThat(capturedTokens)
                .hasSize(2)
                .extracting("tokenId")
                .containsExactlyInAnyOrder("token1", "token2");

    }

    @Test
    void givenInvalidToken_whenCheckForInvalidityOfToken_thenThrowTokenAlreadyInvalidatedException() {

        // Given
        String tokenId = "invalidToken";
        InvalidTokenEntity invalidTokenEntity = InvalidTokenEntity.builder().tokenId(tokenId).build();

        // When
        when(invalidTokenRepository.findByTokenId(tokenId)).thenReturn(Optional.of(invalidTokenEntity));

        // Then
        assertThatThrownBy(() -> invalidTokenService.checkForInvalidityOfToken(tokenId))
                .isInstanceOf(TokenAlreadyInvalidatedException.class)
                .hasMessageContaining(tokenId);

        // Verify
        verify(invalidTokenRepository).findByTokenId(tokenId);

    }

    @Test
    void givenValidToken_whenCheckForInvalidityOfToken_thenDoNotThrowException() {

        // Given
        String tokenId = "validToken";

        // When
        when(invalidTokenRepository.findByTokenId(tokenId)).thenReturn(Optional.empty());

        // Then
        invalidTokenService.checkForInvalidityOfToken(tokenId);

        // Verify
        verify(invalidTokenRepository).findByTokenId(tokenId);

    }

}
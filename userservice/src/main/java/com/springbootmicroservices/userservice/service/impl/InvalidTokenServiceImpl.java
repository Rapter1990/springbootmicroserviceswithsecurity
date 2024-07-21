package com.springbootmicroservices.userservice.service.impl;


import com.springbootmicroservices.userservice.exception.TokenAlreadyInvalidatedException;
import com.springbootmicroservices.userservice.model.user.entity.InvalidTokenEntity;
import com.springbootmicroservices.userservice.repository.InvalidTokenRepository;
import com.springbootmicroservices.userservice.service.InvalidTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link InvalidTokenService} for managing invalid tokens.
 */
@Service
@RequiredArgsConstructor
public class InvalidTokenServiceImpl implements InvalidTokenService {

    private final InvalidTokenRepository invalidTokenRepository;

    /**
     * Invalidates a set of tokens by saving them as invalid in the repository.
     *
     * @param tokenIds a set of token IDs to be invalidated.
     */
    @Override
    public void invalidateTokens(Set<String> tokenIds) {

        final Set<InvalidTokenEntity> enocaInvalidTokenEntities = tokenIds.stream()
                .map(tokenId -> InvalidTokenEntity.builder()
                        .tokenId(tokenId)
                        .build()
                )
                .collect(Collectors.toSet());

        invalidTokenRepository.saveAll(enocaInvalidTokenEntities);
    }

    /**
     * Checks if a token has been invalidated by its ID.
     *
     * @param tokenId the token ID to check.
     * @throws TokenAlreadyInvalidatedException if the token has already been invalidated.
     */
    @Override
    public void checkForInvalidityOfToken(String tokenId) {

        final boolean isTokenInvalid = invalidTokenRepository.findByTokenId(tokenId).isPresent();

        if (isTokenInvalid) {
            throw new TokenAlreadyInvalidatedException(tokenId);
        }

    }

}

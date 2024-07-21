package com.springbootmicroservices.userservice.service;

import java.util.Set;

/**
 * Service interface named {@link InvalidTokenService} for managing invalid tokens.
 */
public interface InvalidTokenService {

    /**
     * Invalidates a set of tokens by their IDs.
     *
     * @param tokenIds a set of token IDs to be invalidated.
     */
    void invalidateTokens(final Set<String> tokenIds);

    /**
     * Checks if a token has been invalidated by its ID.
     *
     * @param tokenId the token ID to check.
     */
    void checkForInvalidityOfToken(final String tokenId);

}

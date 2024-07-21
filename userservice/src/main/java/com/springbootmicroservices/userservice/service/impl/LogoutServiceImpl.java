package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.model.user.dto.request.TokenInvalidateRequest;
import com.springbootmicroservices.userservice.service.InvalidTokenService;
import com.springbootmicroservices.userservice.service.LogoutService;
import com.springbootmicroservices.userservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Implementation of {@link LogoutService} for handling user logout operations.
 */
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final TokenService tokenService;
    private final InvalidTokenService invalidTokenService;

    /**
     * Logs out a user by invalidating their access and refresh tokens.
     *
     * @param tokenInvalidateRequest the request containing the tokens to be invalidated.
     */
    @Override
    public void logout(TokenInvalidateRequest tokenInvalidateRequest) {

        tokenService.verifyAndValidate(
                Set.of(
                        tokenInvalidateRequest.getAccessToken(),
                        tokenInvalidateRequest.getRefreshToken()
                )
        );

        final String accessTokenId = tokenService
                .getPayload(tokenInvalidateRequest.getAccessToken())
                .getId();

        invalidTokenService.checkForInvalidityOfToken(accessTokenId);


        final String refreshTokenId = tokenService
                .getPayload(tokenInvalidateRequest.getRefreshToken())
                .getId();

        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        invalidTokenService.invalidateTokens(Set.of(accessTokenId,refreshTokenId));

    }

}

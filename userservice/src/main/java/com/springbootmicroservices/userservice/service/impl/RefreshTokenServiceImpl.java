package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.exception.UserNotFoundException;
import com.springbootmicroservices.userservice.exception.UserStatusNotValidException;
import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.dto.request.TokenRefreshRequest;
import com.springbootmicroservices.userservice.model.user.entity.UserEntity;
import com.springbootmicroservices.userservice.model.user.enums.TokenClaims;
import com.springbootmicroservices.userservice.model.user.enums.UserStatus;
import com.springbootmicroservices.userservice.repository.UserRepository;
import com.springbootmicroservices.userservice.service.RefreshTokenService;
import com.springbootmicroservices.userservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link RefreshTokenService} for handling token refresh operations.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRepository userRepository;

    private final TokenService tokenService;

    /**
     * Refreshes the token based on the provided {@link TokenRefreshRequest}.
     *
     * <p>This method verifies and validates the provided refresh token, retrieves the associated user from the database,
     * and generates a new token for the user.</p>
     *
     * @param tokenRefreshRequest the request containing the refresh token.
     * @return a new {@link Token} generated for the user.
     * @throws UserNotFoundException if the user associated with the refresh token is not found.
     * @throws UserStatusNotValidException if the user's status is not active.
     */
    @Override
    public Token refreshToken(TokenRefreshRequest tokenRefreshRequest) {

        tokenService.verifyAndValidate(tokenRefreshRequest.getRefreshToken());

        final String adminId = tokenService
                .getPayload(tokenRefreshRequest.getRefreshToken())
                .get(TokenClaims.USER_ID.getValue())
                .toString();

        final UserEntity userEntityFromDB = userRepository
                .findById(adminId)
                .orElseThrow(UserNotFoundException::new);

        this.validateUserStatus(userEntityFromDB);

        return tokenService.generateToken(
                userEntityFromDB.getClaims(),
                tokenRefreshRequest.getRefreshToken()
        );

    }

    /**
     * Validates the user status to ensure the user is active.
     *
     * @param userEntity the user entity to check.
     * @throws UserStatusNotValidException if the user's status is not active.
     */
    private void validateUserStatus(final UserEntity userEntity) {
        if (!(UserStatus.ACTIVE.equals(userEntity.getUserStatus()))) {
            throw new UserStatusNotValidException("UserStatus = " + userEntity.getUserStatus());
        }
    }

}

package com.springbootmicroservices.userservice.model.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * Represents authentication tokens used for access and refresh.
 * This class contains the access token, its expiration time, and the refresh token.
 */
@Getter
@Builder
public class Token {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;

    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Checks if the provided authorization header contains a Bearer token.
     *
     * @param authorizationHeader the authorization header to check
     * @return {@code true} if the header contains a Bearer token, {@code false} otherwise
     */
    public static boolean isBearerToken(final String authorizationHeader) {
        return StringUtils.hasText(authorizationHeader) &&
                authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    /**
     * Extracts the JWT from the provided authorization header.
     *
     * @param authorizationHeader the authorization header containing the JWT
     * @return the JWT extracted from the header
     */
    public static String getJwt(final String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX, "");
    }

}

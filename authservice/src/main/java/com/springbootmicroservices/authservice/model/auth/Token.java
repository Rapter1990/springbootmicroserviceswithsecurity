package com.springbootmicroservices.authservice.model.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * Represents an authentication token named {@link Token} used for securing API requests.
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
     * Checks if the given authorization header contains a Bearer token.
     *
     * @param authorizationHeader the Authorization header value
     * @return {@code true} if the header starts with "Bearer "; {@code false} otherwise
     */
    public static boolean isBearerToken(final String authorizationHeader) {
        return StringUtils.hasText(authorizationHeader) &&
                authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    /**
     * Extracts the JWT from the given Authorization header.
     * The JWT is expected to follow the Bearer token prefix.
     *
     * @param authorizationHeader the Authorization header value
     * @return the JWT extracted from the header, without the "Bearer " prefix
     */
    public static String getJwt(final String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX, "");
    }

}

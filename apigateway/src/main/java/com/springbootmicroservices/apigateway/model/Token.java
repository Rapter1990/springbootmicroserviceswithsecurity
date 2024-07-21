package com.springbootmicroservices.apigateway.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * Represents a Token that includes an access token, its expiration time, and a refresh token.
 * Provides utility methods to check if a token is a Bearer token and to extract the JWT from an authorization header.
 */
@Getter
@Builder
public class Token {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;

    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Checks if the given authorization header is a Bearer token.
     *
     * @param authorizationHeader the authorization header to check
     * @return true if the authorization header is a Bearer token, false otherwise
     */
    public static boolean isBearerToken(final String authorizationHeader) {
        return StringUtils.hasText(authorizationHeader) &&
                authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    /**
     * Extracts the JWT from the given Bearer token authorization header.
     *
     * @param authorizationHeader the authorization header containing the Bearer token
     * @return the JWT extracted from the authorization header
     */
    public static String getJwt(final String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX, "");
    }

}

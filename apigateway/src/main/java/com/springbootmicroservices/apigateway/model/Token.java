package com.springbootmicroservices.apigateway.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Builder
public class Token {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;

    private static final String TOKEN_PREFIX = "Bearer ";

    public static boolean isBearerToken(final String authorizationHeader) {
        return StringUtils.hasText(authorizationHeader) &&
                authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    public static String getJwt(final String authorizationHeader) {
        return authorizationHeader.replace(TOKEN_PREFIX, "");
    }

}

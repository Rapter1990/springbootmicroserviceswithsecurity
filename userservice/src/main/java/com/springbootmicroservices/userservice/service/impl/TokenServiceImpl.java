package com.springbootmicroservices.userservice.service.impl;

import com.springbootmicroservices.userservice.config.TokenConfigurationParameter;
import com.springbootmicroservices.userservice.model.user.Token;
import com.springbootmicroservices.userservice.model.user.enums.TokenClaims;
import com.springbootmicroservices.userservice.model.user.enums.TokenType;
import com.springbootmicroservices.userservice.model.user.enums.UserType;
import com.springbootmicroservices.userservice.service.InvalidTokenService;
import com.springbootmicroservices.userservice.service.TokenService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * Implementation of {@link TokenService} for handling token-related operations such as generation, validation, and parsing.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final TokenConfigurationParameter tokenConfigurationParameter;
    private final InvalidTokenService invalidTokenService;

    /**
     * Generates a new access token and a refresh token based on the provided claims.
     *
     * <p>This method creates an access token with a short expiration time and a refresh token with a longer expiration time.
     * The tokens are signed with the private key specified in the configuration parameters.</p>
     *
     * @param claims a map of claims to include in the access token.
     * @return a {@link Token} object containing the generated access token and refresh token.
     */
    public Token generateToken(final Map<String, Object> claims) {

        final long currentTimeMillis = System.currentTimeMillis();

        final Date tokenIssuedAt = new Date(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(
                new Date(currentTimeMillis),
                tokenConfigurationParameter.getAccessTokenExpireMinute()
        );

        final String accessToken = Jwts.builder()
                .header()
                .type(TokenType.BEARER.getValue())
                .and()
                .id(UUID.randomUUID().toString())
                .issuedAt(tokenIssuedAt)
                .expiration(accessTokenExpiresAt)
                .signWith(tokenConfigurationParameter.getPrivateKey())
                .claims(claims)
                .compact();

        final Date refreshTokenExpiresAt = DateUtils.addDays(
                new Date(currentTimeMillis),
                tokenConfigurationParameter.getRefreshTokenExpireDay()
        );

        final String refreshToken = Jwts.builder()
                .header()
                .type(TokenType.BEARER.getValue())
                .and()
                .id(UUID.randomUUID().toString())
                .issuedAt(tokenIssuedAt)
                .expiration(refreshTokenExpiresAt)
                .signWith(tokenConfigurationParameter.getPrivateKey())
                .claim(TokenClaims.USER_ID.getValue(), claims.get(TokenClaims.USER_ID.getValue()))
                .compact();

        return Token.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();

    }

    /**
     * Generates a new access token using the provided claims and a refresh token.
     *
     * <p>This method verifies the validity of the provided refresh token, creates a new access token with the claims,
     * and returns the new access token along with the provided refresh token.</p>
     *
     * @param claims a map of claims to include in the new access token.
     * @param refreshToken the existing refresh token.
     * @return a {@link Token} object containing the newly generated access token and the provided refresh token.
     */
    public Token generateToken(final Map<String, Object> claims, final String refreshToken) {
        final long currentTimeMillis = System.currentTimeMillis();

        final String refreshTokenId = this.getId(refreshToken);

        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        final Date accessTokenIssuedAt = new Date(currentTimeMillis);

        final Date accessTokenExpiresAt = DateUtils.addMinutes(
                new Date(currentTimeMillis),
                tokenConfigurationParameter.getAccessTokenExpireMinute()
        );

        final String accessToken = Jwts.builder()
                .header()
                .type(TokenType.BEARER.getValue())
                .and()
                .id(UUID.randomUUID().toString())
                .issuedAt(accessTokenIssuedAt)
                .expiration(accessTokenExpiresAt)
                .signWith(tokenConfigurationParameter.getPrivateKey())
                .claims(claims)
                .compact();

        return Token.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt.toInstant().getEpochSecond())
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Retrieves the authentication details from the given JWT token.
     * This method parses the token, extracts the claims, and creates an {@link UsernamePasswordAuthenticationToken}
     * object with the user's authorities.
     *
     * @param token the JWT token to parse.
     * @return an {@link UsernamePasswordAuthenticationToken} containing the user's authentication details.
     */
    public UsernamePasswordAuthenticationToken getAuthentication(final String token) {

        final Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(tokenConfigurationParameter.getPublicKey())
                .build()
                .parseSignedClaims(token);

        final JwsHeader jwsHeader = claimsJws.getHeader();
        final Claims payload = claimsJws.getPayload();

        final Jwt jwt = new org.springframework.security.oauth2.jwt.Jwt(
                token,
                payload.getIssuedAt().toInstant(),
                payload.getExpiration().toInstant(),
                Map.of(
                        TokenClaims.TYP.getValue(), jwsHeader.getType(),
                        TokenClaims.ALGORITHM.getValue(), jwsHeader.getAlgorithm()
                ),
                payload
        );

        final UserType userType = UserType.valueOf(payload.get(TokenClaims.USER_TYPE.getValue()).toString());

        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userType.name()));

        return UsernamePasswordAuthenticationToken
                .authenticated(jwt, null, authorities);

    }

    /**
     * Verifies and validates the given JWT token.
     * This method parses the token, checks for expiration, and logs the token claims for debugging purposes.
     * Throws exceptions if the token is expired or invalid.
     *
     * @param jwt the JWT token to verify.
     * @throws ResponseStatusException if the token is invalid or expired.
     */
    public void verifyAndValidate(final String jwt) {

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(tokenConfigurationParameter.getPublicKey())
                .build()
                .parseSignedClaims(jwt);

            // Log the claims for debugging purposes
            Claims claims = claimsJws.getPayload();
            log.info("Token claims: {}", claims);

            // Additional checks (e.g., expiration, issuer, etc.)
            if (claims.getExpiration().before(new Date())) {
                throw new JwtException("Token has expired");
            }

            log.info("Token is valid");

        } catch (ExpiredJwtException e) {
            log.error("Token has expired", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token has expired", e);
        } catch (JwtException e) {
            log.error("Invalid JWT token", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token", e);
        } catch (Exception e) {
            log.error("Error validating token", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error validating token", e);
        }
    }

    /**
     * Verifies and validates a set of JWT tokens.
     * This method iterates over the provided set of JWT tokens and validates each one using the {@link #verifyAndValidate(String)}
     * method.
     *
     * @param jwts a set of JWT tokens to verify.
     */
    @Override
    public void verifyAndValidate(final Set<String> jwts) {
        jwts.forEach(this::verifyAndValidate);
    }

    /**
     * Retrieves the claims from the given JWT token.
     * This method parses the token and returns the claims as a {@link Jws} object.
     *
     * @param jwt the JWT token to parse.
     * @return a {@link Jws} object containing the claims from the token.
     */
    public Jws<Claims> getClaims(final String jwt) {
        return Jwts.parser()
                .verifyWith(tokenConfigurationParameter.getPublicKey())
                .build()
                .parseSignedClaims(jwt);

    }

    /**
     * Retrieves the payload (claims) from the given JWT token.
     * This method parses the token and returns the payload (claims) as a {@link Claims} object.
     *
     * @param jwt the JWT token to parse.
     * @return a {@link Claims} object containing the payload from the token.
     */
    public Claims getPayload(final String jwt) {
        return Jwts.parser()
                .verifyWith(tokenConfigurationParameter.getPublicKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    /**
     * Retrieves the ID from the given JWT token.
     * This method parses the token and returns the ID from the token's payload.
     *
     * @param jwt the JWT token to parse.
     * @return the ID from the token's payload.
     */
    public String getId(final String jwt) {
        return Jwts.parser()
                .verifyWith(tokenConfigurationParameter.getPublicKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getId();
    }

}

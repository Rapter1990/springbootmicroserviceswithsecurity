package com.springbootmicroservices.userservice.service;


import com.springbootmicroservices.userservice.model.user.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;
import java.util.Set;

/**
 * Service interface named {@link TokenService} for managing tokens.
 */
public interface TokenService {

    /**
     * Generates a new token with the given claims.
     *
     * @param claims the claims to be included in the token.
     * @return the generated {@link Token}.
     */
    Token generateToken(final Map<String, Object> claims);

    /**
     * Generates a new token with the given claims and refresh token.
     *
     * @param claims the claims to be included in the token.
     * @param refreshToken the refresh token to associate with the generated token.
     * @return the generated {@link Token}.
     */
    Token generateToken(final Map<String, Object> claims, final String refreshToken);

    /**
     * Retrieves the {@link UsernamePasswordAuthenticationToken} for the given token.
     *
     * @param token the token to retrieve the authentication for.
     * @return the {@link UsernamePasswordAuthenticationToken} associated with the token.
     */
    UsernamePasswordAuthenticationToken getAuthentication(final String token);

    /**
     * Verifies and validates the given JWT.
     *
     * @param jwt the JWT to verify and validate.
     */
    void verifyAndValidate(final String jwt);

    /**
     * Verifies and validates a set of JWTs.
     *
     * @param jwts the set of JWTs to verify and validate.
     */
    void verifyAndValidate(final Set<String> jwts);

    /**
     * Extracts the claims from the given JWT.
     *
     * @param jwt the JWT to extract claims from.
     * @return the {@link Jws} containing the claims of the token.
     */
    Jws<Claims> getClaims(final String jwt);

    /**
     * Extracts the payload (claims) from the given JWT.
     *
     * @param jwt the JWT to extract the payload from.
     * @return the {@link Claims} payload of the token.
     */
    Claims getPayload(final String jwt);

    /**
     * Retrieves the ID of the given JWT.
     *
     * @param jwt the JWT to retrieve the ID from.
     * @return the ID of the token.
     */
    String getId(final String jwt);

}

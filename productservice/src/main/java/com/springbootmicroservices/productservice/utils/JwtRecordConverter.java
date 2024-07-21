package com.springbootmicroservices.productservice.utils;

import com.springbootmicroservices.productservice.model.auth.JwtRecord;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Utility class for converting between {@link Jwt} and {@link JwtRecord} representations.
 * Provides methods to convert a {@link Jwt} object to a {@link JwtRecord} and vice versa.
 */
@UtilityClass
public class JwtRecordConverter {

    /**
     * Converts a {@link Jwt} object into a {@link JwtRecord}.
     *
     * @param jwt the {@link Jwt} object to be converted
     * @return a {@link JwtRecord} representing the provided {@link Jwt} object
     */
    public JwtRecord convertJwtToJwtRecord(Jwt jwt) {
        return new JwtRecord(
                jwt.getTokenValue(),
                jwt.getHeaders(),
                jwt.getClaims(),
                jwt.getIssuedAt(),
                jwt.getExpiresAt(),
                jwt.getClaimAsString("sub"),
                jwt.getClaimAsString("iss"),
                jwt.getAudience().toString()
        );
    }

    /**
     * Converts a {@link JwtRecord} into a {@link Jwt} object.
     *
     * @param jwtRecord the {@link JwtRecord} to be converted
     * @return a {@link Jwt} object representing the provided {@link JwtRecord}
     */
    public Jwt convertJwtRecordToJwt(JwtRecord jwtRecord) {
        return new Jwt(
                jwtRecord.tokenValue(),
                jwtRecord.issuedAt(),
                jwtRecord.expiresAt(),
                jwtRecord.headers(),
                jwtRecord.claims()
        );
    }

}

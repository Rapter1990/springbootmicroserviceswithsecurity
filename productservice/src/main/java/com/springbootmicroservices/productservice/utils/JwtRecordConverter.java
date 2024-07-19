package com.springbootmicroservices.productservice.utils;

import com.springbootmicroservices.productservice.model.auth.JwtRecord;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.jwt.Jwt;

@UtilityClass
public class JwtRecordConverter {

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

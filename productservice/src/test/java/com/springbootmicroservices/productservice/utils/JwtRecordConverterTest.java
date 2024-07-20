package com.springbootmicroservices.productservice.utils;

import com.springbootmicroservices.productservice.model.auth.JwtRecord;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtRecordConverterTest {

    @Test
    void utilityClass_ShouldNotBeInstantiated() {
        assertThrows(InvocationTargetException.class, () -> {
            // Attempt to use reflection to create an instance of the utility class
            java.lang.reflect.Constructor<JwtRecordConverter> constructor = JwtRecordConverter.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    void givenJwt_whenConvertJwtToJwtRecord_thenCorrectlyConverted() {

        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "user123");
        claims.put("iss", "issuer");
        claims.put("aud", "audience");

        Jwt jwt = Jwt.withTokenValue("tokenValue")
                .header("alg", "RS256")
                .header("typ", "JWT")
                .claims(claimsConsumer -> claimsConsumer.putAll(claims)) // Corrected claim setting
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // When
        JwtRecord jwtRecord = JwtRecordConverter.convertJwtToJwtRecord(jwt);

        // Then
        assertThat(jwtRecord).isNotNull();
        assertThat(jwtRecord.tokenValue()).isEqualTo("tokenValue");
        assertThat(jwtRecord.headers()).isEqualTo(jwt.getHeaders());
        assertThat(jwtRecord.claims()).isEqualTo(jwt.getClaims());
        assertThat(jwtRecord.issuedAt()).isEqualTo(jwt.getIssuedAt());
        assertThat(jwtRecord.expiresAt()).isEqualTo(jwt.getExpiresAt());
        assertThat(jwtRecord.subject()).isEqualTo("user123");
        assertThat(jwtRecord.issuer()).isEqualTo("issuer");
        assertThat(jwtRecord.audience()).isEqualTo("[audience]");

    }

    @Test
    void givenJwtRecord_whenConvertJwtRecordToJwt_thenCorrectlyConverted() {

        // Given
        JwtRecord jwtRecord = new JwtRecord(
                "tokenValue",
                Map.of("alg", "RS256", "typ", "JWT"),
                Map.of("sub", "user123", "iss", "issuer", "aud", "audience"),
                Instant.now(),
                Instant.now().plusSeconds(3600),
                "user123",
                "issuer",
                "audience"
        );

        // When
        Jwt jwt = JwtRecordConverter.convertJwtRecordToJwt(jwtRecord);

        // Then
        assertThat(jwt).isNotNull();
        assertThat(jwt.getTokenValue()).isEqualTo("tokenValue");
        assertThat(jwt.getHeaders()).isEqualTo(jwtRecord.headers());
        assertThat(jwt.getClaims()).isEqualTo(jwtRecord.claims());
        assertThat(jwt.getIssuedAt()).isEqualTo(jwtRecord.issuedAt());
        assertThat(jwt.getExpiresAt()).isEqualTo(jwtRecord.expiresAt());
        assertThat(jwt.getClaimAsString("sub")).isEqualTo("user123");
        assertThat(jwt.getClaimAsString("iss")).isEqualTo("issuer");
        assertThat(jwt.getAudience()).contains("audience");

    }

}
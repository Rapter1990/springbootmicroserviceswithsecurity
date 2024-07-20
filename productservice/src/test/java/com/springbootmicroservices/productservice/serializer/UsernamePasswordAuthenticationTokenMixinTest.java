package com.springbootmicroservices.productservice.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UsernamePasswordAuthenticationTokenMixinTest {

    @Test
    void givenTokenWithMixin_whenSerializeAndDeserialize_thenCorrect() throws IOException {

        // Given
        Jwt jwt = Jwt.withTokenValue("tokenValue")
                .header("alg", "RS256")
                .header("typ", "JWT")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("sub", "user123")
                .claim("iss", URI.create("https://issuer.example.com"))
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                jwt, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule
        objectMapper.addMixIn(UsernamePasswordAuthenticationToken.class, UsernamePasswordAuthenticationTokenMixin.class);
        objectMapper.registerModule(new ParameterNamesModule()); // Register the ParameterNamesModule

        // When
        String json = objectMapper.writeValueAsString(token);
        UsernamePasswordAuthenticationToken deserializedToken = objectMapper.readValue(json, UsernamePasswordAuthenticationToken.class);

        // Then
        assertThat(deserializedToken).isNotNull();
        assertThat(deserializedToken.getCredentials()).isEqualTo("password");
        assertThat(deserializedToken.getAuthorities()).hasSize(1);
        assertThat(deserializedToken.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
        assertThat(deserializedToken.getPrincipal()).isInstanceOf(Jwt.class);
        Jwt deserializedJwt = (Jwt) deserializedToken.getPrincipal();
        assertThat(deserializedJwt.getTokenValue()).isEqualTo("tokenValue");
        assertThat(deserializedJwt.getClaims().get("sub")).isEqualTo("user123");
        assertThat(deserializedJwt.getClaims().get("iss")).isEqualTo("https://issuer.example.com");

    }

}
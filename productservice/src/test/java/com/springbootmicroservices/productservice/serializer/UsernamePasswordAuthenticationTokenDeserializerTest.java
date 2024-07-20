package com.springbootmicroservices.productservice.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UsernamePasswordAuthenticationTokenDeserializerTest {

    @Test
    void givenValidJson_whenDeserialize_thenReturnUsernamePasswordAuthenticationToken() throws IOException, JsonProcessingException, IOException {

        // Given
        String json = "{ \"principal\": { \"tokenValue\": \"tokenValue\", \"issuedAt\": \"2024-07-19T13:56:26.413522Z\", \"expiresAt\": \"2024-07-19T14:56:26.413522Z\", \"headers\": {\"alg\": \"RS256\"}, \"claims\": {\"sub\": \"user123\", \"iss\": \"issuer\", \"aud\": \"audience\"} }, \"credentials\": \"password\", \"authorities\": [ { \"authority\": \"ROLE_USER\" } ] }";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        // Mocking deserialization context
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        // Create the deserializer
        JsonDeserializer<UsernamePasswordAuthenticationToken> deserializer = new UsernamePasswordAuthenticationTokenDeserializer();

        // When
        UsernamePasswordAuthenticationToken token = deserializer.deserialize(jsonParser, deserializationContext);

        // Then
        assertThat(token).isNotNull();
        assertThat(token.getCredentials()).isEqualTo("password");
        assertThat(token.getAuthorities()).hasSize(1);
        assertThat(token.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
        assertThat(token.getPrincipal()).isInstanceOf(Jwt.class);
        Jwt jwt = (Jwt) token.getPrincipal();
        assertThat(jwt.getTokenValue()).isEqualTo("tokenValue");
        assertThat(jwt.getClaims().get("sub")).isEqualTo("user123");
        assertThat(jwt.getClaims().get("iss")).isEqualTo("issuer");
        assertThat(jwt.getClaims().get("aud")).isEqualTo("audience");

    }

}
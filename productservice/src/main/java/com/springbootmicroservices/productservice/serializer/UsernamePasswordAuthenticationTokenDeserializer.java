package com.springbootmicroservices.productservice.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springbootmicroservices.productservice.model.auth.JwtRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;

public class UsernamePasswordAuthenticationTokenDeserializer extends JsonDeserializer<UsernamePasswordAuthenticationToken> {

    private final ObjectMapper objectMapper;

    public UsernamePasswordAuthenticationTokenDeserializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for date/time support
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown properties
    }


    @Override
    public UsernamePasswordAuthenticationToken deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        // Extract the nested principal object and deserialize it into a JwtRecord object
        JsonNode principalNode = node.get("principal");
        JwtRecord principal = objectMapper.treeToValue(principalNode, JwtRecord.class);

        // Extracting the credentials
        String credentials = node.get("credentials").isNull() ? null : node.get("credentials").asText();

        return new UsernamePasswordAuthenticationToken(principal, credentials);
    }
}

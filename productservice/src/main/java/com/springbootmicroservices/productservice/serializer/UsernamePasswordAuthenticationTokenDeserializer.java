package com.springbootmicroservices.productservice.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springbootmicroservices.productservice.model.auth.JwtRecord;
import com.springbootmicroservices.productservice.utils.JwtRecordConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer for {@link UsernamePasswordAuthenticationToken}.
 * This deserializer converts JSON into a {@link UsernamePasswordAuthenticationToken} object.
 */
public class UsernamePasswordAuthenticationTokenDeserializer extends JsonDeserializer<UsernamePasswordAuthenticationToken> {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a {@link UsernamePasswordAuthenticationTokenDeserializer} with an {@link ObjectMapper}
     * configured to handle date/time serialization and ignore unknown properties.
     */
    public UsernamePasswordAuthenticationTokenDeserializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for date/time support
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown properties
    }

    /**
     * Deserializes JSON into a {@link UsernamePasswordAuthenticationToken} object.
     * Extracts the principal, credentials, and authorities from the JSON.
     *
     * @param p the {@link JsonParser} used to parse the JSON
     * @param ctxt the {@link DeserializationContext} in which the deserialization occurs
     * @return a {@link UsernamePasswordAuthenticationToken} object
     * @throws IOException if an I/O error occurs
     * @throws JsonProcessingException if a JSON processing error occurs
     */
    @Override
    public UsernamePasswordAuthenticationToken deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        // Extract the nested principal object and deserialize it into a JwtRecord object
        JsonNode principalNode = node.get("principal");
        JwtRecord jwtRecord = objectMapper.treeToValue(principalNode, JwtRecord.class);

        // Convert JwtRecord to Jwt
        Jwt principal = JwtRecordConverter.convertJwtRecordToJwt(jwtRecord);

        // Extracting the credentials
        String credentials = node.get("credentials").isNull() ? null : node.get("credentials").asText();

        // Extracting the authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        ArrayNode authoritiesNode = (ArrayNode) node.get("authorities");
        for (JsonNode authorityNode : authoritiesNode) {
            String authority = authorityNode.get("authority").asText();
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
    }

}

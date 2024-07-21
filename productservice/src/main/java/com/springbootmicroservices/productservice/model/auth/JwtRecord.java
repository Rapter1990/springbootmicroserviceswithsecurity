package com.springbootmicroservices.productservice.model.auth;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a JWT (JSON Web Token) with its components and metadata.
 * This record contains information about the token, its headers, claims,
 * issuance and expiration times, subject, issuer, and audience.
 */
public record JwtRecord(String tokenValue, Map<String, Object> headers, Map<String, Object> claims,
                        Instant issuedAt, Instant expiresAt, String subject, String issuer, String audience) {
    // You can add any necessary methods or constructors here if needed
}

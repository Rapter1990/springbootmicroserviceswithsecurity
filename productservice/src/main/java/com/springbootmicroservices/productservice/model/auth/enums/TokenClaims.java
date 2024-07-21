package com.springbootmicroservices.productservice.model.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing various JWT token claims and their corresponding keys.
 * This enum defines the standard claims used in JWT tokens along with their
 * JSON keys, which are utilized for encoding and decoding JWT payloads.
 */
@Getter
@RequiredArgsConstructor
public enum TokenClaims {

    JWT_ID("jti"),
    USER_ID("userId"),
    USER_TYPE("userType"),
    USER_STATUS("userStatus"),
    USER_FIRST_NAME("userFirstName"),
    USER_LAST_NAME("userLastName"),
    USER_EMAIL("userEmail"),
    USER_PHONE_NUMBER("userPhoneNumber"),
    STORE_TITLE("storeTitle"),
    ISSUED_AT("iat"),
    EXPIRES_AT("exp"),
    ALGORITHM("alg"),
    TYP("typ");

    private final String value;

}

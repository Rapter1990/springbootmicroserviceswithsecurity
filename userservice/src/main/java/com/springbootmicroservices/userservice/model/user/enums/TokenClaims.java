package com.springbootmicroservices.userservice.model.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing various token claims used in JSON Web Tokens (JWT).
 * This enum defines the standard and custom claims that can be included in JWTs
 * to provide information about the token and its associated user or context.
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

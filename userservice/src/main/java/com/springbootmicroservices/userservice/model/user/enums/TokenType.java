package com.springbootmicroservices.userservice.model.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the types of tokens used in authentication and authorization.
 * This enum is used to define and standardize the token types that are recognized
 * within the application, such as the "Bearer" token type.
 */
@Getter
@RequiredArgsConstructor
public enum TokenType {

    BEARER("Bearer");

    private final String value;

}

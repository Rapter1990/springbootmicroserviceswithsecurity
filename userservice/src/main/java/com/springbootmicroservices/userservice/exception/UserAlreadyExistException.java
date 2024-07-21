package com.springbootmicroservices.userservice.exception;

import java.io.Serial;

/**
 * Exception named {@link UserAlreadyExistException} thrown when an attempt is made to register a user who already exists.
 */
public class UserAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2178948664026920647L;

    private static final String DEFAULT_MESSAGE = """
            User already exist!
            """;

    /**
     * Constructs a {@code UserAlreadyExistException} with the default message.
     */
    public UserAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a {@code UserAlreadyExistException} with a custom message.
     *
     * @param message the detail message
     */
    public UserAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}

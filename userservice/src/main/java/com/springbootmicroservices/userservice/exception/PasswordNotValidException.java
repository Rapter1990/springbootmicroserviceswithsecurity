package com.springbootmicroservices.userservice.exception;

import java.io.Serial;

/**
 * Exception named {@link PasswordNotValidException} thrown when a password does not meet the required criteria.
 */
public class PasswordNotValidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7389659106153108528L;

    private static final String DEFAULT_MESSAGE = """
            Password is not valid!
            """;

    /**
     * Constructs a {@code PasswordNotValidException} with the default message.
     */
    public PasswordNotValidException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a {@code PasswordNotValidException} with a custom message.
     *
     * @param message the detail message
     */
    public PasswordNotValidException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}

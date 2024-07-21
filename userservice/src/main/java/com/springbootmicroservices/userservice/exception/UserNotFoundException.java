package com.springbootmicroservices.userservice.exception;

import java.io.Serial;

/**
 * Exception named {@link UserNotFoundException} thrown when a requested user cannot be found.
 */
public class UserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3952215105519401565L;

    private static final String DEFAULT_MESSAGE = """
            User not found!
            """;

    /**
     * Constructs a {@code UserNotFoundException} with the default message.
     */
    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a {@code UserNotFoundException} with a custom message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}

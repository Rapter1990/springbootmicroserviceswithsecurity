package com.springbootmicroservices.userservice.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3952215105519401565L;

    private static final String DEFAULT_MESSAGE = """
            User not found!
            """;

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}

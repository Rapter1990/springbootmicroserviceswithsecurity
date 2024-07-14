package com.springbootmicroservices.productservice.exception;

import java.io.Serial;

/**
 * Exception class named {@link ProductNotFoundException} thrown when a requested product cannot be found.
 */
public class ProductNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5854010258697200749L;

    private static final String DEFAULT_MESSAGE = """
            Product not found!
            """;

    /**
     * Constructs a new ProductNotFoundException with a default message.
     */
    public ProductNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a new ProductNotFoundException with a custom message appended to the default message.
     *
     * @param message the custom message indicating details about the exception
     */
    public ProductNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}

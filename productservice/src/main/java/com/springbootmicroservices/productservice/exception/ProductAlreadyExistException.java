package com.springbootmicroservices.productservice.exception;

import java.io.Serial;

/**
 * Exception class named {@link ProductAlreadyExistException} thrown when attempting to create a product that already exists.
 */
public class ProductAlreadyExistException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 53457089789182737L;

    private static final String DEFAULT_MESSAGE = """
            Product already exist!
            """;

    /**
     * Constructs a new ProductAlreadyExistException with a default message.
     */
    public ProductAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a new ProductAlreadyExistException with a custom message appended to the default message.
     *
     * @param message the custom message indicating details about the exception
     */
    public ProductAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}

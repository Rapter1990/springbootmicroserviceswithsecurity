package com.springbootmicroservices.userservice.exception;

import java.io.Serial;

/**
 * Exception named {@link TokenAlreadyInvalidatedException} thrown when a token has already been invalidated.
 */
public class TokenAlreadyInvalidatedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3922046409563858698L;

    private static final String DEFAULT_MESSAGE = """
            Token is already invalidated!
            """;

    /**
     * Constructs a {@code TokenAlreadyInvalidatedException} with the default message.
     */
    public TokenAlreadyInvalidatedException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a {@code TokenAlreadyInvalidatedException} with a custom message including the token ID.
     *
     * @param tokenId the ID of the invalidated token
     */
    public TokenAlreadyInvalidatedException(final String tokenId) {
        super(DEFAULT_MESSAGE + " TokenID = " + tokenId);
    }

}

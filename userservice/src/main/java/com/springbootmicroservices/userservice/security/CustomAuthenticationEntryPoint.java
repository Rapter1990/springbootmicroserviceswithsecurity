package com.springbootmicroservices.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springbootmicroservices.userservice.model.common.CustomError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;

/**
 * Custom implementation of the {@link AuthenticationEntryPoint} interface.
 * This component is responsible for handling unauthorized access attempts
 * by sending a custom error response when authentication fails.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * Handles authentication errors by sending a custom error response to the client.
     * Sets the HTTP status to 401 Unauthorized and sends a JSON response with error details.
     *
     * @param httpServletRequest the request that resulted in an AuthenticationException
     * @param httpServletResponse the response to send to the client
     * @param authenticationException the exception that triggered this entry point
     * @throws IOException if an input or output exception occurs
     */
    @Override
    public void commence(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
                         final AuthenticationException authenticationException) throws IOException {

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        final CustomError customError = CustomError.builder()
                .header(CustomError.Header.AUTH_ERROR.getName())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .build();

        final String responseBody = OBJECT_MAPPER
                .writer(DateFormat.getDateInstance())
                .writeValueAsString(customError);

        httpServletResponse.getOutputStream()
                .write(responseBody.getBytes());

    }

}

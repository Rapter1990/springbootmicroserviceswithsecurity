package com.springbootmicroservices.authservice.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springbootmicroservices.authservice.exception.CustomError;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomAuthenticationEntryPointTest {

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @BeforeEach
    public void setUp() {
        customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
    }

    @Test
    public void testCommence() throws IOException {
        // Mock objects
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                byteArrayOutputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                // No-op
            }
        };

        // Set up the mocks
        when(httpServletResponse.getOutputStream()).thenReturn(servletOutputStream);

        // Call the method to test
        customAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse, new AuthenticationException("Test") {});

        // Verify that the response status was set
        verify(httpServletResponse).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(httpServletResponse).setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Convert the response to a string and verify the content
        String responseBody = byteArrayOutputStream.toString(); // Use ByteArrayOutputStream
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        CustomError expectedCustomError = CustomError.builder()
                .header(CustomError.Header.AUTH_ERROR.getName())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .build();
        String expectedResponseBody = objectMapper.writeValueAsString(expectedCustomError);

        // Parse the JSON response and expected response
        JsonNode responseNode = objectMapper.readTree(responseBody);
        JsonNode expectedNode = objectMapper.readTree(expectedResponseBody);

        // Extract and format the 'time' fields
        String responseTime = responseNode.get("time").asText();
        JsonNode expectedTimeNode = expectedNode.get("time");

        // Define a DateTimeFormatter to compare up to minutes
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // Parse the time strings into LocalDateTime objects
        LocalDateTime responseDateTime = LocalDateTime.parse(responseTime, formatter);
        LocalDateTime expectedDateTime = convertArrayToLocalDateTime(expectedTimeNode);

        // Truncate to minutes for comparison
        responseDateTime = responseDateTime.truncatedTo(ChronoUnit.MINUTES);
        expectedDateTime = expectedDateTime.truncatedTo(ChronoUnit.MINUTES);

        // Compare only the date and time up to minutes
        assertEquals(expectedDateTime, responseDateTime);
    }

    private LocalDateTime convertArrayToLocalDateTime(JsonNode timeNode) {
        if (timeNode.isArray()) {
            int year = timeNode.get(0).asInt();
            int month = timeNode.get(1).asInt();
            int day = timeNode.get(2).asInt();
            int hour = timeNode.get(3).asInt();
            int minute = timeNode.get(4).asInt();
            int second = timeNode.get(5).asInt();
            int nano = timeNode.get(6).asInt();
            return LocalDateTime.of(year, month, day, hour, minute, second, nano);
        }
        throw new IllegalArgumentException("Unexpected time format: " + timeNode);
    }

}

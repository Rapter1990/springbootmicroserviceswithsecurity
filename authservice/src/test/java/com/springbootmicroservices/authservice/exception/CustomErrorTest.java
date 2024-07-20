package com.springbootmicroservices.authservice.exception;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;

public class CustomErrorTest {

    @Test
    void testCustomErrorBuilder_WithAllFields() {

        LocalDateTime now = LocalDateTime.now();
        CustomError.CustomSubError subError = CustomError.CustomSubError.builder()
                .message("Sub error message")
                .field("field")
                .value("value")
                .type("type")
                .build();

        CustomError customError = CustomError.builder()
                .time(now)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Main error message")
                .subErrors(Collections.singletonList(subError))
                .build();

        assertNotNull(customError);
        assertEquals(now, customError.getTime());
        assertEquals(HttpStatus.BAD_REQUEST, customError.getHttpStatus());
        assertEquals(CustomError.Header.VALIDATION_ERROR.getName(), customError.getHeader());
        assertEquals("Main error message", customError.getMessage());
        assertFalse(customError.getIsSuccess());
        assertNotNull(customError.getSubErrors());
        assertEquals(1, customError.getSubErrors().size());
        assertEquals(subError, customError.getSubErrors().get(0));
    }

    @Test
    void testCustomErrorBuilder_DefaultValues() {
        CustomError customError = CustomError.builder().build();

        assertNotNull(customError);
        assertNotNull(customError.getTime());
        assertEquals(false, customError.getIsSuccess());
        assertNull(customError.getHttpStatus());
        assertNull(customError.getHeader());
        assertNull(customError.getMessage());
        assertNull(customError.getSubErrors());
    }

    @Test
    void testCustomSubErrorBuilder_WithAllFields() {
        CustomError.CustomSubError subError = CustomError.CustomSubError.builder()
                .message("Sub error message")
                .field("field")
                .value("value")
                .type("type")
                .build();

        assertNotNull(subError);
        assertEquals("Sub error message", subError.getMessage());
        assertEquals("field", subError.getField());
        assertEquals("value", subError.getValue());
        assertEquals("type", subError.getType());
    }

    @Test
    void testCustomSubErrorBuilder_DefaultValues() {
        CustomError.CustomSubError subError = CustomError.CustomSubError.builder().build();

        assertNotNull(subError);
        assertNull(subError.getMessage());
        assertNull(subError.getField());
        assertNull(subError.getValue());
        assertNull(subError.getType());
    }

    @Test
    void testCustomErrorHeaderEnum() {
        assertEquals("API ERROR", CustomError.Header.API_ERROR.getName());
        assertEquals("ALREADY EXIST", CustomError.Header.ALREADY_EXIST.getName());
        assertEquals("NOT EXIST", CustomError.Header.NOT_FOUND.getName());
        assertEquals("VALIDATION ERROR", CustomError.Header.VALIDATION_ERROR.getName());
        assertEquals("DATABASE ERROR", CustomError.Header.DATABASE_ERROR.getName());
        assertEquals("PROCESS ERROR", CustomError.Header.PROCESS_ERROR.getName());
        assertEquals("AUTH ERROR", CustomError.Header.AUTH_ERROR.getName());
    }

}
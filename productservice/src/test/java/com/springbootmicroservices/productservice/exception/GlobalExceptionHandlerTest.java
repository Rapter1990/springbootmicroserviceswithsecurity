package com.springbootmicroservices.productservice.exception;

import com.springbootmicroservices.productservice.base.AbstractRestControllerTest;
import com.springbootmicroservices.productservice.model.common.CustomError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest extends AbstractRestControllerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void givenMethodArgumentNotValidException_whenHandleMethodArgumentNotValid_thenRespondWithBadRequest() {

        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
        List<ObjectError> objectErrors = Collections.singletonList(fieldError);

        when(bindingResult.getAllErrors()).thenReturn(objectErrors);

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Validation failed")
                .subErrors(Collections.singletonList(
                        CustomError.CustomSubError.builder()
                                .field("fieldName")
                                .message("error message")
                                .build()))
                .build();

        // When
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        CustomError actualError = (CustomError) responseEntity.getBody();
        checkCustomError(expectedError, actualError);

    }

    @Test
    void givenConstraintViolationException_whenHandlePathVariableErrors_thenRespondWithBadRequest() {

        // Given
        ConstraintViolation<String> mockViolation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);
        Set<ConstraintViolation<?>> violations = Set.of(mockViolation);
        ConstraintViolationException mockException = new ConstraintViolationException(violations);

        CustomError.CustomSubError subError = CustomError.CustomSubError.builder()
                .message("must not be null")
                .field("")
                .value("invalid value")
                .type("String") // Default to String if getRootBeanClass() is null
                .build();

        CustomError expectedError = CustomError.builder()
                .time(LocalDateTime.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Constraint violation")
                .subErrors(Collections.singletonList(subError))
                .build();

        // When
        when(mockViolation.getMessage()).thenReturn("must not be null");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);
        when(mockPath.toString()).thenReturn("field");
        when(mockViolation.getInvalidValue()).thenReturn("invalid value");
        when(mockViolation.getRootBeanClass()).thenReturn(String.class); // Ensure this does not return null

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handlePathVariableErrors(mockException);

        CustomError actualError = (CustomError) responseEntity.getBody();

        // Verify
        checkCustomError(expectedError, actualError);

    }

    @Test
    void givenRuntimeException_whenHandleRuntimeException_thenRespondWithNotFound() {

        // Given
        RuntimeException ex = new RuntimeException("Runtime exception message");

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Runtime exception message")
                .build();

        // When
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleRuntimeException(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        CustomError actualError = (CustomError) responseEntity.getBody();
        checkCustomError(expectedError, actualError);

    }

    @Test
    void givenProductAlreadyExistException_whenHandleProductAlreadyExistException_thenRespondWithConflict() {

        // Given
        ProductAlreadyExistException ex = new ProductAlreadyExistException();

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .header(CustomError.Header.ALREADY_EXIST.getName())
                .message("Product already exist!\n")
                .isSuccess(false)
                .build();

        // When
        ResponseEntity<CustomError> responseEntity = globalExceptionHandler.handleProductAlreadyExistException(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        CustomError actualError = responseEntity.getBody();
        checkCustomError(expectedError, actualError);

    }

    @Test
    void givenProductNotFoundException_whenHandleProductNotFoundException_thenRespondWithNotFound() {

        // Given
        ProductNotFoundException ex = new ProductNotFoundException();

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.NOT_FOUND.getName())
                .message("Product not found!\n")
                .isSuccess(false)
                .build();

        // When
        ResponseEntity<CustomError> responseEntity = globalExceptionHandler.handleProductNotFoundException(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        CustomError actualError = responseEntity.getBody();
        checkCustomError(expectedError, actualError);
    }



    private void checkCustomError(CustomError expectedError, CustomError actualError) {

        assertThat(actualError).isNotNull();
        assertThat(actualError.getTime()).isNotNull();
        assertThat(actualError.getHeader()).isEqualTo(expectedError.getHeader());
        assertThat(actualError.getIsSuccess()).isEqualTo(expectedError.getIsSuccess());

        if (expectedError.getMessage() != null) {
            assertThat(actualError.getMessage()).isEqualTo(expectedError.getMessage());
        }

        if (expectedError.getSubErrors() != null) {
            assertThat(actualError.getSubErrors().size()).isEqualTo(expectedError.getSubErrors().size());
            if (!expectedError.getSubErrors().isEmpty()) {
                assertThat(actualError.getSubErrors().get(0).getMessage()).isEqualTo(expectedError.getSubErrors().get(0).getMessage());
                assertThat(actualError.getSubErrors().get(0).getField()).isEqualTo(expectedError.getSubErrors().get(0).getField());
                assertThat(actualError.getSubErrors().get(0).getValue()).isEqualTo(expectedError.getSubErrors().get(0).getValue());
                assertThat(actualError.getSubErrors().get(0).getType()).isEqualTo(expectedError.getSubErrors().get(0).getType());
            }
        }
    }

}
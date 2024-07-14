package com.springbootmicroservices.productservice.model.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Represents a generic response object named {@link CustomResponse<T>} with standardized fields.
 *
 * @param <T> Type of the response payload.
 */
@Getter
@Builder
public class CustomResponse<T> {

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    private HttpStatus httpStatus;

    private Boolean isSuccess;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T response;

    /**
     * Default successful response with HTTP OK status and success indicator set to true.
     */
    public static final CustomResponse<Void> SUCCESS = CustomResponse.<Void>builder()
            .httpStatus(HttpStatus.OK)
            .isSuccess(true)
            .build();

    /**
     * Creates a successful response with the provided payload and HTTP OK status.
     *
     * @param <T>      Type of the response payload.
     * @param response Response payload.
     * @return CustomResponse instance with success status, HTTP OK, and the provided payload.
     */
    public static <T> CustomResponse<T> successOf(final T response) {
        return CustomResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .isSuccess(true)
                .response(response)
                .build();
    }

}

package com.springbootmicroservices.authservice.model.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Represents a request named {@link TokenInvalidateRequest} to invalidate tokens.
 * This class contains the access and refresh tokens that need to be invalidated.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInvalidateRequest {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

}

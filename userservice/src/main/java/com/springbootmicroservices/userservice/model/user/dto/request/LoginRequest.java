package com.springbootmicroservices.userservice.model.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a login request named {@link LoginRequest} containing the user's email and password.
 */
@Getter
@Setter
@Builder
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}

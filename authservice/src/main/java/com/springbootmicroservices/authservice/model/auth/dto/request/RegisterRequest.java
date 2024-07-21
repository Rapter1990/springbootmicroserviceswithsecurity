package com.springbootmicroservices.authservice.model.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Represents a request named {@link RegisterRequest} for user registration.
 * This class contains the necessary details required to register a new user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @Email(message = "Please enter valid e-mail address")
    @Size(min = 7, message = "Minimum e-mail length is 7 characters.")
    private String email;

    @Size(min = 8)
    private String password;

    @NotBlank(message = "First name can't be blank.")
    private String firstName;

    @NotBlank(message = "Last name can't be blank.")
    private String lastName;

    @NotBlank(message = "Phone number can't be blank.")
    @Size(min = 11, max = 20)
    private String phoneNumber;

    @NotBlank(message = "Role can't be blank.")
    private String role;

}

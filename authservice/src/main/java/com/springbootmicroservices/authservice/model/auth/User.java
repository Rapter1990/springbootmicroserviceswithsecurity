package com.springbootmicroservices.authservice.model.auth;

import com.springbootmicroservices.authservice.model.auth.enums.UserStatus;
import com.springbootmicroservices.authservice.model.auth.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a user named {@link User} in the system.
 * This class contains information about the user's identity, contact details, status, and type.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class User {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserStatus userStatus;
    private UserType userType;
}

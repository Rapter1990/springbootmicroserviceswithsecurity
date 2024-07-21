package com.springbootmicroservices.userservice.model.user;

import com.springbootmicroservices.userservice.model.common.BaseDomainModel;
import com.springbootmicroservices.userservice.model.user.enums.UserStatus;
import com.springbootmicroservices.userservice.model.user.enums.UserType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a user domain object named {@link User} in the system.
 * The {@code User} class is a domain model that contains user-related information such as
 * identification, contact details, status, type, and password. It extends {@link BaseDomainModel}
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDomainModel {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserStatus userStatus;
    private UserType userType;
    private String password;
}

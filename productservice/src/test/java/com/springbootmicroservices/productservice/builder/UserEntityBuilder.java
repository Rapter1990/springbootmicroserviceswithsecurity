package com.springbootmicroservices.productservice.builder;

import com.springbootmicroservices.productservice.model.auth.UserStatus;
import com.springbootmicroservices.productservice.model.auth.UserType;

import java.util.UUID;

public class UserEntityBuilder extends BaseBuilder<UserEntity> {

    public UserEntityBuilder() {
        super(UserEntity.class);
    }

    public UserEntityBuilder withValidUserFields() {
        return this
                .withId(UUID.randomUUID().toString())
                .withEmail("useradmin@example.com")
                .withPassword("useradmin")
                .withFirstName("John")
                .withLastName("Doe")
                .withPhoneNumber("12345678901011")
                .withUserType(UserType.USER)
                .withUserStatus(UserStatus.ACTIVE);
    }

    public UserEntityBuilder withValidAdminFields() {
        return this
                .withId(UUID.randomUUID().toString())
                .withEmail("user@example.com")
                .withPassword("userpassword")
                .withFirstName("John")
                .withLastName("Doe")
                .withPhoneNumber("12345678901012")
                .withUserType(UserType.ADMIN)
                .withUserStatus(UserStatus.ACTIVE);
    }

    public UserEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserEntityBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public UserEntityBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public UserEntityBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public UserEntityBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public UserEntityBuilder withPhoneNumber(String phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public UserEntityBuilder withUserType(UserType userType) {
        data.setUserType(userType);
        return this;
    }

    public UserEntityBuilder withUserStatus(UserStatus userStatus) {
        data.setUserStatus(userStatus);
        return this;
    }

}

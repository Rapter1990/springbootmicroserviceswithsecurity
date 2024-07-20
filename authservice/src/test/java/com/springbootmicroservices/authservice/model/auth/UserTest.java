package com.springbootmicroservices.authservice.model.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.springbootmicroservices.authservice.model.auth.enums.UserStatus;
import com.springbootmicroservices.authservice.model.auth.enums.UserType;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testUserBuilder_WithAllFields() {

        // Given
        String id = "12345";
        String email = "example@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String phoneNumber = "1234567890";
        UserStatus userStatus = UserStatus.ACTIVE;
        UserType userType = UserType.ADMIN;

        // When
        User user = User.builder()
                .id(id)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .userStatus(userStatus)
                .userType(userType)
                .build();

        // Then
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(userStatus, user.getUserStatus());
        assertEquals(userType, user.getUserType());

    }

    @Test
    void testUserBuilder_DefaultValues() {

        // When
        User user = User.builder().build();

        // Then
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPhoneNumber());
        assertNull(user.getUserStatus());
        assertNull(user.getUserType());

    }

    @Test
    void testUserSettersAndGetters() {

        // Given
        User user = new User();

        // When
        user.setId("12345");
        user.setEmail("example@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("1234567890");
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserType(UserType.ADMIN);

        // Then
        assertEquals("12345", user.getId());
        assertEquals("example@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals(UserStatus.ACTIVE, user.getUserStatus());
        assertEquals(UserType.ADMIN, user.getUserType());

    }

    @Test
    void testUserEquality() {

        // Given
        User user1 = User.builder()
                .id("12345")
                .email("example@example.com")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.ADMIN)
                .build();

        User user3 = User.builder()
                .id("67890")
                .email("different@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .phoneNumber("0987654321")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.USER)
                .build();

        // When & Then
        assertNotEquals(user1, user3);

    }

}

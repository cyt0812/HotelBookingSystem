package com.hotelbooking.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationRequestTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        UserRegistrationRequest request = new UserRegistrationRequest();

        // Assert
        assertNull(request.getUsername());
        assertNull(request.getEmail());
        assertNull(request.getPassword());
        assertNull(request.getRole());
    }

    @Test
    public void testParameterizedConstructor() {
        // Arrange & Act
        String username = "testuser";
        String email = "test@example.com";
        String password = "testpassword";
        String role = "CUSTOMER";
        UserRegistrationRequest request = new UserRegistrationRequest(username, email, password, role);

        // Assert
        assertEquals(username, request.getUsername());
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
        assertEquals(role, request.getRole());
    }

    @Test
    public void testSetUsername() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        String newUsername = "updateduser";

        // Act
        request.setUsername(newUsername);

        // Assert
        assertEquals(newUsername, request.getUsername());
    }

    @Test
    public void testSetEmail() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        String newEmail = "updated@example.com";

        // Act
        request.setEmail(newEmail);

        // Assert
        assertEquals(newEmail, request.getEmail());
    }

    @Test
    public void testSetPassword() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        String newPassword = "updatedpassword";

        // Act
        request.setPassword(newPassword);

        // Assert
        assertEquals(newPassword, request.getPassword());
    }

    @Test
    public void testSetRole() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        String newRole = "ADMIN";

        // Act
        request.setRole(newRole);

        // Assert
        assertEquals(newRole, request.getRole());
    }

    @Test
    public void testSettersAndGettersWithNullValues() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest("initialuser", "initial@example.com", "initialpassword", "CUSTOMER");

        // Act
        request.setUsername(null);
        request.setEmail(null);
        request.setPassword(null);
        request.setRole(null);

        // Assert
        assertNull(request.getUsername());
        assertNull(request.getEmail());
        assertNull(request.getPassword());
        assertNull(request.getRole());
    }

    @Test
    public void testSettersAndGettersWithEmptyValues() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest("initialuser", "initial@example.com", "initialpassword", "CUSTOMER");

        // Act
        request.setUsername("");
        request.setEmail("");
        request.setPassword("");
        request.setRole("");

        // Assert
        assertEquals("", request.getUsername());
        assertEquals("", request.getEmail());
        assertEquals("", request.getPassword());
        assertEquals("", request.getRole());
    }

    @Test
    public void testSettersAndGettersWithSpecialCharacters() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        String specialUsername = "user@name123";
        String specialEmail = "user.name+tag@example.co.uk";
        String specialPassword = "Passw0rd!@#$";
        String specialRole = "SUPER_ADMIN";

        // Act
        request.setUsername(specialUsername);
        request.setEmail(specialEmail);
        request.setPassword(specialPassword);
        request.setRole(specialRole);

        // Assert
        assertEquals(specialUsername, request.getUsername());
        assertEquals(specialEmail, request.getEmail());
        assertEquals(specialPassword, request.getPassword());
        assertEquals(specialRole, request.getRole());
    }
}

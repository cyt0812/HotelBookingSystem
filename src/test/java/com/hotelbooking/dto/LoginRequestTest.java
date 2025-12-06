package com.hotelbooking.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        LoginRequest loginRequest = new LoginRequest();

        // Assert
        assertNull(loginRequest.getUsername());
        assertNull(loginRequest.getPassword());
    }

    @Test
    public void testParameterizedConstructor() {
        // Arrange & Act
        String username = "testuser";
        String password = "testpassword";
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Assert
        assertEquals(username, loginRequest.getUsername());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    public void testSetUsername() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        String newUsername = "updateduser";

        // Act
        loginRequest.setUsername(newUsername);

        // Assert
        assertEquals(newUsername, loginRequest.getUsername());
    }

    @Test
    public void testSetPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        String newPassword = "updatedpassword";

        // Act
        loginRequest.setPassword(newPassword);

        // Assert
        assertEquals(newPassword, loginRequest.getPassword());
    }

    @Test
    public void testSettersAndGettersWithNullValues() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("initialuser", "initialpassword");

        // Act
        loginRequest.setUsername(null);
        loginRequest.setPassword(null);

        // Assert
        assertNull(loginRequest.getUsername());
        assertNull(loginRequest.getPassword());
    }

    @Test
    public void testSettersAndGettersWithEmptyValues() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("initialuser", "initialpassword");

        // Act
        loginRequest.setUsername("");
        loginRequest.setPassword("");

        // Assert
        assertEquals("", loginRequest.getUsername());
        assertEquals("", loginRequest.getPassword());
    }
}

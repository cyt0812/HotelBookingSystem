package com.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userCreation_WithAllFields_ShouldSetPropertiesCorrectly() {
        // Arrange & Act
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        
        // Assert
        assertEquals("john_doe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("CUSTOMER", user.getRole());
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void userSetters_ShouldUpdateFieldsCorrectly() {
        // Arrange
        User user = new User();
        
        // Act
        user.setId(1);
        user.setUsername("jane_doe");
        user.setEmail("jane@example.com");
        user.setPassword("newpassword");
        user.setRole("ADMIN");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        
        // Assert
        assertEquals(1, user.getId());
        assertEquals("jane_doe", user.getUsername());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals("ADMIN", user.getRole());
        assertEquals(now, user.getCreatedAt());
    }

//    @Test
//    void userCreation_WithNullUsername_ShouldThrowException() {
//        // Test if constructor throws exception when username is null
//        assertThrows(IllegalArgumentException.class, () -> {
//            new User(null, "test@test.com", "pass", "USER");
//        });
//    }
}
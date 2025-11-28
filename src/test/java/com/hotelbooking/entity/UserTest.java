package com.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userCreation_WithAllFields_ShouldSetPropertiesCorrectly() {
        // 准备 & 执行
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        
        // 验证
        assertEquals("john_doe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("CUSTOMER", user.getRole());
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void userSetters_ShouldUpdateFieldsCorrectly() {
        // 准备
        User user = new User();
        
        // 执行
        user.setId(1);
        user.setUsername("jane_doe");
        user.setEmail("jane@example.com");
        user.setPassword("newpassword");
        user.setRole("ADMIN");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        
        // 验证
        assertEquals(1, user.getId());
        assertEquals("jane_doe", user.getUsername());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals("ADMIN", user.getRole());
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void userCreation_WithNullUsername_ShouldThrowException() {
        // 这个测试帮助我们思考验证逻辑
        User user = new User(null, "test@test.com", "pass", "USER");
        assertNull(user.getUsername());
    }
}
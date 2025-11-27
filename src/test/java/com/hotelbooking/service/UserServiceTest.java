package com.hotelbooking.service;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDAO);
    }

    @Test
    void registerUser_WithValidData_ShouldCreateUser() {
        // 准备
        User expectedUser = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        expectedUser.setId(1);

        when(userDAO.createUser(any(User.class))).thenReturn(expectedUser);

        // 执行
        User result = userService.registerUser("john_doe", "john@example.com", "password123", "CUSTOMER");

        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("john_doe", result.getUsername());
        verify(userDAO, times(1)).createUser(any(User.class));
    }

    @Test
    void registerUser_WithEmptyUsername_ShouldThrowException() {
        // 执行和验证
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.registerUser("", "test@test.com", "password", "CUSTOMER")
        );
        
        assertEquals("Username cannot be empty", exception.getMessage());
        verify(userDAO, never()).createUser(any(User.class));
    }

    @Test
    void registerUser_WithInvalidEmail_ShouldThrowException() {
        // 执行和验证
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.registerUser("testuser", "invalid-email", "password", "CUSTOMER")
        );
        
        assertEquals("Invalid email format", exception.getMessage());
        verify(userDAO, never()).createUser(any(User.class));
    }

    @Test
    void loginUser_WithValidCredentials_ShouldReturnUser() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        user.setId(1);
        
        when(userDAO.getUserByUsername("john_doe")).thenReturn(Optional.of(user));
        
        // 执行
        Optional<User> result = userService.loginUser("john_doe", "password123");
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals("john_doe", result.get().getUsername());
        verify(userDAO, times(1)).getUserByUsername("john_doe");
    }

    @Test
    void loginUser_WithInvalidPassword_ShouldReturnEmpty() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        when(userDAO.getUserByUsername("john_doe")).thenReturn(Optional.of(user));
        
        // 执行
        Optional<User> result = userService.loginUser("john_doe", "wrongpassword");
        
        // 验证
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // 准备
        User user1 = new User("user1", "user1@test.com", "pass", "CUSTOMER");
        User user2 = new User("user2", "user2@test.com", "pass", "ADMIN");
        java.util.List<User> expectedUsers = java.util.Arrays.asList(user1, user2);
        
        when(userDAO.getAllUsers()).thenReturn(expectedUsers);
        
        // 执行
        java.util.List<User> result = userService.getAllUsers();
        
        // 验证
        assertEquals(2, result.size());
        verify(userDAO, times(1)).getAllUsers();
    }
}
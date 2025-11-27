package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void registerUser_WithValidData_ShouldReturnUser() {
        // 准备
        User expectedUser = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        expectedUser.setId(1);
        
        when(userService.registerUser("john_doe", "john@example.com", "password123", "CUSTOMER"))
                .thenReturn(expectedUser);
        
        // 执行
        User result = userController.registerUser("john_doe", "john@example.com", "password123", "CUSTOMER");
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("john_doe", result.getUsername());
        verify(userService, times(1)).registerUser("john_doe", "john@example.com", "password123", "CUSTOMER");
    }

    @Test
    void loginUser_WithValidCredentials_ShouldReturnUser() {
        // 准备
        User expectedUser = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        expectedUser.setId(1);
        
        when(userService.loginUser("john_doe", "password123"))
                .thenReturn(Optional.of(expectedUser));
        
        // 执行
        User result = userController.loginUser("john_doe", "password123");
        
        // 验证
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        verify(userService, times(1)).loginUser("john_doe", "password123");
    }

    @Test
    void loginUser_WithInvalidCredentials_ShouldThrowException() {
        // 准备
        when(userService.loginUser("john_doe", "wrongpassword"))
                .thenReturn(Optional.empty());
        
        // 执行和验证
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userController.loginUser("john_doe", "wrongpassword")
        );
        
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService, times(1)).loginUser("john_doe", "wrongpassword");
    }

    @Test
    void getUserById_WithExistingUser_ShouldReturnUser() {
        // 准备
        User expectedUser = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        expectedUser.setId(1);
        
        when(userService.getUserById(1)).thenReturn(Optional.of(expectedUser));
        
        // 执行
        User result = userController.getUserById(1);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("john_doe", result.getUsername());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void updateUser_WithValidUser_ShouldReturnTrue() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        user.setId(1);
        
        when(userService.updateUser(user)).thenReturn(true);
        
        // 执行
        boolean result = userController.updateUser(user);
        
        // 验证
        assertTrue(result);
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    void deleteUser_WithExistingUser_ShouldReturnTrue() {
        // 准备
        when(userService.deleteUser(1)).thenReturn(true);
        
        // 执行
        boolean result = userController.deleteUser(1);
        
        // 验证
        assertTrue(result);
        verify(userService, times(1)).deleteUser(1);
    }
}
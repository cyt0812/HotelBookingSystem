package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
import com.hotelbooking.entity.User;
import com.hotelbooking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void getUserById_WithExistingUser_ShouldReturnUser() {
        User expected = new User("john_doe", "john@example.com", "password123");
        expected.setId(1);
        when(userService.getUserById(1)).thenReturn(Optional.of(expected));

        ApiResponse<Object> result = userController.getUserById(1);

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        verify(userService).getUserById(1);
    }

    @Test
    void getUserById_WithNonExistingUser_ShouldReturnFailure() {
        when(userService.getUserById(999)).thenReturn(Optional.empty());

        ApiResponse<Object> result = userController.getUserById(999);

        assertFalse(result.isSuccess());
        verify(userService).getUserById(999);
    }

    @Test
    void getUserByUsername_WithExistingUser_ShouldReturnUser() {
        User expected = new User("john_doe", "john@example.com", "password123");
        when(userService.getUserByUsername("john_doe")).thenReturn(Optional.of(expected));

        ApiResponse<Object> result = userController.getUserByUsername("john_doe");

        assertTrue(result.isSuccess());
        assertEquals(expected, result.getData());
    }

    @Test
    void getUserByUsername_WithNonExistingUser_ShouldReturnFailure() {
        when(userService.getUserByUsername("nobody")).thenReturn(Optional.empty());

        assertFalse(userController.getUserByUsername("nobody").isSuccess());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> users = List.of(
                new User("john_doe", "john@example.com", "password123"),
                new User("jane_smith", "jane@example.com", "password456"));
        when(userService.getAllUsers()).thenReturn(users);

        ApiResponse<Object> result = userController.getAllUsers();

        assertTrue(result.isSuccess());
        assertEquals(2, ((List<?>) result.getData()).size());
    }

    @Test
    void getAllUsers_WhenServiceThrows_ShouldReturnFailure() {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("db down"));

        assertFalse(userController.getAllUsers().isSuccess());
    }

    @Test
    void updateUser_WithValidUser_ShouldReturnTrue() {
        User user = new User("john_doe", "john@example.com", "password123");
        when(userService.updateUser(user)).thenReturn(true);

        ApiResponse<Object> result = userController.updateUser(user);

        assertTrue(result.isSuccess());
        assertTrue((Boolean) result.getData());
    }

    @Test
    void updateUser_WithInvalidUser_ShouldReturnFalse() {
        User user = new User("john_doe", "john@example.com", "password123");
        when(userService.updateUser(user)).thenReturn(false);

        ApiResponse<Object> result = userController.updateUser(user);

        assertTrue(result.isSuccess());
        assertFalse((Boolean) result.getData());
    }

    @Test
    void deleteUser_WithExistingUser_ShouldReturnTrue() {
        when(userService.deleteUser(1)).thenReturn(true);

        ApiResponse<Object> result = userController.deleteUser(1);

        assertTrue(result.isSuccess());
        assertTrue((Boolean) result.getData());
    }

    @Test
    void deleteUser_WithNonExistingUser_ShouldReturnFalse() {
        when(userService.deleteUser(999)).thenReturn(false);

        ApiResponse<Object> result = userController.deleteUser(999);

        assertTrue(result.isSuccess());
        assertFalse((Boolean) result.getData());
    }

    @Test
    void validateCredentials_WithValidCredentials_ShouldReturnTrue() {
        User user = new User("john_doe", "john@example.com", "password123");
        when(userService.getUserByUsername("john_doe")).thenReturn(Optional.of(user));

        ApiResponse<Object> result = userController.validateCredentials("john_doe", "password123");

        assertTrue(result.isSuccess());
        assertTrue((Boolean) result.getData());
    }

    @Test
    void validateCredentials_WithInvalidPassword_ShouldReturnFalse() {
        User user = new User("john_doe", "john@example.com", "password123");
        when(userService.getUserByUsername("john_doe")).thenReturn(Optional.of(user));

        ApiResponse<Object> result = userController.validateCredentials("john_doe", "wrong-password");

        assertTrue(result.isSuccess());
        assertFalse((Boolean) result.getData());
    }

    @Test
    void validateCredentials_WithNonExistingUser_ShouldReturnFalse() {
        when(userService.getUserByUsername("nobody")).thenReturn(Optional.empty());

        ApiResponse<Object> result = userController.validateCredentials("nobody", "password123");

        assertFalse((Boolean) result.getData());
    }

    @Test
    void isUsernameExists_WithExistingUsername_ShouldReturnTrue() {
        when(userService.getUserByUsername("john_doe"))
                .thenReturn(Optional.of(new User("john_doe", "john@example.com", "password123")));

        ApiResponse<Object> result = userController.isUsernameExists("john_doe");

        assertTrue((Boolean) result.getData());
    }

    @Test
    void isUsernameExists_WithNonExistingUsername_ShouldReturnFalse() {
        when(userService.getUserByUsername("newuser")).thenReturn(Optional.empty());

        ApiResponse<Object> result = userController.isUsernameExists("newuser");

        assertFalse((Boolean) result.getData());
    }

    @Test
    void isEmailExists_WithExistingEmail_ShouldReturnTrue() {
        when(userService.getAllUsers()).thenReturn(List.of(
                new User("john_doe", "john@example.com", "password123")));

        ApiResponse<Object> result = userController.isEmailExists("john@example.com");

        assertTrue((Boolean) result.getData());
    }

    @Test
    void isEmailExists_WithNonExistingEmail_ShouldReturnFalse() {
        when(userService.getAllUsers()).thenReturn(List.of(
                new User("john_doe", "john@example.com", "password123")));

        ApiResponse<Object> result = userController.isEmailExists("other@example.com");

        assertFalse((Boolean) result.getData());
    }
}

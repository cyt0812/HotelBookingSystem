package com.hotelbooking.service;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
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

        when(userDAO.isUsernameExists("john_doe")).thenReturn(false);
        when(userDAO.isEmailExists("john@example.com")).thenReturn(false);
        when(userDAO.createUser(any(User.class))).thenReturn(expectedUser);

        // 执行
        User result = userService.registerUser("john_doe", "john@example.com", "password123", "CUSTOMER");

        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("john_doe", result.getUsername());
        verify(userDAO, times(1)).createUser(any(User.class));
        verify(userDAO, times(1)).isUsernameExists("john_doe");
        verify(userDAO, times(1)).isEmailExists("john@example.com");
    }

    @Test
    void registerUser_WithEmptyUsername_ShouldThrowValidationException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.registerUser("", "test@test.com", "password", "CUSTOMER")
        );
        
        assertEquals("用户名不能为空", exception.getMessage());
        verify(userDAO, never()).createUser(any(User.class));
    }

    @Test
    void registerUser_WithInvalidEmail_ShouldThrowValidationException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.registerUser("testuser", "invalid-email", "password", "CUSTOMER")
        );
        
        assertEquals("邮箱格式无效", exception.getMessage());
        verify(userDAO, never()).createUser(any(User.class));
    }

    @Test
    void registerUser_WithEmptyPassword_ShouldThrowValidationException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.registerUser("testuser", "test@test.com", "", "CUSTOMER")
        );
        
        assertEquals("密码不能为空", exception.getMessage());
        verify(userDAO, never()).createUser(any(User.class));
    }

    @Test
    void registerUser_WithExistingUsername_ShouldThrowBusinessException() {
        // 准备
        when(userDAO.isUsernameExists("existing_user")).thenReturn(true);

        // 执行和验证
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> userService.registerUser("existing_user", "test@test.com", "password", "CUSTOMER")
        );
        
        assertEquals(ErrorType.USERNAME_EXISTS, exception.getErrorType());
        verify(userDAO, times(1)).isUsernameExists("existing_user");
        verify(userDAO, never()).createUser(any(User.class));
    }

    @Test
    void registerUser_WithExistingEmail_ShouldThrowBusinessException() {
        // 准备
        when(userDAO.isUsernameExists("new_user")).thenReturn(false);
        when(userDAO.isEmailExists("existing@test.com")).thenReturn(true);

        // 执行和验证
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> userService.registerUser("new_user", "existing@test.com", "password", "CUSTOMER")
        );
        
        assertEquals(ErrorType.EMAIL_EXISTS, exception.getErrorType());
        verify(userDAO, times(1)).isUsernameExists("new_user");
        verify(userDAO, times(1)).isEmailExists("existing@test.com");
        verify(userDAO, never()).createUser(any(User.class));
    }

    @Test
    void registerUser_WithDefaultRole_ShouldUseCustomerRole() {
        // 准备
        User expectedUser = new User("testuser", "test@test.com", "password", "CUSTOMER");
        expectedUser.setId(1);

        when(userDAO.isUsernameExists("testuser")).thenReturn(false);
        when(userDAO.isEmailExists("test@test.com")).thenReturn(false);
        when(userDAO.createUser(any(User.class))).thenReturn(expectedUser);

        // 执行
        User result = userService.registerUser("testuser", "test@test.com", "password", null);

        // 验证
        assertNotNull(result);
        assertEquals("CUSTOMER", result.getRole());
        verify(userDAO, times(1)).createUser(any(User.class));
    }

    @Test
    void loginUser_WithValidCredentials_ShouldReturnUser() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        user.setId(1);
        
        when(userDAO.getUserByUsername("john_doe")).thenReturn(Optional.of(user));
        
        // 执行
        User result = userService.loginUser("john_doe", "password123");
        
        // 验证
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        verify(userDAO, times(1)).getUserByUsername("john_doe");
    }

    @Test
    void loginUser_WithInvalidPassword_ShouldThrowBusinessException() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        when(userDAO.getUserByUsername("john_doe")).thenReturn(Optional.of(user));
        
        // 执行和验证
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> userService.loginUser("john_doe", "wrongpassword")
        );
        
        assertEquals(ErrorType.INVALID_CREDENTIALS, exception.getErrorType());
        verify(userDAO, times(1)).getUserByUsername("john_doe");
    }

    @Test
    void loginUser_WithNonExistingUser_ShouldThrowBusinessException() {
        // 准备
        when(userDAO.getUserByUsername("nonexistent")).thenReturn(Optional.empty());
        
        // 执行和验证
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> userService.loginUser("nonexistent", "password")
        );
        
        assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrorType());
        verify(userDAO, times(1)).getUserByUsername("nonexistent");
    }

    @Test
    void loginUser_WithNullCredentials_ShouldThrowValidationException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.loginUser(null, "password")
        );
        
        assertEquals("用户名和密码不能为空", exception.getMessage());
        verify(userDAO, never()).getUserByUsername(anyString());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // 准备
        User user1 = new User("user1", "user1@test.com", "pass", "CUSTOMER");
        User user2 = new User("user2", "user2@test.com", "pass", "ADMIN");
        List<User> expectedUsers = Arrays.asList(user1, user2);
        
        when(userDAO.getAllUsers()).thenReturn(expectedUsers);
        
        // 执行
        List<User> result = userService.getAllUsers();
        
        // 验证
        assertEquals(2, result.size());
        verify(userDAO, times(1)).getAllUsers();
    }

    @Test
    void getUserById_WithExistingId_ShouldReturnUser() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        user.setId(1);
        
        when(userDAO.getUserById(1)).thenReturn(Optional.of(user));
        
        // 执行
        Optional<User> result = userService.getUserById(1);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("john_doe", result.get().getUsername());
        verify(userDAO, times(1)).getUserById(1);
    }

    @Test
    void getUserByUsername_WithExistingUser_ShouldReturnUser() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        user.setId(1);
        
        when(userDAO.getUserByUsername("john_doe")).thenReturn(Optional.of(user));
        
        // 执行
        Optional<User> result = userService.getUserByUsername("john_doe");
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals("john_doe", result.get().getUsername());
        verify(userDAO, times(1)).getUserByUsername("john_doe");
    }

    @Test
    void updateUser_WithValidUser_ShouldReturnTrue() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        user.setId(1);
        
        when(userDAO.updateUser(user)).thenReturn(true);
        
        // 执行
        boolean result = userService.updateUser(user);
        
        // 验证
        assertTrue(result);
        verify(userDAO, times(1)).updateUser(user);
    }

    @Test
    void updateUser_WithNullUser_ShouldReturnFalse() {
        // 执行
        boolean result = userService.updateUser(null);
        
        // 验证
        assertFalse(result);
        verify(userDAO, never()).updateUser(any(User.class));
    }

    @Test
    void updateUser_WithUserWithoutId_ShouldReturnFalse() {
        // 准备
        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
        // 不设置ID
        
        // 执行
        boolean result = userService.updateUser(user);
        
        // 验证
        assertFalse(result);
        verify(userDAO, never()).updateUser(any(User.class));
    }

    @Test
    void deleteUser_WithExistingUser_ShouldReturnTrue() {
        // 准备
        when(userDAO.deleteUser(1)).thenReturn(true);
        
        // 执行
        boolean result = userService.deleteUser(1);
        
        // 验证
        assertTrue(result);
        verify(userDAO, times(1)).deleteUser(1);
    }

    @Test
    void deleteUser_WithNonExistingUser_ShouldReturnFalse() {
        // 准备
        when(userDAO.deleteUser(999)).thenReturn(false);
        
        // 执行
        boolean result = userService.deleteUser(999);
        
        // 验证
        assertFalse(result);
        verify(userDAO, times(1)).deleteUser(999);
    }
}
//package com.hotelbooking.controller;
//
//import com.hotelbooking.dto.ApiResponse;
//import com.hotelbooking.entity.User;
//import com.hotelbooking.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        userController = new UserController(userService);
//    }
//
//    @Test
//    void registerUser_WithValidData_ShouldReturnUser() {
//        // 准备
//        User expectedUser = new User("john_doe", "john@example.com", "password123");
//        expectedUser.setId(1);
//        
//        when(userService.loginUser("john_doe", "password123"))
//                .thenReturn(Optional.of(expectedUser));
//        
//        // 执行
//        ApiResponse<Object> result = userController.registerUser("john_doe", "john@example.com", "password123","CUSTOMER");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("用户注册成功", result.getMessage());
//        User actualUser = (User) result.getData();
//        assertNotNull(actualUser);
//        assertEquals(1, actualUser.getId());
//        assertEquals("john_doe", actualUser.getUsername());
//        verify(userService, times(1)).registerUser("john_doe", "john@example.com", "password123","CUSTOMER");
//    }
//
//    @Test
//    void loginUser_WithValidCredentials_ShouldReturnUser() {
//        // 准备
//        User expectedUser = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        expectedUser.setId(1);
//        
//        when(userService.registerUser("john_doe", "john@example.com", "password123", "CUSTOMER"))
//                .thenReturn(Optional.of(expectedUser));
//        
//        // 执行
//        ApiResponse<Object> result = userController.loginUser("john_doe", "password123");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("用户登录成功", result.getMessage());
//        User actualUser = (User) result.getData();
//        assertNotNull(actualUser);
//        assertEquals("john_doe", actualUser.getUsername());
//        verify(userService, times(1)).loginUser("john_doe", "password123");
//    }
//
//    @Test
//    void getUserById_WithExistingUser_ShouldReturnUser() {
//        // 准备
//        User expectedUser = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        expectedUser.setId(1);
//        
//        when(userService.getUserById(1)).thenReturn(Optional.of(expectedUser));
//        
//        // 执行
//        ApiResponse<Object> result = userController.getUserById(1);
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("获取用户信息成功", result.getMessage());
//        User actualUser = (User) result.getData();
//        assertNotNull(actualUser);
//        assertEquals(1, actualUser.getId());
//        assertEquals("john_doe", actualUser.getUsername());
//        verify(userService, times(1)).getUserById(1);
//    }
//
//    @Test
//    void getUserById_WithNonExistingUser_ShouldReturnFailure() {
//        // 准备
//        when(userService.getUserById(999)).thenReturn(Optional.empty());
//        
//        // 执行
//        ApiResponse<Object> result = userController.getUserById(999);
//        
//        // 验证
//        assertFalse(result.isSuccess());
//        assertEquals("用户不存在，ID: 999", result.getMessage());
//        verify(userService, times(1)).getUserById(999);
//    }
//
//    @Test
//    void getUserByUsername_WithExistingUser_ShouldReturnUser() {
//        // 准备
//        User expectedUser = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        expectedUser.setId(1);
//        
//        when(userService.getUserByUsername("john_doe")).thenReturn(Optional.of(expectedUser));
//        
//        // 执行
//        ApiResponse<Object> result = userController.getUserByUsername("john_doe");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("获取用户信息成功", result.getMessage());
//        User actualUser = (User) result.getData();
//        assertNotNull(actualUser);
//        assertEquals("john_doe", actualUser.getUsername());
//        verify(userService, times(1)).getUserByUsername("john_doe");
//    }
//
//    @Test
//    void getAllUsers_ShouldReturnAllUsers() {
//        // 准备
//        User user1 = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        User user2 = new User("jane_smith", "jane@example.com", "password456", "ADMIN");
//        List<User> expectedUsers = Arrays.asList(user1, user2);
//        
//        when(userService.getAllUsers()).thenReturn(expectedUsers);
//        
//        // 执行
//        ApiResponse<Object> result = userController.getAllUsers();
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("获取用户列表成功", result.getMessage());
//        List<User> actualUsers = (List<User>) result.getData();
//        assertEquals(2, actualUsers.size());
//        verify(userService, times(1)).getAllUsers();
//    }
//
//    @Test
//    void updateUser_WithValidUser_ShouldReturnTrue() {
//        // 准备
//        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        user.setId(1);
//        
//        when(userService.updateUser(user)).thenReturn(true);
//        
//        // 执行
//        ApiResponse<Object> result = userController.updateUser(user);
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("用户信息更新成功", result.getMessage());
//        assertTrue((Boolean) result.getData());
//        verify(userService, times(1)).updateUser(user);
//    }
//
//    @Test
//    void updateUser_WithInvalidUser_ShouldReturnFalse() {
//        // 准备
//        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        user.setId(999);
//        
//        when(userService.updateUser(user)).thenReturn(false);
//        
//        // 执行
//        ApiResponse<Object> result = userController.updateUser(user);
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("用户信息更新失败", result.getMessage());
//        assertFalse((Boolean) result.getData());
//        verify(userService, times(1)).updateUser(user);
//    }
//
//    @Test
//    void deleteUser_WithExistingUser_ShouldReturnTrue() {
//        // 准备
//        when(userService.deleteUser(1)).thenReturn(true);
//        
//        // 执行
//        ApiResponse<Object> result = userController.deleteUser(1);
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("用户删除成功", result.getMessage());
//        assertTrue((Boolean) result.getData());
//        verify(userService, times(1)).deleteUser(1);
//    }
//
//    @Test
//    void deleteUser_WithNonExistingUser_ShouldReturnFalse() {
//        // 准备
//        when(userService.deleteUser(999)).thenReturn(false);
//        
//        // 执行
//        ApiResponse<Object> result = userController.deleteUser(999);
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("用户删除失败", result.getMessage());
//        assertFalse((Boolean) result.getData());
//        verify(userService, times(1)).deleteUser(999);
//    }
//
//    @Test
//    void validateCredentials_WithValidCredentials_ShouldReturnTrue() {
//        // 准备
//        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        
//        when(userService.getUserByUsername("john_doe")).thenReturn(Optional.of(user));
//        
//        // 执行
//        ApiResponse<Object> result = userController.validateCredentials("john_doe", "password123");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("凭据验证成功", result.getMessage());
//        assertTrue((Boolean) result.getData());
//        verify(userService, times(1)).getUserByUsername("john_doe");
//    }
//
//    @Test
//    void validateCredentials_WithInvalidPassword_ShouldReturnFalse() {
//        // 准备
//        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        
//        when(userService.getUserByUsername("john_doe")).thenReturn(Optional.of(user));
//        
//        // 执行
//        ApiResponse<Object> result = userController.validateCredentials("john_doe", "wrongpassword");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("凭据验证失败", result.getMessage());
//        assertFalse((Boolean) result.getData());
//        verify(userService, times(1)).getUserByUsername("john_doe");
//    }
//
//    @Test
//    void validateCredentials_WithNonExistingUser_ShouldReturnFalse() {
//        // 准备
//        when(userService.getUserByUsername("nonexistent")).thenReturn(Optional.empty());
//        
//        // 执行
//        ApiResponse<Object> result = userController.validateCredentials("nonexistent", "password");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("凭据验证失败", result.getMessage());
//        assertFalse((Boolean) result.getData());
//        verify(userService, times(1)).getUserByUsername("nonexistent");
//    }
//
//    @Test
//    void isUsernameExists_WithExistingUsername_ShouldReturnTrue() {
//        // 准备
//        User user = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        
//        when(userService.getUserByUsername("john_doe")).thenReturn(Optional.of(user));
//        
//        // 执行
//        ApiResponse<Object> result = userController.isUsernameExists("john_doe");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("用户名已存在", result.getMessage());
//        assertTrue((Boolean) result.getData());
//        verify(userService, times(1)).getUserByUsername("john_doe");
//    }
//
//    @Test
//    void isUsernameExists_WithNonExistingUsername_ShouldReturnFalse() {
//        // 准备
//        when(userService.getUserByUsername("newuser")).thenReturn(Optional.empty());
//        
//        // 执行
//        ApiResponse<Object> result = userController.isUsernameExists("newuser");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("用户名可用", result.getMessage());
//        assertFalse((Boolean) result.getData());
//        verify(userService, times(1)).getUserByUsername("newuser");
//    }
//
//    @Test
//    void isEmailExists_WithExistingEmail_ShouldReturnTrue() {
//        // 准备
//        User user1 = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        User user2 = new User("jane_smith", "jane@example.com", "password456", "ADMIN");
//        List<User> allUsers = Arrays.asList(user1, user2);
//        
//        when(userService.getAllUsers()).thenReturn(allUsers);
//        
//        // 执行
//        ApiResponse<Object> result = userController.isEmailExists("john@example.com");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("邮箱已存在", result.getMessage());
//        assertTrue((Boolean) result.getData());
//        verify(userService, times(1)).getAllUsers();
//    }
//
//    @Test
//    void isEmailExists_WithNonExistingEmail_ShouldReturnFalse() {
//        // 准备
//        User user1 = new User("john_doe", "john@example.com", "password123", "CUSTOMER");
//        User user2 = new User("jane_smith", "jane@example.com", "password456", "ADMIN");
//        List<User> allUsers = Arrays.asList(user1, user2);
//        
//        when(userService.getAllUsers()).thenReturn(allUsers);
//        
//        // 执行
//        ApiResponse<Object> result = userController.isEmailExists("newuser@example.com");
//        
//        // 验证
//        assertTrue(result.isSuccess());
//        assertEquals("邮箱可用", result.getMessage());
//        assertFalse((Boolean) result.getData());
//        verify(userService, times(1)).getAllUsers();
//    }
//}
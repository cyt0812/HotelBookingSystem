//package com.hotelbooking.integration;
//
//import com.hotelbooking.controller.UserController;
//import com.hotelbooking.dao.UserDAO;
//import com.hotelbooking.dto.ApiResponse;
//import com.hotelbooking.entity.User;
//import com.hotelbooking.service.UserService;
//import com.hotelbooking.util.DatabaseInitializer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserRegistrationIntegrationTest {
//
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        DatabaseInitializer.resetDatabase(); // 重置数据库
//        DatabaseInitializer.initializeDatabase(); // 重新初始化
//        
//        UserDAO userDAO = new UserDAO();
//        UserService userService = new UserService(userDAO);
//        userController = new UserController(userService);
//    }
//
//    @Test
//    void userRegistration_ShouldWorkEndToEnd() {
//        // 执行注册
//        ApiResponse<Object> registerResponse = userController.registerUser(
//            "integration_user", "integration@test.com", "password123", "CUSTOMER");
//        
//        // 验证注册响应
//        assertTrue(registerResponse.isSuccess(), "注册应该成功");
//        assertEquals("用户注册成功", registerResponse.getMessage());
//        
//        User user = (User) registerResponse.getData();
//        assertNotNull(user, "返回的用户对象不应为null");
//        assertNotNull(user.getId(), "用户ID不应为null");
//        assertEquals("integration_user", user.getUsername());
//        assertEquals("integration@test.com", user.getEmail());
//        
//        // 验证可以登录
//        ApiResponse<Object> loginResponse = userController.loginUser("integration_user", "password123");
//        
//        // 验证登录响应
//        assertTrue(loginResponse.isSuccess(), "登录应该成功");
//        assertEquals("用户登录成功", loginResponse.getMessage());
//        
//        User loggedInUser = (User) loginResponse.getData();
//        assertNotNull(loggedInUser, "登录返回的用户对象不应为null");
//        assertEquals(user.getId(), loggedInUser.getId());
//    }
//
//    @Test
//    void userLogin_WithInvalidCredentials_ShouldFail() {
//        // 准备 - 先注册用户
//        ApiResponse<Object> registerResponse = userController.registerUser(
//            "testuser", "test@test.com", "correctpassword", "CUSTOMER");
//        assertTrue(registerResponse.isSuccess(), "注册应该成功");
//        
//        // 执行错误密码登录
//        ApiResponse<Object> loginResponse = userController.loginUser("testuser", "wrongpassword");
//        
//        // 验证登录失败
//        assertFalse(loginResponse.isSuccess(), "使用错误密码登录应该失败");
//        assertTrue(loginResponse.getMessage().contains("失败") || 
//                  loginResponse.getMessage().contains("错误"), 
//                  "错误消息应该表明登录失败");
//    }
//
//    @Test
//    void userRegistration_WithDuplicateUsername_ShouldFail() {
//        // 第一次注册
//        ApiResponse<Object> firstResponse = userController.registerUser(
//            "duplicate_user", "first@test.com", "password123", "CUSTOMER");
//        assertTrue(firstResponse.isSuccess(), "第一次注册应该成功");
//        
//        // 第二次注册相同用户名
//        ApiResponse<Object> secondResponse = userController.registerUser(
//            "duplicate_user", "second@test.com", "password456", "CUSTOMER");
//        
//        // 验证第二次注册失败
//        assertFalse(secondResponse.isSuccess(), "重复用户名注册应该失败");
//        assertTrue(secondResponse.getMessage().contains("存在") || 
//                  secondResponse.getMessage().contains("重复"),
//                  "错误消息应该表明用户名已存在");
//    }
//
//    @Test
//    void userRegistration_WithDuplicateEmail_ShouldFail() {
//        // 第一次注册
//        ApiResponse<Object> firstResponse = userController.registerUser(
//            "user1", "duplicate@test.com", "password123", "CUSTOMER");
//        assertTrue(firstResponse.isSuccess(), "第一次注册应该成功");
//        
//        // 第二次注册相同邮箱
//        ApiResponse<Object> secondResponse = userController.registerUser(
//            "user2", "duplicate@test.com", "password456", "CUSTOMER");
//        
//        // 验证第二次注册失败
//        assertFalse(secondResponse.isSuccess(), "重复邮箱注册应该失败");
//        assertTrue(secondResponse.getMessage().contains("存在") || 
//                  secondResponse.getMessage().contains("重复"),
//                  "错误消息应该表明邮箱已存在");
//    }
//
//    @Test
//    void getUserById_ShouldReturnCorrectUser() {
//        // 先注册用户
//        ApiResponse<Object> registerResponse = userController.registerUser(
//            "getbyid_user", "getbyid@test.com", "password123", "CUSTOMER");
//        assertTrue(registerResponse.isSuccess());
//        
//        User registeredUser = (User) registerResponse.getData();
//        Integer userId = registeredUser.getId();
//        
//        // 根据ID获取用户
//        ApiResponse<Object> getResponse = userController.getUserById(userId);
//        
//        // 验证
//        assertTrue(getResponse.isSuccess(), "根据ID获取用户应该成功");
//        User retrievedUser = (User) getResponse.getData();
//        assertNotNull(retrievedUser, "获取的用户不应为null");
//        assertEquals(userId, retrievedUser.getId());
//        assertEquals("getbyid_user", retrievedUser.getUsername());
//    }
//
//    @Test
//    void validateCredentials_WithCorrectPassword_ShouldReturnTrue() {
//        // 先注册用户
//        ApiResponse<Object> registerResponse = userController.registerUser(
//            "validate_user", "validate@test.com", "correctpass", "CUSTOMER");
//        assertTrue(registerResponse.isSuccess());
//        
//        // 验证正确凭据
//        ApiResponse<Object> validateResponse = userController.validateCredentials("validate_user", "correctpass");
//        
//        // 验证
//        assertTrue(validateResponse.isSuccess());
//        assertTrue((Boolean) validateResponse.getData(), "正确密码验证应该返回true");
//    }
//
//    @Test
//    void validateCredentials_WithWrongPassword_ShouldReturnFalse() {
//        // 先注册用户
//        ApiResponse<Object> registerResponse = userController.registerUser(
//            "validate_user2", "validate2@test.com", "correctpass", "CUSTOMER");
//        assertTrue(registerResponse.isSuccess());
//        
//        // 验证错误凭据
//        ApiResponse<Object> validateResponse = userController.validateCredentials("validate_user2", "wrongpass");
//        
//        // 验证
//        assertTrue(validateResponse.isSuccess());
//        assertFalse((Boolean) validateResponse.getData(), "错误密码验证应该返回false");
//    }
//}
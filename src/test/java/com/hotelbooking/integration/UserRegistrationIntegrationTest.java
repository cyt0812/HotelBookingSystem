package com.hotelbooking.integration;

import com.hotelbooking.controller.UserController;
import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import com.hotelbooking.service.UserService;
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationIntegrationTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        DatabaseInitializer.resetDatabase(); // 重置数据库
        DatabaseInitializer.initializeDatabase(); // 重新初始化
        
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);
        userController = new UserController(userService);
    }

    @Test
    void userRegistration_ShouldWorkEndToEnd() {
        // 执行
        User user = userController.registerUser("integration_user", "integration@test.com", "password123", "CUSTOMER");
        
        // 验证
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("integration_user", user.getUsername());
        assertEquals("integration@test.com", user.getEmail());
        
        // 验证可以登录
        User loggedInUser = userController.loginUser("integration_user", "password123");
        assertNotNull(loggedInUser);
        assertEquals(user.getId(), loggedInUser.getId());
    }

    @Test
    void userLogin_WithInvalidCredentials_ShouldFail() {
        // 准备 - 先注册用户
        userController.registerUser("testuser", "test@test.com", "correctpassword", "CUSTOMER");
        
        // 执行和验证
        Exception exception = assertThrows(RuntimeException.class, 
            () -> userController.loginUser("testuser", "wrongpassword"));
        
        assertEquals("Invalid username or password", exception.getMessage());
    }
}
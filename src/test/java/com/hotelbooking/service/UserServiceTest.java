package com.hotelbooking.service;

import com.hotelbooking.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserService userService = new UserService();

    // 测试1：注册新用户（使用四参数构造）

   @Test
void registerUser_shouldReturnTrueWhenUsernameNotExists() {
     String randomUsername = "testuser_" + System.currentTimeMillis();
    User user = new User();
    user.setUsername(randomUsername); 
    user.setPassword("123456");
    user.setEmail("test@example.com");
    user.setFullName("Test User");

    boolean result = userService.registerUser(user);
    assertTrue(result, "新用户注册应返回true");
}
    // 测试2：注册重复用户名（使用四参数构造）
    @Test
    void registerUser_shouldReturnFalseWhenUsernameExists() {
        // 1. 先注册一个用户
        User user = new User(
            "testDuplicate", 
            "pass456", 
            "dup@test.com", 
            "Dup User"
        );
        userService.registerUser(user);
        
        // 2. 用相同用户名再次注册
        User duplicateUser = new User(
            "testDuplicate",  // 重复的用户名
            "pass789", 
            "dup2@test.com", 
            "Dup User2"
        );
        boolean result = userService.registerUser(duplicateUser);
        assertFalse(result, "重复用户名注册应返回false");
    }

    // 测试3：登录成功（先注册用户时用四参数构造）
    @Test
    void loginUser_shouldReturnUserWhenCredentialsCorrect() {
        // 1. 先注册一个用户
        User user = new User(
            "testLogin", 
            "correctPass", 
            "login@test.com", 
            "Login User"
        );
        userService.registerUser(user);
        
        // 2. 使用正确的用户名和密码登录
        User loggedInUser = userService.loginUser("testLogin", "correctPass");
        assertNotNull(loggedInUser, "正确凭据登录应返回用户对象");
        assertEquals("testLogin", loggedInUser.getUsername());
    }

    // 测试4：登录失败（用户名不存在）
    @Test
    void loginUser_shouldReturnNullWhenUserNotExists() {
        User loggedInUser = userService.loginUser("nonExistentUser", "anyPass");
        assertNull(loggedInUser, "用户名不存在时应返回null");
    }

    // 测试5：登录失败（密码错误）
    @Test
    void loginUser_shouldReturnNullWhenPasswordWrong() {
        // 1. 先注册一个用户
        User user = new User(
            "testWrongPass", 
            "rightPass", 
            "wrong@test.com", 
            "Wrong Pass User"
        );
        userService.registerUser(user);
        
        // 2. 用正确用户名+错误密码登录
        User loggedInUser = userService.loginUser("testWrongPass", "wrongPass");
        assertNull(loggedInUser, "密码错误时应返回null");
    }
}
package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.service.UserService;

import java.util.List;
import java.util.Optional;

public class UserController {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 用户注册
     */
    public User registerUser(String username, String email, String password, String role) {
        return userService.registerUser(username, email, password, role);
    }
    
    /**
     * 用户登录
     */
    public User loginUser(String username, String password) {
        Optional<User> user = userService.loginUser(username, password);
        return user.orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }
    
    /**
     * 根据ID获取用户
     */
    public User getUserById(Integer userId) {
        return userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    /**
     * 根据用户名获取用户
     */
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    
    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * 更新用户信息
     */
    public boolean updateUser(User user) {
        return userService.updateUser(user);
    }
    
    /**
     * 删除用户
     */
    public boolean deleteUser(Integer userId) {
        return userService.deleteUser(userId);
    }
    
    /**
     * 验证用户凭据
     */
    public boolean validateCredentials(String username, String password) {
        Optional<User> user = userService.loginUser(username, password);
        return user.isPresent();
    }
    
    /**
     * 检查用户名是否已存在
     */
    public boolean isUsernameExists(String username) {
        return userService.getUserByUsername(username).isPresent();
    }
    
    /**
     * 检查邮箱是否已存在
     */
    public boolean isEmailExists(String email) {
        // 这里需要扩展 UserService 来支持按邮箱查询
        List<User> allUsers = userService.getAllUsers();
        return allUsers.stream().anyMatch(user -> email.equals(user.getEmail()));
    }
}
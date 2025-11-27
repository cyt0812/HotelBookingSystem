package com.hotelbooking.service;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;
    
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    /**
     * 注册新用户
     */
    public User registerUser(String username, String email, String password, String role) {
        // 验证输入
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (role == null || role.trim().isEmpty()) {
            role = "CUSTOMER"; // 默认角色
        }
        
        // 创建用户
        User user = new User(username.trim(), email.trim(), password, role);
        return userDAO.createUser(user);
    }
    
    /**
     * 用户登录
     */
    public Optional<User> loginUser(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        
        Optional<User> user = userDAO.getUserByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
    
    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * 根据ID获取用户
     */
    public Optional<User> getUserById(Integer id) {
        return userDAO.getUserById(id);
    }
    
    /**
     * 根据用户名获取用户
     */
    public Optional<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
    
    /**
     * 删除用户
     */
    public boolean deleteUser(Integer userId) {
        return userDAO.deleteUser(userId);
    }
    
    /**
     * 更新用户信息
     */
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        return userDAO.updateUser(user);
    }
}
package com.hotelbooking.service;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
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
        try {
            // 验证输入
            if (username == null || username.trim().isEmpty()) {
                throw new ValidationException("用户名不能为空");
            }
            if (email == null || !email.contains("@")) {
                throw new ValidationException("邮箱格式无效");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new ValidationException("密码不能为空");
            }
            if (role == null || role.trim().isEmpty()) {
                role = "CUSTOMER"; // 默认角色
            }
            
            // 检查用户名是否已存在
            if (userDAO.isUsernameExists(username)) {
                throw new BusinessException(ErrorType.USERNAME_EXISTS);
            }
            
            // 检查邮箱是否已存在
            if (userDAO.isEmailExists(email)) {
                throw new BusinessException(ErrorType.EMAIL_EXISTS);
            }
            
            // 创建用户
            User user = new User(username.trim(), email.trim(), password, role);
            return userDAO.createUser(user);
        } catch (BusinessException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "用户注册失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 用户登录
     */
    public User loginUser(String username, String password) {
        try {
            if (username == null || password == null) {
                throw new ValidationException("用户名和密码不能为空");
            }
            
            User user = userDAO.getUserByUsername(username)
                    .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));
            
            if (!user.getPassword().equals(password)) {
                throw new BusinessException(ErrorType.INVALID_CREDENTIALS);
            }
            
            return user;
        } catch (BusinessException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "用户登录失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取用户列表失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据ID获取用户
     */
    public Optional<User> getUserById(Integer id) {
        try {
            return userDAO.getUserById(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取用户信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据用户名获取用户
     */
    public Optional<User> getUserByUsername(String username) {
        try {
            return userDAO.getUserByUsername(username);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取用户信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除用户
     */
    public boolean deleteUser(Integer userId) {
        try {
            return userDAO.deleteUser(userId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "删除用户失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新用户信息
     */
    public boolean updateUser(User user) {
        try {
            if (user == null || user.getId() == null) {
                return false;
            }
            return userDAO.updateUser(user);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "更新用户信息失败: " + e.getMessage(), e);
        }
    }
}
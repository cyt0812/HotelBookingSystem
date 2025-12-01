package com.hotelbooking.util;

import com.hotelbooking.entity.User;

/**
 * 用户会话管理类 - 单例模式
 * 用于存储和管理当前登录用户的信息
 */
public class UserSession {
    private static UserSession instance;
    private User currentUser;
    private Integer currentUserId;
    private String currentUsername;
    
    private UserSession() {
    }
    
    /**
     * 获取 UserSession 单例实例
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    /**
     * 登录 - 设置当前用户信息
     */
    public void login(User user) {
        this.currentUser = user;
        this.currentUserId = user.getId();
        this.currentUsername = user.getUsername();
    }
    
    /**
     * 登录 - 设置用户ID和用户名
     */
    public void login(Integer userId, String username) {
        this.currentUserId = userId;
        this.currentUsername = username;
    }
    
    /**
     * 登出 - 清除当前用户信息
     */
    public void logout() {
        this.currentUser = null;
        this.currentUserId = null;
        this.currentUsername = null;
    }
    
    /**
     * 获取当前用户ID
     */
    public Integer getCurrentUserId() {
        return currentUserId;
    }
    
    /**
     * 获取当前用户名
     */
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    /**
     * 获取当前用户对象
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * 检查用户是否已登录
     */
    public boolean isLoggedIn() {
        return currentUserId != null && currentUsername != null;
    }
    
    /**
     * 设置当前用户对象
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            this.currentUserId = user.getId();
            this.currentUsername = user.getUsername();
        }
    }
    
    /**
     * 更新用户信息
     */
    public void updateUser(User user) {
        if (user != null && currentUserId != null && currentUserId.equals(user.getId())) {
            this.currentUser = user;
        }
    }
    
    /**
     * 清空所有会话数据
     */
    public void clear() {
        this.currentUser = null;
        this.currentUserId = null;
        this.currentUsername = null;
    }
}
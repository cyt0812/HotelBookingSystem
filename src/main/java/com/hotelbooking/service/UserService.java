package com.hotelbooking.service;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;

public class UserService {
    private final UserDAO userDao = new UserDAO();

    public boolean registerUser(User user) {
        // 检查用户名是否已存在
        if (userDao.getUserByUsername(user.getUsername()) != null) {
            return false; // 用户名已存在
        }
        // TODO: 密码加密
        return userDao.createUser(user);
    }

    public User loginUser(String username, String password) {
        User user = userDao.getUserByUsername(username);
        if (user == null) return null;
        
        // TODO: 验证加密密码
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
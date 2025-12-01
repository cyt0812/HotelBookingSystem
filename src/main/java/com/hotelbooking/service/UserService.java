/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.service;

/**
 *
 * @author LENOVO
 */

//无数据库情况下测试
import com.hotelbooking.entity.User;

public class UserService {

    // 临时模拟登录，不接数据库
    public User loginUser(String username, String password) {
        if ("admin".equals(username) && "123".equals(password)) {
            return new User(username, password, "demo@email.com");
        }
        return null;
    }

    // 临时模拟注册
    public boolean registerUser(User user) {
        System.out.println("Registered: " + user.getUsername());
        return true;
    }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.util;

import com.hotelbooking.entity.User;

public class SessionManager {

//    private static String loggedInUser;
//
//    public static void login(String username) {
//        loggedInUser = username;
//    }
//
//    public static String getLoggedInUser() {
//        return loggedInUser;
//    }
//
    
    
//    private static User currentUser;
//
//    public static void setCurrentUser(User user) {
//        currentUser = user;
//    }
//
//    public static User getCurrentUser() {
//        return currentUser;
//    }
//    
//    
//    public static void login(User user) {
//        currentUser = user;
//    }
//
//    public static String getLoggedInUsername() {
//        return currentUser.getUsername();
//    }
//    
//    public static void logout() {
//        currentUser = null;
//    }
    // 静态变量保存当前登录的用户对象
    private static User currentUser = null;
    
    /**
     * 登录用户 - 设置当前用户
     * @param user 登录的用户对象
     */
    public static void login(User user) {
        currentUser = user;
        System.out.println("用户登录成功: " + user.getUsername());
    }
    
    /**
     * 获取当前登录的用户
     * @return 当前用户对象，如果未登录返回null
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * 登出 - 清除当前用户信息
     */
    public static void logout() {
        if (currentUser != null) {
            System.out.println("用户登出: " + currentUser.getUsername());
            currentUser = null;
        }
    }
    
    /**
     * 检查是否有用户登录
     * @return true如果有用户登录，否则false
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * 获取当前登录用户的用户名
     * @return 用户名，如果未登录返回null
     */
    public static String getLoggedInUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

}

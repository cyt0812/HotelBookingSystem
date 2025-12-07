/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.util;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.User;
import java.time.LocalDate;

public class SessionManager {
    private static LocalDate checkInDate;
        private static LocalDate checkOutDate;
        private static int roomCount;
        private static int adultCount;
        private static int childCount;
        

        // ====== 设置方法 ======
        public static void setCheckInDate(LocalDate date) {
            checkInDate = date;
        }

        public static void setCheckOutDate(LocalDate date) {
            checkOutDate = date;
        }

        public static void setRoomCount(int count) {
            roomCount = count;
        }

        public static void setAdultCount(int count) {
            adultCount = count;
        }

        public static void setChildCount(int count) {
            childCount = count;
        }

        // ====== 获取方法 ======
        public static LocalDate getCheckInDate() {
            return checkInDate;
        }

        public static LocalDate getCheckOutDate() {
            return checkOutDate;
        }

        public static int getRoomCount() {
            return roomCount;
        }

        public static int getAdultCount() {
            return adultCount;
        }

        public static int getChildCount() {
            return childCount;
        }

        // ====== 清空状态 ======
        public static void clear() {
            currentHotel = null;
            checkInDate = null;
            checkOutDate = null;
            roomCount = 1;
            adultCount = 1;
            childCount = 0;
        }

    // 静态变量保存当前登录的用户对象
    private static User currentUser = null;
    
    // 当前酒店
    private static Hotel currentHotel = null;

    // ====== 当前酒店设置和获取 ======
    public static void setCurrentHotel(Hotel hotel) {
        currentHotel = hotel;
        System.out.println("当前酒店已设置: " + hotel.getName());
    }

    public static Hotel getCurrentHotel() {
        return currentHotel;
    }

    
    
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
    
<<<<<<< HEAD
    public static void setCurrentUser(User user) {
        currentUser = user;
        System.out.println("用户设置成功");
    }
    
=======
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
    
    public static Integer getLoggedInId() {
        return currentUser != null ? currentUser.getId() : null;
    }

}

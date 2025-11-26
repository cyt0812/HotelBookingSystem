package com.hotelbooking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // 必须去掉final，否则setUrl无法修改
    private static String URL = "jdbc:derby://localhost:1527/HotelDB;create=true";
    private static final String USER = "app";
    private static final String PASSWORD = "app";

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            throw e; // 必须重新抛出异常，测试才能捕获
        }
    }

    // 核心：public static的setUrl方法，用于测试修改URL
    public static void setUrl(String newUrl) {
        URL = newUrl; // 直接赋值修改静态变量
    }

    // 测试连接的main方法
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ 数据库连接成功！");
        } catch (SQLException e) {
            System.err.println("❌ 数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
package com.hotelbooking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // 保留你本地的URL配置
    private static final String URL = "jdbc:derby://localhost:1527/HotelDB";
    private static final String USER = "app";
    private static final String PASSWORD = "app";

    public static Connection getConnection() throws SQLException {
        try {
            // 保留你本地的注释和代码逻辑（新版JDBC自动加载驱动）
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            throw e;
        }
    }

    // 保留你本地的main方法（用于测试数据库连接）
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ 数据库连接成功！");
        } catch (SQLException e) {
            System.err.println("❌ 数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
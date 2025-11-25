package com.hotelbooking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:derby://localhost:1527/HotelDB";
    private static final String USER = "app";
    private static final String PASSWORD = "app";

    public static Connection getConnection() throws SQLException {
        try {
            // 新版JDBC会自动加载驱动，不需要手动Class.forName
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ 数据库连接成功！");
        } catch (SQLException e) {
            System.err.println("❌ 数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
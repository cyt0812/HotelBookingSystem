// src/test/java/com/hotelbooking/test/TestDBConnection.java
package com.hotelbooking.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {
    public static void main(String[] args) {
        String[] testUrls = {
            "jdbc:mysql://localhost:3306/mysql",  // 测试能否连接到 MySQL
            "jdbc:mysql://localhost:3306/",
            "jdbc:mysql://localhost:3306/hotel_booking_db"
        };
        
        String user = "root";
        String password = "123456";  // 修改为你的密码
        
        for (String url : testUrls) {
            System.out.println("\n尝试连接: " + url);
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                System.out.println("✅ 连接成功！");
                System.out.println("数据库: " + conn.getMetaData().getDatabaseProductName());
                conn.close();
            } catch (Exception e) {
                System.out.println("❌ 连接失败: " + e.getMessage());
            }
        }
    }
}
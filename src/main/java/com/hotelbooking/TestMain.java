package com.hotelbooking;

import com.hotelbooking.util.DatabaseInitializer;

public class TestMain {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking System Test ===");
        
        // 测试数据库初始化
        DatabaseInitializer.initialize();
        
        // 测试数据库连接
        try {
            var conn = com.hotelbooking.util.DatabaseConnection.getConnection();
            System.out.println("✅ Database connection successful!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Database connection failed:: " + e.getMessage());
        }
        
        System.out.println("=== Basic environment test completed ===");
        System.out.println("Next step: Create FXML interface files for Role B");
    }
}
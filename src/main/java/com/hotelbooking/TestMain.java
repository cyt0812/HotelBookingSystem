package com.hotelbooking;

import com.hotelbooking.util.DatabaseInitializer;

public class TestMain {
    public static void main(String[] args) {
        System.out.println("=== 酒店预订系统测试 ===");
        
        // 测试数据库初始化
        DatabaseInitializer.initializeDatabase();
        
        // 测试数据库连接
        try {
            var conn = com.hotelbooking.util.DatabaseConnection.getConnection();
            System.out.println("✅ 数据库连接成功！");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ 数据库连接失败: " + e.getMessage());
        }
        
        System.out.println("=== 基础环境测试完成 ===");
        System.out.println("下一步：角色B创建FXML界面文件");
    }
}
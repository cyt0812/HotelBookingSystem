package com.hotelbooking.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库初始化工具类：用于创建HotelDB数据库的表结构（首次运行时执行）
 */
public class DatabaseInitializer {

    // 初始化数据库表结构（核心方法：创建users表）
    public static void initialize() {
        // 1. 定义创建users表的SQL语句（和UserDAO、User实体类的字段匹配）
        String createUsersTable = "CREATE TABLE users (" +
                "user_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +  // 自增主键
                "username VARCHAR(50) UNIQUE NOT NULL, " +                 // 用户名唯一，非空
                "password VARCHAR(50) NOT NULL, " +                        // 密码非空
                "email VARCHAR(100), " +                                   // 邮箱可选
                "full_name VARCHAR(100), " +                               // 全名可选
                "role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER'" +           // 角色默认值CUSTOMER
                ")";

        // 2. 执行SQL创建表
        try (Connection conn = DatabaseConnection.getConnection();  // 获取数据库连接
             Statement stmt = conn.createStatement()) {              // 创建SQL执行对象

            stmt.execute(createUsersTable);  // 执行建表SQL
            System.out.println("✅ [初始化成功] users表创建完成！");

        } catch (SQLException e) {
            // 处理异常：如果表已存在，忽略该错误（仅首次创建需要）
            if (e.getMessage().contains("already exists")) {
                System.out.println("ℹ️ [提示] users表已存在，无需重复创建！");
            } else {
                // 其他SQL错误，打印详细信息
                System.err.println("❌ [初始化失败] 创建users表出错：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // 主方法：运行该类即可执行初始化（手动触发建表）
    public static void main(String[] args) {
        System.out.println("开始初始化数据库表结构...");
        initialize();  // 调用初始化方法创建表
        System.out.println("数据库表结构初始化流程结束！");
    }
}
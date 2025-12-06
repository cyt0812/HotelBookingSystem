// src/test/java/com/hotelbooking/util/SimpleDerbyTest.java
package com.hotelbooking.util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleDerbyTest {
    
    @Test
    void testDerbyDriverLoading() {
        System.out.println("=== 测试 Derby 驱动加载 ===");
        
        try {
            // 1. 尝试加载驱动
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("✅ Derby 驱动类加载成功");
            
            // 2. 检查 DriverManager 是否注册了驱动
            java.util.Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
            boolean foundDerby = false;
            while (drivers.hasMoreElements()) {
                java.sql.Driver driver = drivers.nextElement();
                System.out.println("已注册驱动: " + driver.getClass().getName());
                if (driver.getClass().getName().contains("derby")) {
                    foundDerby = true;
                }
            }
            
            assertTrue(foundDerby, "DriverManager 应该注册了 Derby 驱动");
            System.out.println("✅ Derby 驱动已注册到 DriverManager");
            
        } catch (ClassNotFoundException e) {
            fail("无法加载 Derby 驱动类: " + e.getMessage());
        }
    }
    
    @Test
    void testMemoryDatabase() {
        System.out.println("\n=== 测试内存数据库 ===");
        
        String url = "jdbc:derby:memory:simple_test;create=true";
        
        try {
            // 1. 显示类路径信息
            System.out.println("类路径: " + System.getProperty("java.class.path"));
            
            // 2. 尝试连接
            System.out.println("连接 URL: " + url);
            Connection conn = DriverManager.getConnection(url);
            
            assertNotNull(conn);
            assertFalse(conn.isClosed());
            
            System.out.println("✅ 内存数据库连接成功");
            System.out.println("数据库: " + conn.getMetaData().getDatabaseProductName());
            
            conn.close();
            
        } catch (Exception e) {
            System.err.println("❌ 连接失败: " + e.getMessage());
            e.printStackTrace();
            fail("内存数据库连接失败: " + e.getMessage());
        }
    }
}
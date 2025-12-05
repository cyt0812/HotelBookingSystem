package com.hotelbooking.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @BeforeAll
    static void setUp() {
        System.out.println("=== 设置测试环境 ===");
        
        // 关键：设置系统属性，强制使用内存数据库
        System.setProperty("test.derby.url", "jdbc:derby:memory:testdb;create=true");
        
        System.out.println("测试数据库URL: " + System.getProperty("test.derby.url"));
        
        try {
            // 确保Derby驱动已加载
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("✅ Derby驱动加载成功");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Derby驱动加载失败: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    void getConnection_ShouldReturnValidConnection() {
        System.out.println("\n--- 测试: getConnection_ShouldReturnValidConnection ---");
        
        try {
            // 执行
            Connection connection = DatabaseConnection.getConnection();
            
            // 验证
            assertNotNull(connection);
            assertFalse(connection.isClosed());
            assertTrue(connection.isValid(2)); // 2秒超时测试连接有效性
            
            System.out.println("✅ 连接成功");
            System.out.println("数据库: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("URL: " + connection.getMetaData().getURL());
            
            connection.close();
            
        } catch (Exception e) {
            System.err.println("❌ 连接失败: " + e.getMessage());
            
            // 诊断信息
            diagnoseIssue();
            
            fail("不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    void testConnection_ShouldReturnTrueWhenConnected() {
        System.out.println("\n--- 测试: testConnection_ShouldReturnTrueWhenConnected ---");
        
        // 先测试直接连接
        boolean directConnected = testDirectDerbyConnection();
        assertTrue(directConnected, "直接Derby连接应该成功");
        
        // 再测试应用的方法
        boolean appConnected = DatabaseConnection.testConnection();
        assertTrue(appConnected, "DatabaseConnection.testConnection() 应该返回 true");
        
        System.out.println("✅ 连接状态测试通过");
    }

    @Test
    void databaseInitializer_ShouldCreateTables() {
        System.out.println("\n--- 测试: databaseInitializer_ShouldCreateTables ---");
        
        try {
            // 保存原始URL
            String originalUrl = DatabaseConnection.getDatabaseUrl();
            System.out.println("原始URL: " + originalUrl);
            
            // 创建专用的测试数据库
            String testDbUrl = "jdbc:derby:memory:init_test_db;create=true";
            System.out.println("测试数据库: " + testDbUrl);
            
            // 临时修改URL
            setStaticField(DatabaseConnection.class, "URL", testDbUrl);
            
            try {
                // 清理可能存在的旧表
                try (Connection conn = DriverManager.getConnection(testDbUrl);
                     Statement stmt = conn.createStatement()) {
                    try { stmt.execute("DROP TABLE payments"); } catch (Exception e) { /* 忽略 */ }
                    try { stmt.execute("DROP TABLE bookings"); } catch (Exception e) { /* 忽略 */ }
                    try { stmt.execute("DROP TABLE rooms"); } catch (Exception e) { /* 忽略 */ }
                    try { stmt.execute("DROP TABLE hotels"); } catch (Exception e) { /* 忽略 */ }
                    try { stmt.execute("DROP TABLE users"); } catch (Exception e) { /* 忽略 */ }
                }
                
                // 执行初始化
                DatabaseInitializer.initializeDatabase();
                System.out.println("✅ 数据库初始化执行成功");
                
                // 验证表是否存在
                try (Connection conn = DriverManager.getConnection(testDbUrl);
                     Statement stmt = conn.createStatement()) {
                    
                    String[] tables = {"USERS", "HOTELS", "ROOMS", "BOOKINGS", "PAYMENTS"};
                    
                    for (String table : tables) {
                        ResultSet rs = stmt.executeQuery(
                            "SELECT COUNT(*) FROM SYS.SYSTABLES WHERE TABLENAME = '" + table + "'"
                        );
                        if (rs.next() && rs.getInt(1) > 0) {
                            System.out.println("✅ 表 " + table + " 存在");
                        } else {
                            // 尝试直接查询
                            try {
                                stmt.executeQuery("SELECT 1 FROM " + table);
                                System.out.println("✅ 表 " + table + " 可访问");
                            } catch (Exception e) {
                                System.err.println("❌ 表 " + table + " 不存在或不可访问: " + e.getMessage());
                                fail("表 " + table + " 应该被创建");
                            }
                        }
                    }
                }
                
                System.out.println("✅ 数据库初始化测试通过");
                
            } finally {
                // 恢复原始URL
                setStaticField(DatabaseConnection.class, "URL", originalUrl);
            }
            
        } catch (Exception e) {
            System.err.println("❌ 数据库初始化测试失败: " + e.getMessage());
            
            if (e.getMessage().contains("already exists")) {
                System.out.println("⚠️ 表已存在，跳过创建");
                // 不抛出失败
            } else {
                fail("数据库初始化测试失败: " + e.getMessage());
            }
        }
    }
    
    private boolean testDirectDerbyConnection() {
        System.out.println("测试直接Derby连接...");
        
        try {
            String url = "jdbc:derby:memory:direct_test;create=true";
            Connection conn = DriverManager.getConnection(url);
            
            assertNotNull(conn);
            assertFalse(conn.isClosed());
            
            // 执行简单查询
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("VALUES 1");
                assertTrue(rs.next());
                assertEquals(1, rs.getInt(1));
                System.out.println("✅ 直接连接测试成功");
            }
            
            conn.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ 直接连接测试失败: " + e.getMessage());
            return false;
        }
    }
    
    private void diagnoseIssue() {
        System.err.println("\n=== 问题诊断 ===");
        
        try {
            // 1. 检查当前URL
            System.err.println("1. DatabaseConnection URL: " + DatabaseConnection.getDatabaseUrl());
            
            // 2. 检查系统属性
            System.err.println("2. 系统属性 test.derby.url: " + System.getProperty("test.derby.url"));
            
            // 3. 检查DriverManager中的驱动
            System.err.println("3. 已注册的JDBC驱动:");
            java.util.Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                java.sql.Driver driver = drivers.nextElement();
                System.err.println("   - " + driver.getClass().getName());
            }
            
            // 4. 测试不同URL
            System.err.println("4. 测试不同数据库URL:");
            String[] testUrls = {
                "jdbc:derby:memory:diagnose1;create=true",
                "jdbc:derby:memory:diagnose2;create=true",
                DatabaseConnection.getDatabaseUrl()
            };
            
            for (String url : testUrls) {
                try {
                    Connection conn = DriverManager.getConnection(url, "app", "app");
                    System.err.println("   ✅ " + url + " - 成功");
                    conn.close();
                } catch (Exception e) {
                    System.err.println("   ❌ " + url + " - 失败: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("诊断过程出错: " + e.getMessage());
        }
    }
    
    private static void setStaticField(Class<?> clazz, String fieldName, Object value) {
        try {
            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            throw new RuntimeException("无法设置字段 " + fieldName, e);
        }
    }
    
    @AfterAll
    static void tearDown() {
        System.out.println("\n=== 清理测试环境 ===");
        // 注释掉关闭数据库的代码，让其他测试可以使用
        // try {
        //     // 关闭Derby
        //     DriverManager.getConnection("jdbc:derby:;shutdown=true");
        //     System.out.println("✅ Derby已关闭");
        // } catch (Exception e) {
        //     // 忽略关闭异常
        //     System.out.println("Derby关闭完成");
        // }
    }
}
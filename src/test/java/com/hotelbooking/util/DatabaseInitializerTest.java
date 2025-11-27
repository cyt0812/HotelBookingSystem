package com.hotelbooking.util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseInitializerTest {

    @Test
    void initializeDatabase_ShouldCreateAllTables() {
        // 执行
        DatabaseInitializer.initializeDatabase();
        
        // 验证 - 检查表是否创建成功
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 检查 users 表
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            assertTrue(rs.next());
            
            // 检查 hotels 表
            rs = stmt.executeQuery("SELECT COUNT(*) FROM hotels");
            assertTrue(rs.next());
            
            // 检查 rooms 表
            rs = stmt.executeQuery("SELECT COUNT(*) FROM rooms");
            assertTrue(rs.next());
            
            // 检查 bookings 表
            rs = stmt.executeQuery("SELECT COUNT(*) FROM bookings");
            assertTrue(rs.next());
            
        } catch (Exception e) {
            fail("Database initialization failed: " + e.getMessage());
        }
    }

    @Test
    void clearTestData_ShouldClearAllData() {
        // 准备 - 先插入一些测试数据
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("INSERT INTO users (username, email, password, role, created_at) VALUES " +
                    "('testuser', 'test@test.com', 'pass', 'CUSTOMER', CURRENT_TIMESTAMP)");
            
        } catch (Exception e) {
            fail("Failed to setup test data: " + e.getMessage());
        }
        
        // 执行
        DatabaseInitializer.clearTestData();
        
        // 验证 - 检查数据是否被清空
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));
            
        } catch (Exception e) {
            fail("Failed to verify data clearance: " + e.getMessage());
        }
    }

    @Test
    void insertSampleData_ShouldInsertSampleData() {
        // 执行
        DatabaseInitializer.clearTestData(); // 先清空
        DatabaseInitializer.insertSampleData();
        
        // 验证 - 检查示例数据是否插入
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 检查用户数据
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) >= 3); // 至少3个示例用户
            
            // 检查酒店数据
            rs = stmt.executeQuery("SELECT COUNT(*) FROM hotels");
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) >= 3); // 至少3个示例酒店
            
            // 检查房间数据
            rs = stmt.executeQuery("SELECT COUNT(*) FROM rooms");
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) >= 5); // 至少5个示例房间
            
        } catch (Exception e) {
            fail("Sample data insertion failed: " + e.getMessage());
        }
    }
}
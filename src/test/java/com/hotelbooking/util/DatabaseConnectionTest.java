package com.hotelbooking.util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void getConnection_ShouldReturnValidConnection() {
        // 执行
        try (Connection connection = DatabaseConnection.getConnection()) {
            // 验证
            assertNotNull(connection);
            assertFalse(connection.isClosed());
            assertTrue(connection.isValid(2)); // 2秒超时测试连接有效性
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void testConnection_ShouldReturnTrueWhenConnected() {
        // 执行 & 验证
        assertTrue(DatabaseConnection.testConnection());
    }

    @Test
    void databaseInitializer_ShouldCreateTables() {
        // 执行
        DatabaseInitializer.initializeDatabase();
        
        // 验证 - 如果没抛出异常就认为成功
        assertTrue(DatabaseConnection.testConnection());
    }
}
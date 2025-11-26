package com.hotelbooking.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    // 手动保存原始URL（和DatabaseConnection中的URL一致）
    private static final String ORIGINAL_URL = "jdbc:derby://localhost:1527/HotelDB;create=true";

    // 测试前无需额外操作，因为原始URL是固定的

    // 测试后：强制恢复原始URL，避免影响其他测试
    @AfterEach
    void tearDown() {
        DatabaseConnection.setUrl(ORIGINAL_URL);
    }

    // 测试正常连接数据库（需确保本地Derby服务已启动）
    @Test
    void getConnection_shouldReturnValidConnection() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection, "数据库连接不应为null");
        assertFalse(connection.isClosed(), "连接应处于打开状态");
        connection.close(); // 测试后关闭连接
    }

    // 测试连接无效URL时抛出异常（核心修复）
    @Test
    void getConnection_whenInvalidUrl_shouldThrowSQLException() {
        // 步骤1：设置无效URL（错误端口+不存在的数据库）
        DatabaseConnection.setUrl("jdbc:derby://localhost:9999/InvalidDB;create=true");
        // 步骤2：验证调用getConnection会抛出SQLException
        assertThrows(SQLException.class,
                DatabaseConnection::getConnection,
                "连接无效URL时应抛出SQLException");
    }

    // 测试main方法（验证连接测试逻辑）
    @Test
    void main_shouldPrintConnectionStatus() {
        // 调用main方法，验证是否能正常执行（无异常抛出）
        assertDoesNotThrow(() -> DatabaseConnection.main(new String[0]),
                "main方法执行时不应抛出异常");
    }
}
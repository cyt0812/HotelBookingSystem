package com.hotelbooking;

import com.hotelbooking.util.DatabaseConnection;
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestMainTest {

    @BeforeEach
    void setUp() {
        // 可以在这里设置测试环境
    }

    @Test
    void testMainMethod() {
        // 使用mockStatic来模拟静态方法调用
        try (MockedStatic<DatabaseInitializer> mockedInitializer = Mockito.mockStatic(DatabaseInitializer.class);
             MockedStatic<DatabaseConnection> mockedConnection = Mockito.mockStatic(DatabaseConnection.class)) {

            // 模拟DatabaseInitializer.initializeDatabase()方法
            mockedInitializer.when(DatabaseInitializer::initializeDatabase).thenAnswer(invocation -> null);

            // 模拟DatabaseConnection.getConnection()返回一个模拟连接
            Connection mockConn = mock(Connection.class);
            mockedConnection.when(DatabaseConnection::getConnection).thenReturn(mockConn);

            // 调用TestMain的main方法
            TestMain.main(new String[]{});

            // 验证静态方法是否被调用
            mockedInitializer.verify(DatabaseInitializer::initializeDatabase, times(1));
            mockedConnection.verify(DatabaseConnection::getConnection, times(1));

            // 验证连接是否被关闭
            verify(mockConn, times(1)).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

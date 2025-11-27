package com.hotelbooking.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        String createUserTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL,
                role VARCHAR(20) NOT NULL,
                created_at TIMESTAMP NOT NULL
            )
            """;
            
        String createHotelTable = """
            CREATE TABLE hotels (
                id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                name VARCHAR(100) NOT NULL,
                location VARCHAR(100) NOT NULL,
                description VARCHAR(500),
                available_rooms INTEGER NOT NULL
            )
            """;
            
        String createRoomTable = """
            CREATE TABLE rooms (
                id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                hotel_id INTEGER NOT NULL,
                room_number VARCHAR(10) NOT NULL,
                room_type VARCHAR(20) NOT NULL,
                price DECIMAL(10,2) NOT NULL,
                available BOOLEAN NOT NULL,
                FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
                UNIQUE (hotel_id, room_number)
            )
            """;
            
        String createBookingTable = """
            CREATE TABLE bookings (
                id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                user_id INTEGER NOT NULL,
                hotel_id INTEGER NOT NULL,
                room_id INTEGER NOT NULL,
                check_in_date DATE NOT NULL,
                check_out_date DATE NOT NULL,
                total_price DECIMAL(10,2) NOT NULL,
                status VARCHAR(20) NOT NULL,
                created_at TIMESTAMP NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
                FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
                CHECK (check_out_date > check_in_date)
            )
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 创建用户表
            stmt.executeUpdate(createUserTable);
            System.out.println("Users table created successfully");
            
            // 创建酒店表
            stmt.executeUpdate(createHotelTable);
            System.out.println("Hotels table created successfully");
            
            // 创建房间表
            stmt.executeUpdate(createRoomTable);
            System.out.println("Rooms table created successfully");
            
            // 创建预订表
            stmt.executeUpdate(createBookingTable);
            System.out.println("Bookings table created successfully");
            
            System.out.println("Database initialized successfully");
            
        } catch (SQLException e) {
            // 如果表已存在，会抛出异常，这是正常的
            if (e.getSQLState().equals("X0Y32")) {
                // Derby 的表已存在错误代码
                System.out.println("Database tables already exist");
            } else {
                System.out.println("Database initialization error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 清空所有测试数据（用于测试环境）
     */
    public static void clearTestData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 注意：由于外键约束，需要按正确顺序删除
            stmt.executeUpdate("DELETE FROM bookings");
            stmt.executeUpdate("DELETE FROM rooms");
            stmt.executeUpdate("DELETE FROM hotels");
            stmt.executeUpdate("DELETE FROM users");
            
            System.out.println("Test data cleared successfully");
            
        } catch (SQLException e) {
            System.out.println("Error clearing test data: " + e.getMessage());
        }
    }
    
    /**
     * 插入一些示例数据用于测试和演示
     */
    public static void insertSampleData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 插入示例用户
            String insertUsers = """
                INSERT INTO users (username, email, password, role, created_at) VALUES
                ('admin', 'admin@hotel.com', 'admin123', 'ADMIN', CURRENT_TIMESTAMP),
                ('john_doe', 'john@example.com', 'password123', 'CUSTOMER', CURRENT_TIMESTAMP),
                ('jane_smith', 'jane@example.com', 'password123', 'CUSTOMER', CURRENT_TIMESTAMP)
                """;
            
            // 插入示例酒店
            String insertHotels = """
                INSERT INTO hotels (name, location, description, available_rooms) VALUES
                ('Grand Plaza Hotel', 'New York', 'Luxury hotel in downtown Manhattan', 20),
                ('Beach Resort', 'Miami', 'Beachfront property with ocean view', 15),
                ('Mountain Lodge', 'Denver', 'Cozy lodge in the mountains', 10)
                """;
            
            // 插入示例房间
            String insertRooms = """
                INSERT INTO rooms (hotel_id, room_number, room_type, price, available) VALUES
                (1, '101', 'SINGLE', 99.99, true),
                (1, '102', 'DOUBLE', 149.99, true),
                (1, '201', 'SUITE', 249.99, true),
                (2, '101', 'SINGLE', 79.99, true),
                (2, '102', 'DOUBLE', 129.99, false),
                (3, '101', 'SINGLE', 89.99, true)
                """;
            
            stmt.executeUpdate(insertUsers);
            stmt.executeUpdate(insertHotels);
            stmt.executeUpdate(insertRooms);
            
            System.out.println("Sample data inserted successfully");
            
        } catch (SQLException e) {
            System.out.println("Error inserting sample data: " + e.getMessage());
        }
    }
    
    /**
     * 重置数据库（删除所有表并重新创建）
     * 注意：这会删除所有数据！
     */
    public static void resetDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 由于外键约束，需要按正确顺序删除表
            try {
                stmt.executeUpdate("DROP TABLE bookings");
                System.out.println("Bookings table dropped");
            } catch (SQLException e) {
                System.out.println("Bookings table doesn't exist or can't be dropped");
            }
            
            try {
                stmt.executeUpdate("DROP TABLE rooms");
                System.out.println("Rooms table dropped");
            } catch (SQLException e) {
                System.out.println("Rooms table doesn't exist or can't be dropped");
            }
            
            try {
                stmt.executeUpdate("DROP TABLE hotels");
                System.out.println("Hotels table dropped");
            } catch (SQLException e) {
                System.out.println("Hotels table doesn't exist or can't be dropped");
            }
            
            try {
                stmt.executeUpdate("DROP TABLE users");
                System.out.println("Users table dropped");
            } catch (SQLException e) {
                System.out.println("Users table doesn't exist or can't be dropped");
            }
            
            // 重新初始化数据库
            initializeDatabase();
            System.out.println("Database reset successfully");
            
        } catch (SQLException e) {
            System.out.println("Error resetting database: " + e.getMessage());
        }
    }
}
package com.hotelbooking.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
           String[] dropTables = {
        "DROP TABLE payments",
        "DROP TABLE bookings", 
        "DROP TABLE rooms",
        "DROP TABLE hotels",
        "DROP TABLE users"
    };      
        // 修复点：BOOLEAN → SMALLINT (Derby 不支持 BOOLEAN)
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
        max_occupancy INTEGER NOT NULL,  -- 新增
        description VARCHAR(500),        -- 新增
        available SMALLINT NOT NULL,
        FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
        UNIQUE (hotel_id, room_number)
    )
    """;

        String createBookingTable = """
            CREATE TABLE bookings (
                id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                booking_id VARCHAR(50) UNIQUE NOT NULL,
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

        String createPaymentTable = """
            CREATE TABLE payments (
                id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                payment_id VARCHAR(50) UNIQUE NOT NULL,
                booking_id VARCHAR(50) NOT NULL,
                amount DECIMAL(10,2) NOT NULL,
                payment_method VARCHAR(20) NOT NULL,
                payment_status VARCHAR(20) NOT NULL,
                payment_date TIMESTAMP NOT NULL,
                transaction_id VARCHAR(100),
                FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
            )
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
<<<<<<< HEAD
  System.out.println("Starting to drop old tables...");
        for (String dropSql : dropTables) {
            try {
                stmt.executeUpdate(dropSql);
                System.out.println("Table dropped successfully: " + dropSql);
            } catch (SQLException e) {
                // 表不存在也没关系
                if (!"42Y55".equals(e.getSQLState())) { // Derby的表不存在错误码
                    System.out.println("Warning when dropping table: " + e.getMessage());
=======
  System.out.println("开始删除旧表...");
        for (String dropSql : dropTables) {
            try {
                stmt.executeUpdate(dropSql);
                System.out.println("删除表成功: " + dropSql);
            } catch (SQLException e) {
                // 表不存在也没关系
                if (!"42Y55".equals(e.getSQLState())) { // Derby的表不存在错误码
                    System.out.println("删除表时警告: " + e.getMessage());
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
                }
            }
        }
        
            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createHotelTable);
            stmt.executeUpdate(createRoomTable);
            stmt.executeUpdate(createBookingTable);
            stmt.executeUpdate(createPaymentTable);

            System.out.println("Database initialized successfully");

        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("Tables already exist");
            } else {
                System.out.println("Initialization error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /** 清空数据 */
    public static void clearTestData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM payments");
            stmt.executeUpdate("DELETE FROM bookings");
            stmt.executeUpdate("DELETE FROM rooms");
            stmt.executeUpdate("DELETE FROM hotels");
            stmt.executeUpdate("DELETE FROM users");

            System.out.println("Test data cleared");

        } catch (SQLException e) {
            System.out.println("Clear data error: " + e.getMessage());
        }
    }

    // 重置数据库：清空所有数据并重新初始化表结构
    public static void resetDatabase() {
        clearTestData(); // 先清空现有数据
        initializeDatabase(); // 然后重新初始化数据库
    }
    
    /** 插入示例数据（修复 Derby 时间语法） */
    public static void insertSampleData() {
    try (Connection conn = DatabaseConnection.getConnection()) {

        // 插入用户
        try (Statement stmt = conn.createStatement()) {
            String insertUsers = """
                INSERT INTO users (username, email, password, role, created_at) VALUES
                ('admin','admin@hotel.com','admin123','ADMIN', CURRENT_TIMESTAMP),
                ('manager1','manager@hotel.com','manager123','HOTEL_MANAGER', CURRENT_TIMESTAMP),
                ('john_doe','john@example.com','password123','CUSTOMER', CURRENT_TIMESTAMP),
                ('jane_smith','jane@example.com','password123','CUSTOMER', CURRENT_TIMESTAMP)
                """;
            stmt.executeUpdate(insertUsers);
        }

        // 插入酒店并保存生成的 ID
        String[] hotelNames = { "Grand Plaza Hotel", "Sunset Resort", "Mountain Lodge" };
        String[] locations = { "New York", "Miami Beach", "Colorado" };
        String[] descriptions = {
            "Luxury hotel in Manhattan",
            "Beachfront resort with spa",
            "Cozy mountain retreat"
        };
        String[] amenities = {
            "WiFi, Pool, Gym, Restaurant, Bar, Spa",
            "WiFi, Beach Access, Pool, Gym, Spa, Kids Club",
            "WiFi, Meeting Rooms, Business Center, Gym, Restaurant"
        };
        int[] availableRooms = { 50, 30, 20 };

        int[] hotelIds = new int[hotelNames.length];

        String insertHotelSQL = "INSERT INTO hotels (name, location, description, available_rooms) VALUES (?, ?, ?, ?)";
        try (PreparedStatement psHotel = conn.prepareStatement(insertHotelSQL, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < hotelNames.length; i++) {
                psHotel.setString(1, hotelNames[i]);
                psHotel.setString(2, locations[i]);
                psHotel.setString(3, descriptions[i]);
                psHotel.setInt(4, availableRooms[i]);
                psHotel.executeUpdate();

                try (ResultSet rs = psHotel.getGeneratedKeys()) {
                    if (rs.next()) {
                        hotelIds[i] = rs.getInt(1); // 保存自动生成的ID
                    }
                }
            }
        }

        // 插入房间
        String insertRoomSQL = "INSERT INTO rooms (hotel_id, room_number, room_type, price, available, max_occupancy, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psRoom = conn.prepareStatement(insertRoomSQL)) {
                // Grand Plaza Hotel 房间
                psRoom.setInt(1, hotelIds[0]);
                psRoom.setString(2, "101");
                psRoom.setString(3, "STANDARD");
                psRoom.setDouble(4, 99.99);
                psRoom.setInt(5, 1);
                psRoom.setInt(6, 2);
                psRoom.setString(7, "Standard room");
                psRoom.addBatch();

                psRoom.setInt(1, hotelIds[0]);
                psRoom.setString(2, "102");
                psRoom.setString(3, "DELUXE");
                psRoom.setDouble(4, 149.99);
                psRoom.setInt(5, 1);
                psRoom.setInt(6, 3);
                psRoom.setString(7, "Deluxe room with balcony");
                psRoom.addBatch();

                psRoom.setInt(1, hotelIds[0]);
                psRoom.setString(2, "201");
                psRoom.setString(3, "SUITE");
                psRoom.setDouble(4, 249.99);
                psRoom.setInt(5, 1);
                psRoom.setInt(6, 4);
                psRoom.setString(7, "Luxury suite");
                psRoom.addBatch();

                // Sunset Resort 房间
                psRoom.setInt(1, hotelIds[1]);
                psRoom.setString(2, "101");
                psRoom.setString(3, "STANDARD");
                psRoom.setDouble(4, 79.99);
                psRoom.setInt(5, 1);
                psRoom.setInt(6, 2);
                psRoom.setString(7, "Standard beachfront room");
                psRoom.addBatch();

                psRoom.setInt(1, hotelIds[1]);
                psRoom.setString(2, "102");
                psRoom.setString(3, "DELUXE");
                psRoom.setDouble(4, 129.99);
                psRoom.setInt(5, 0); // 不可用
                psRoom.setInt(6, 3);
                psRoom.setString(7, "Deluxe beachfront room");
                psRoom.addBatch();

                // Mountain Lodge 房间
                psRoom.setInt(1, hotelIds[2]);
                psRoom.setString(2, "101");
                psRoom.setString(3, "STANDARD");
                psRoom.setDouble(4, 89.99);
                psRoom.setInt(5, 1);
                psRoom.setInt(6, 2);
                psRoom.setString(7, "Cozy mountain standard room");
                psRoom.addBatch();

                psRoom.setInt(1, hotelIds[2]);
                psRoom.setString(2, "102");
                psRoom.setString(3, "SUITE");
                psRoom.setDouble(4, 199.99);
                psRoom.setInt(5, 1);
                psRoom.setInt(6, 4);
                psRoom.setString(7, "Mountain suite");
                psRoom.addBatch();

                psRoom.executeBatch();
            }

            System.out.println("Sample data inserted");

        } catch (SQLException e) {
            System.out.println("Insert sample data error: " + e.getMessage());
            e.printStackTrace();
        }
    }
//    public static void insertSampleData() {
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement()) {
//
//            String insertUsers = """
//                INSERT INTO users (username, email, password, role, created_at) VALUES
//                ('admin','admin@hotel.com','admin123','ADMIN', CURRENT_TIMESTAMP),
//                ('manager1','manager@hotel.com','manager123','HOTEL_MANAGER', CURRENT_TIMESTAMP),
//                ('john_doe','john@example.com','password123','CUSTOMER', CURRENT_TIMESTAMP),
//                ('jane_smith','jane@example.com','password123','CUSTOMER', CURRENT_TIMESTAMP)
//                """;
//
//            // 修复点：Derby 不支持 true/false → 改成 1/0
//            String insertRooms = """
//                INSERT INTO rooms (hotel_id, room_number, room_type, price, available) VALUES
//                (1, '101', 'STANDARD', 99.99, 1),
//                (1, '102', 'DELUXE', 149.99, 1),
//                (1, '201', 'SUITE', 249.99, 1),
//                (2, '101', 'STANDARD', 79.99, 1),
//                (2, '102', 'DELUXE', 129.99, 0),
//                (3, '101', 'STANDARD', 89.99, 1),
//                (3, '102', 'SUITE', 199.99, 1)
//                """;
//
//            // 修复点：CURRENT_DATE + 7 → TIMESTAMPADD
//            String insertBookings = """
//                INSERT INTO bookings (
//                    booking_id, user_id, hotel_id, room_id,
//                    check_in_date, check_out_date, total_price, status, created_at
//                ) VALUES
//                ('BOOK_001', 3, 1, 1,
//                  {fn TIMESTAMPADD(SQL_TSI_DAY, 7, CURRENT_DATE)},
//                  {fn TIMESTAMPADD(SQL_TSI_DAY, 9, CURRENT_DATE)},
//                  199.98, 'CONFIRMED', CURRENT_TIMESTAMP),
//                ('BOOK_002', 4, 2, 4,
//                  {fn TIMESTAMPADD(SQL_TSI_DAY, 14, CURRENT_DATE)},
//                  {fn TIMESTAMPADD(SQL_TSI_DAY, 16, CURRENT_DATE)},
//                  159.98, 'PENDING', CURRENT_TIMESTAMP)
//                """;
//
//            // 修复点：INTERVAL → TIMESTAMPADD
//            String insertPayments = """
//                INSERT INTO payments (
//                    payment_id, booking_id, amount, payment_method, payment_status, payment_date, transaction_id
//                ) VALUES
//                ('PAY_001','BOOK_001',199.98,'CREDIT_CARD','COMPLETED', CURRENT_TIMESTAMP,'TXN_123456'),
//                ('PAY_002','BOOK_002',159.98,'PAYPAL','PENDING', CURRENT_TIMESTAMP, NULL),
//                ('PAY_003','BOOK_001',179.98,'CREDIT_CARD','COMPLETED',
//                 {fn TIMESTAMPADD(SQL_TSI_DAY, -5, CURRENT_TIMESTAMP)}, 'TXN_789012')
//                """;
//
//            stmt.executeUpdate(insertUsers);
//            stmt.executeUpdate("""
//                INSERT INTO hotels (name, location, description, available_rooms) VALUES 
//                ('Grand Plaza Hotel', 'New York', 'Luxury hotel in Manhattan', 50),
//                ('Sunset Resort', 'Miami Beach', 'Beachfront resort with spa', 30),
//                ('Mountain Lodge', 'Colorado', 'Cozy mountain retreat', 20)
//                """);
////            stmt.executeUpdate("""
////                INSERT INTO hotels (name, location, description, amenities, available_rooms) VALUES 
////                ('Grand Plaza Hotel', 'New York', 'Luxury hotel in Manhattan','WiFi, Pool, Gym, Restaurant, Bar, Spa', 50),
////                ('Sunset Resort', 'Miami Beach', 'Beachfront resort with spa','WiFi, Beach Access, Pool, Gym, Spa, Kids Club', 30),
////                ('Mountain Lodge', 'Colorado', 'Cozy mountain retreat','WiFi, Meeting Rooms, Business Center, Gym, Restaurant', 20)
////                """);
//            stmt.executeUpdate(insertRooms);
//            stmt.executeUpdate(insertBookings);
//            stmt.executeUpdate(insertPayments);
//
//            System.out.println("Sample data inserted");
//
//        } catch (SQLException e) {
//            System.out.println("Insert sample data error: " + e.getMessage());
//        }
//    }
}
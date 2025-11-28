package com.hotelbooking.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {

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
                available SMALLINT NOT NULL,   -- 修复点：BOOLEAN → SMALLINT
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

    /** 插入示例数据（修复 Derby 时间语法） */
    public static void insertSampleData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String insertUsers = """
                INSERT INTO users (username, email, password, role, created_at) VALUES
                ('admin','admin@hotel.com','admin123','ADMIN', CURRENT_TIMESTAMP),
                ('manager1','manager@hotel.com','manager123','HOTEL_MANAGER', CURRENT_TIMESTAMP),
                ('john_doe','john@example.com','password123','CUSTOMER', CURRENT_TIMESTAMP),
                ('jane_smith','jane@example.com','password123','CUSTOMER', CURRENT_TIMESTAMP)
                """;

            // 修复点：Derby 不支持 true/false → 改成 1/0
            String insertRooms = """
                INSERT INTO rooms (hotel_id, room_number, room_type, price, available) VALUES
                (1, '101', 'STANDARD', 99.99, 1),
                (1, '102', 'DELUXE', 149.99, 1),
                (1, '201', 'SUITE', 249.99, 1),
                (2, '101', 'STANDARD', 79.99, 1),
                (2, '102', 'DELUXE', 129.99, 0),
                (3, '101', 'STANDARD', 89.99, 1),
                (3, '102', 'SUITE', 199.99, 1)
                """;

            // 修复点：CURRENT_DATE + 7 → TIMESTAMPADD
            String insertBookings = """
                INSERT INTO bookings (
                    booking_id, user_id, hotel_id, room_id,
                    check_in_date, check_out_date, total_price, status, created_at
                ) VALUES
                ('BOOK_001', 3, 1, 1,
                  {fn TIMESTAMPADD(SQL_TSI_DAY, 7, CURRENT_DATE)},
                  {fn TIMESTAMPADD(SQL_TSI_DAY, 9, CURRENT_DATE)},
                  199.98, 'CONFIRMED', CURRENT_TIMESTAMP),
                ('BOOK_002', 4, 2, 4,
                  {fn TIMESTAMPADD(SQL_TSI_DAY, 14, CURRENT_DATE)},
                  {fn TIMESTAMPADD(SQL_TSI_DAY, 16, CURRENT_DATE)},
                  159.98, 'PENDING', CURRENT_TIMESTAMP)
                """;

            // 修复点：INTERVAL → TIMESTAMPADD
            String insertPayments = """
                INSERT INTO payments (
                    payment_id, booking_id, amount, payment_method, payment_status, payment_date, transaction_id
                ) VALUES
                ('PAY_001','BOOK_001',199.98,'CREDIT_CARD','COMPLETED', CURRENT_TIMESTAMP,'TXN_123456'),
                ('PAY_002','BOOK_002',159.98,'PAYPAL','PENDING', CURRENT_TIMESTAMP, NULL),
                ('PAY_003','BOOK_001',179.98,'CREDIT_CARD','COMPLETED',
                 {fn TIMESTAMPADD(SQL_TSI_DAY, -5, CURRENT_TIMESTAMP)}, 'TXN_789012')
                """;

            stmt.executeUpdate(insertUsers);
            stmt.executeUpdate("INSERT INTO hotels (name, location, description, available_rooms) VALUES ('Grand Plaza','NY','desc',20)");
            stmt.executeUpdate(insertRooms);
            stmt.executeUpdate(insertBookings);
            stmt.executeUpdate(insertPayments);

            System.out.println("Sample data inserted");

        } catch (SQLException e) {
            System.out.println("Insert sample data error: " + e.getMessage());
        }
    }
}

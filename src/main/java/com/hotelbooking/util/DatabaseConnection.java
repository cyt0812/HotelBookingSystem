package com.hotelbooking.util;

import java.sql.Connection;
<<<<<<< HEAD
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        System.out.println("Using file storage mode - skipping database connection");
        return null;
=======
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // 使用嵌入式Derby
    private static final String URL = "jdbc:derby:HotelBookingDB;create=true";
    private static final String USER = "app";
    private static final String PASSWORD = "app";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Derby driver not found", e);
        }
>>>>>>> gui-dashboard/master
    }
}
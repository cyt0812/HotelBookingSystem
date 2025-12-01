package com.hotelbooking.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        System.out.println("Using file storage mode - skipping database connection");
        return null;
    }
}
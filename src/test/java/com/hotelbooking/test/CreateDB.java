// Create CreateDB.java and run
package com.hotelbooking.test;

import com.hotelbooking.util.DatabaseInitializer;

public class CreateDB {
    public static void main(String[] args) {
        System.out.println("Starting database creation...");
        DatabaseInitializer.initializeDatabase();
        DatabaseInitializer.insertSampleData();
        System.out.println("✅ Database creation completed!");
    }
}
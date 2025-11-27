package com.hotelbooking;

import com.hotelbooking.controller.AdminController;
import com.hotelbooking.controller.BookingController;
import com.hotelbooking.controller.HotelController;
import com.hotelbooking.controller.UserController;
import com.hotelbooking.dao.*;
import com.hotelbooking.service.*;
import com.hotelbooking.util.DatabaseConnection;
import com.hotelbooking.util.DatabaseInitializer;

public class Main {
    
    // 初始化所有组件
    private static UserDAO userDAO = new UserDAO();
    private static HotelDAO hotelDAO = new HotelDAO();
    private static RoomDAO roomDAO = new RoomDAO();
    private static BookingDAO bookingDAO = new BookingDAO();
    
    private static UserService userService = new UserService(userDAO);
    private static HotelService hotelService = new HotelService(hotelDAO);
    private static RoomService roomService = new RoomService(roomDAO);
    private static BookingService bookingService = new BookingService(bookingDAO, roomDAO);
    private static PaymentService paymentService = new PaymentService(bookingDAO);
    private static AdminService adminService = new AdminService(userDAO, hotelDAO, bookingDAO);
    
    public static UserController userController = new UserController(userService);
    public static HotelController hotelController = new HotelController(hotelService, roomService);
    public static BookingController bookingController = new BookingController(bookingService, paymentService, roomService);
    public static AdminController adminController = new AdminController(adminService, userService, hotelService, bookingService);
    
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking System ===");
        
        try {
            // 初始化数据库
            DatabaseInitializer.initializeDatabase();
            DatabaseInitializer.insertSampleData();
            
            System.out.println("Database initialized successfully!");
            DatabaseConnection.printDatabaseInfo();
            
            // 运行演示
            runDemo();
            
        } catch (Exception e) {
            System.err.println("Application startup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runDemo() {
        System.out.println("\n=== Running Demo ===");
        
        try {
            // 演示用户注册和登录
            System.out.println("\n1. User Registration Demo:");
            var user = userController.registerUser("demo_user", "demo@example.com", "password123", "CUSTOMER");
            System.out.println("Registered user: " + user.getUsername() + " (ID: " + user.getId() + ")");
            
            // 演示用户登录
            var loggedInUser = userController.loginUser("demo_user", "password123");
            System.out.println("Logged in as: " + loggedInUser.getUsername());
            
            // 演示查看酒店
            System.out.println("\n2. Hotel Listing Demo:");
            var hotels = hotelController.getAllHotels();
            System.out.println("Available hotels: " + hotels.size());
            hotels.forEach(hotel -> 
                System.out.println(" - " + hotel.getName() + " in " + hotel.getLocation())
            );
            
            // 演示查看可用房间
            if (!hotels.isEmpty()) {
                System.out.println("\n3. Available Rooms Demo:");
                var availableRooms = hotelController.getAvailableRoomsByHotel(hotels.get(0).getId());
                System.out.println("Available rooms in " + hotels.get(0).getName() + ": " + availableRooms.size());
            }
            
            // 演示管理员功能
            System.out.println("\n4. Admin Statistics Demo:");
            var stats = adminController.getBookingStatistics();
            System.out.println(stats);
            
            System.out.println("\n=== Demo Completed Successfully ===");
            
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
package com.hotelbooking;

import com.hotelbooking.controller.AdminController;
import com.hotelbooking.controller.BookingController;
import com.hotelbooking.controller.HotelController;
import com.hotelbooking.controller.UserController;
import com.hotelbooking.dao.*;
import com.hotelbooking.dto.ApiResponse;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.User;
import com.hotelbooking.service.*;
import com.hotelbooking.util.DatabaseConnection;
import com.hotelbooking.util.DatabaseInitializer;
import java.time.LocalDate;
import java.util.List;

public class Main {
    
    // 初始化所有组件
    private static UserDAO userDAO = new UserDAO();
    private static HotelDAO hotelDAO = new HotelDAO();
    private static RoomDAO roomDAO = new RoomDAO();
    private static BookingDAO bookingDAO = new BookingDAO();
    private static PaymentDAO paymentDAO = new PaymentDAO();
    
    private static UserService userService = new UserService(userDAO);
    private static HotelService hotelService = new HotelService(hotelDAO);
    private static RoomService roomService = new RoomService(roomDAO);
    private static PaymentService paymentService = new PaymentService(paymentDAO);
    private static BookingService bookingService = new BookingService(bookingDAO, roomDAO, paymentService);
    private static AdminService adminService = new AdminService(userDAO, hotelDAO, bookingDAO);
    
    public static UserController userController = new UserController(userService);
    public static HotelController hotelController = new HotelController(hotelService, roomService);
    public static BookingController bookingController = new BookingController(bookingService, paymentService, roomService);
    public static AdminController adminController = new AdminController(adminService, userService, hotelService, bookingService);
    
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking System ===");
        try {
    DatabaseInitializer.initializeDatabase();
    DatabaseInitializer.insertSampleData();
    System.out.println("Database initialized!");
} catch (Exception e) {
    System.out.println("Startup failed: " + e.getMessage());
}

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
        } finally {
            // 关闭数据库连接
            DatabaseConnection.shutdownDatabase();
        }
    }
    
    private static void runDemo() {
        System.out.println("\n=== Running Demo ===");
        
        try {
            // 演示用户注册和登录
            System.out.println("\n1. User Registration Demo:");
            ApiResponse<Object> registerResponse = userController.registerUser("demo_user", "demo@example.com", "password123", "CUSTOMER");
            
            if (registerResponse.isSuccess()) {
                User user = (User) registerResponse.getData();
                System.out.println("Registered user: " + user.getUsername() + " (ID: " + user.getId() + ")");
                
                // 演示用户登录
                System.out.println("\n2. User Login Demo:");
                ApiResponse<Object> loginResponse = userController.loginUser("demo_user", "password123");
                if (loginResponse.isSuccess()) {
                    User loggedInUser = (User) loginResponse.getData();
                    System.out.println("Logged in as: " + loggedInUser.getUsername());
                    
                    // 演示查看酒店
                    System.out.println("\n3. Hotel Listing Demo:");
                    ApiResponse<Object> hotelsResponse = hotelController.getAllHotels();
                    if (hotelsResponse.isSuccess()) {
                        List<Hotel> hotels = (List<Hotel>) hotelsResponse.getData();
                        System.out.println("Available hotels: " + hotels.size());
                        hotels.forEach(hotel -> 
                            System.out.println(" - " + hotel.getName() + " in " + hotel.getLocation())
                        );
                        
                        // 演示查看可用房间
                        if (!hotels.isEmpty()) {
                            System.out.println("\n4. Available Rooms Demo:");
                            ApiResponse<Object> roomsResponse = hotelController.getAvailableRoomsByHotel(hotels.get(0).getId());
                            if (roomsResponse.isSuccess()) {
                                List<Room> availableRooms = (List<Room>) roomsResponse.getData();
                                System.out.println("Available rooms in " + hotels.get(0).getName() + ": " + availableRooms.size());
                                if (!availableRooms.isEmpty()) {
                                    availableRooms.forEach(room -> 
                                        System.out.println("   - Room " + room.getRoomNumber() + " (" + room.getRoomType() + "): $" + room.getPrice())
                                    );
                                    
                                    // 演示创建带支付的预订
                                    System.out.println("\n5. Booking with Payment Demo:");
                                    try {
                                        LocalDate checkIn = LocalDate.now().plusDays(2);
                                        LocalDate checkOut = LocalDate.now().plusDays(4);
                                        
                                        ApiResponse<Object> bookingResponse = bookingController.createBookingWithPayment(
                                            user.getId(), 
                                            hotels.get(0).getId(), 
                                            availableRooms.get(0).getId(), 
                                            checkIn, 
                                            checkOut, 
                                            "CREDIT_CARD"
                                        );
                                        
                                        if (bookingResponse.isSuccess()) {
                                            Booking booking = (Booking) bookingResponse.getData();
                                            System.out.println("Booking created with ID: " + booking.getId());
                                            System.out.println("Booking reference: " + booking.getBookingId());
                                            System.out.println("Total price: $" + booking.getTotalPrice());
                                            System.out.println("Status: " + booking.getStatus());
                                            
                                            // 检查支付状态
                                            ApiResponse<Object> paymentResponse = bookingController.getPaymentStatus(booking.getBookingId());
                                            if (paymentResponse.isSuccess()) {
                                                System.out.println("Payment status: " + paymentResponse.getData());
                                            } else {
                                                System.err.println("Failed to get payment status: " + paymentResponse.getMessage());
                                            }
                                        } else {
                                            System.err.println("Booking failed: " + bookingResponse.getMessage());
                                        }
                                        
                                    } catch (Exception e) {
                                        System.err.println("Booking demo failed: " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                System.err.println("Failed to get available rooms: " + roomsResponse.getMessage());
                            }
                        }
                    } else {
                        System.err.println("Failed to get hotels: " + hotelsResponse.getMessage());
                    }
                    
                    // 演示管理员功能
                    System.out.println("\n6. Admin Statistics Demo:");
                    try {
                        ApiResponse<Object> statsResponse = adminController.getBookingStatistics();
                        if (statsResponse.isSuccess()) {
                            System.out.println(statsResponse.getData());
                        } else {
                            System.err.println("Admin statistics failed: " + statsResponse.getMessage());
                        }
                    } catch (Exception e) {
                        System.err.println("Admin statistics demo failed: " + e.getMessage());
                    }
                } else {
                    System.err.println("Login failed: " + loginResponse.getMessage());
                }
            } else {
                System.err.println("Registration failed: " + registerResponse.getMessage());
            }
            
            System.out.println("\n=== Demo Completed ===");
            
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 工具方法：安全地从ApiResponse获取数据
     */
    private static <T> T getDataFromResponse(ApiResponse<Object> response, Class<T> clazz) {
        if (response.isSuccess() && response.getData() != null) {
            return clazz.cast(response.getData());
        }
        throw new RuntimeException("Failed to get data from response: " + response.getMessage());
    }
}
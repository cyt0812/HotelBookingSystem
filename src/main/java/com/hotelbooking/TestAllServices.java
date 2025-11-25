package com.hotelbooking;

import com.hotelbooking.service.*;
import com.hotelbooking.entity.*;
import java.time.LocalDate;

public class TestAllServices {
    public static void main(String[] args) {
        System.out.println("=== Testing All Services ===");
        
        // 测试 UserService
        UserService userService = new UserService();
        User user = userService.loginUser("testuser", "password");
        System.out.println("UserService Test: " + (user != null ? "PASS" : "FAIL"));
        
        // 测试 HotelService
        HotelService hotelService = new HotelService();
        System.out.println("HotelService Test: " + hotelService.getAllHotels().size() + " hotels");
        
        // 测试 RoomService  
        RoomService roomService = new RoomService();
        System.out.println("RoomService Test: " + roomService.getAllRooms().size() + " rooms");
        
        // 测试 BookingService
        BookingService bookingService = new BookingService();
        System.out.println("BookingService Test: Ready");
        
        System.out.println("=== All Service Tests Completed ===");
        
        // 测试预订功能
        if (user != null) {
            System.out.println("\n--- Testing Booking Function ---");
            LocalDate checkIn = LocalDate.now().plusDays(1);
            LocalDate checkOut = LocalDate.now().plusDays(3);
            
            // 测试房间可用性
            boolean available = bookingService.isRoomAvailable(1, checkIn, checkOut);
            System.out.println("Room 1 available: " + available);
            
            // 测试价格计算
            double price = bookingService.calculateBookingPrice(1, checkIn, checkOut);
            System.out.println("Estimated price: ￥" + price);
            
            // 测试实际预订
            Booking testBooking = new Booking(
                user.getUserId(), 
                1, 
                checkIn, 
                checkOut, 
                0.0  // 价格会在makeBooking中计算
            );
            boolean bookingSuccess = bookingService.makeBooking(testBooking);
            System.out.println("Booking creation: " + (bookingSuccess ? "SUCCESS" : "FAILED"));
        }
    }
}
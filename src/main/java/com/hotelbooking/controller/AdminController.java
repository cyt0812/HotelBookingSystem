package com.hotelbooking.controller;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.User;
import com.hotelbooking.service.AdminService;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.service.UserService;

import java.math.BigDecimal;
import java.util.List;

public class AdminController {
    private final AdminService adminService;
    private final UserService userService;
    private final HotelService hotelService;
    private final BookingService bookingService;
    
    public AdminController(AdminService adminService, UserService userService, 
                          HotelService hotelService, BookingService bookingService) {
        this.adminService = adminService;
        this.userService = userService;
        this.hotelService = hotelService;
        this.bookingService = bookingService;
    }
    
    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }
    
    /**
     * 获取所有酒店
     */
    public List<Hotel> getAllHotels() {
        return adminService.getAllHotels();
    }
    
    /**
     * 获取所有预订
     */
    public List<Booking> getAllBookings() {
        return adminService.getAllBookings();
    }
    
    /**
     * 获取预订统计信息
     */
    public String getBookingStatistics() {
        return adminService.getBookingStatistics();
    }
    
    /**
     * 更新用户角色
     */
    public boolean updateUserRole(Integer userId, String newRole) {
        return adminService.updateUserRole(userId, newRole);
    }
    
    /**
     * 删除用户
     */
    public boolean deleteUser(Integer userId) {
        return userService.deleteUser(userId);
    }
    
    /**
     * 删除酒店
     */
    public boolean deleteHotel(Integer hotelId) {
        return adminService.deleteHotel(hotelId);
    }
    
    /**
     * 取消用户预订
     */
    public boolean cancelBooking(Integer bookingId) {
        return adminService.cancelUserBooking(bookingId);
    }
    
    /**
     * 获取系统总收入
     */
    public BigDecimal getTotalRevenue() {
        return adminService.getTotalRevenue();
    }
    
    /**
     * 获取最受欢迎的酒店
     */
    public String getMostPopularHotel() {
        return adminService.getMostPopularHotel();
    }
    
    /**
     * 根据ID获取用户
     */
    public User getUserById(Integer userId) {
        return userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    /**
     * 根据ID获取酒店
     */
    public Hotel getHotelById(Integer hotelId) {
        return hotelService.getHotelById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));
    }
    
    /**
     * 根据ID获取预订
     */
    public Booking getBookingById(Integer bookingId) {
        return bookingService.getBookingById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
    }
    
    /**
     * 创建新酒店
     */
    public Hotel createHotel(String name, String location, String description, Integer availableRooms) {
        return hotelService.createHotel(name, location, description, availableRooms);
    }
    
    /**
     * 更新酒店信息
     */
    public boolean updateHotel(Hotel hotel) {
        return hotelService.updateHotel(hotel);
    }
}
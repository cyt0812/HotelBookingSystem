package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.User;
import com.hotelbooking.service.AdminService;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.service.UserService;
import com.hotelbooking.exception.GlobalExceptionHandler;
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
    public ApiResponse<Object> getAllUsers() {
        try {
            List<User> users = adminService.getAllUsers();
            return ApiResponse.success("获取用户列表成功", users);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取所有酒店
     */
    public ApiResponse<Object> getAllHotels() {
        try {
            List<Hotel> hotels = adminService.getAllHotels();
            return ApiResponse.success("获取酒店列表成功", hotels);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取所有预订
     */
    public ApiResponse<Object> getAllBookings() {
        try {
            List<Booking> bookings = adminService.getAllBookings();
            return ApiResponse.success("获取预订列表成功", bookings);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取预订统计信息
     */
    public ApiResponse<Object> getBookingStatistics() {
        try {
            String statistics = adminService.getBookingStatistics();
            return ApiResponse.success("获取统计信息成功", statistics);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 更新用户角色
     */
    public ApiResponse<Object> updateUserRole(Integer userId, String newRole) {
        try {
            boolean result = adminService.updateUserRole(userId, newRole);
            String message = result ? "用户角色更新成功" : "用户角色更新失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 删除用户
     */
    public ApiResponse<Object> deleteUser(Integer userId) {
        try {
            boolean result = userService.deleteUser(userId);
            String message = result ? "用户删除成功" : "用户删除失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 删除酒店
     */
    public ApiResponse<Object> deleteHotel(Integer hotelId) {
        try {
            boolean result = adminService.deleteHotel(hotelId);
            String message = result ? "酒店删除成功" : "酒店删除失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 取消用户预订
     */
    public ApiResponse<Object> cancelBooking(Integer bookingId) {
        try {
            boolean result = adminService.cancelUserBooking(bookingId);
            String message = result ? "预订取消成功" : "预订取消失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取系统总收入
     */
    public ApiResponse<Object> getTotalRevenue() {
        try {
            BigDecimal revenue = adminService.getTotalRevenue();
            return ApiResponse.success("获取总收入成功", revenue);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取最受欢迎的酒店
     */
    public ApiResponse<Object> getMostPopularHotel() {
        try {
            String popularHotel = adminService.getMostPopularHotel();
            return ApiResponse.success("获取热门酒店成功", popularHotel);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据ID获取用户
     */
    public ApiResponse<Object> getUserById(Integer userId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + userId));
            return ApiResponse.success("获取用户信息成功", user);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据ID获取酒店
     */
    public ApiResponse<Object> getHotelById(Integer hotelId) {
        try {
            Hotel hotel = hotelService.getHotelById(hotelId)
                    .orElseThrow(() -> new RuntimeException("酒店不存在，ID: " + hotelId));
            return ApiResponse.success("获取酒店信息成功", hotel);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据ID获取预订
     */
    public ApiResponse<Object> getBookingById(Integer bookingId) {
        try {
            Booking booking = bookingService.getBookingById(bookingId)
                    .orElseThrow(() -> new RuntimeException("预订不存在，ID: " + bookingId));
            return ApiResponse.success("获取预订信息成功", booking);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 创建新酒店
     */
    public ApiResponse<Object> createHotel(String name, String location, String description, Integer availableRooms) {
        try {
            Hotel hotel = hotelService.createHotel(name, location, description, availableRooms);
            return ApiResponse.success("酒店创建成功", hotel);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 更新酒店信息
     */
    public ApiResponse<Object> updateHotel(Hotel hotel) {
        try {
            boolean result = hotelService.updateHotel(hotel);
            String message = result ? "酒店信息更新成功" : "酒店信息更新失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
}
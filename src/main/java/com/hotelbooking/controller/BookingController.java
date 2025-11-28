package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.PaymentService;
import com.hotelbooking.service.RoomService;
import com.hotelbooking.exception.GlobalExceptionHandler;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookingController {
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final RoomService roomService;
    
    public BookingController(BookingService bookingService, PaymentService paymentService, RoomService roomService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.roomService = roomService;
    }
    
    /**
     * 创建预订
     */
    public ApiResponse<Object> createBooking(Integer userId, Integer hotelId, Integer roomId, 
                                            LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            Booking booking = bookingService.createBooking(userId, hotelId, roomId, checkInDate, checkOutDate);
            return ApiResponse.success("预订创建成功", booking);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 创建带支付的预订
     */
    public ApiResponse<Object> createBookingWithPayment(Integer userId, Integer hotelId, Integer roomId, 
                                                       LocalDate checkInDate, LocalDate checkOutDate, 
                                                       String paymentMethod) {
        try {
            Booking booking = bookingService.createBookingWithPayment(userId, hotelId, roomId, 
                                                                     checkInDate, checkOutDate, paymentMethod);
            return ApiResponse.success("带支付预订创建成功", booking);
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
     * 获取用户的所有预订
     */
    public ApiResponse<Object> getBookingsByUserId(Integer userId) {
        try {
            List<Booking> bookings = bookingService.getBookingsByUserId(userId);
            return ApiResponse.success("获取用户预订列表成功", bookings);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取酒店的所有预订
     */
    public ApiResponse<Object> getBookingsByHotelId(Integer hotelId) {
        try {
            List<Booking> bookings = bookingService.getBookingsByHotelId(hotelId);
            return ApiResponse.success("获取酒店预订列表成功", bookings);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 取消预订
     */
    public ApiResponse<Object> cancelBooking(Integer bookingId) {
        try {
            boolean result = bookingService.cancelBooking(bookingId);
            String message = result ? "预订取消成功" : "预订取消失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 取消预订并退款
     */
    public ApiResponse<Object> cancelBookingWithRefund(Integer bookingId) {
        try {
            boolean result = bookingService.cancelBookingWithRefund(bookingId);
            String message = result ? "取消并退款成功" : "取消并退款失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 完成预订（退房）
     */
    public ApiResponse<Object> completeBooking(Integer bookingId) {
        try {
            boolean result = bookingService.completeBooking(bookingId);
            String message = result ? "预订完成成功" : "预订完成失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 处理支付
     */
    public ApiResponse<Object> processPayment(String bookingId, BigDecimal amount, String paymentMethod) {
        try {
            boolean result = paymentService.processPayment(bookingId, amount, paymentMethod);
            String message = result ? "支付处理成功" : "支付处理失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 处理退款
     */
    public ApiResponse<Object> processRefund(String bookingId) {
        try {
            boolean result = paymentService.processRefund(bookingId);
            String message = result ? "退款处理成功" : "退款处理失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取支付状态
     */
    public ApiResponse<Object> getPaymentStatus(String bookingId) {
        try {
            String status = paymentService.getPaymentStatus(bookingId)
                    .map(payment -> payment.getPaymentStatus())
                    .orElse("NOT_FOUND");
            return ApiResponse.success("获取支付状态成功", status);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 计算应退金额
     */
    public ApiResponse<Object> calculateRefundAmount(String bookingId, LocalDate checkInDate) {
        try {
            BigDecimal refundAmount = paymentService.calculateRefundAmount(bookingId, checkInDate);
            return ApiResponse.success("计算退款金额成功", refundAmount);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 检查房间是否可用
     */
    public ApiResponse<Object> checkRoomAvailability(Integer roomId) {
        try {
            boolean available = roomService.isRoomAvailable(roomId);
            String message = available ? "房间可用" : "房间不可用";
            return ApiResponse.success(message, available);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 检查房间在指定时间段是否可用
     */
    public ApiResponse<Object> checkRoomAvailability(Integer roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            boolean available = bookingService.isRoomAvailable(roomId, checkInDate, checkOutDate);
            String message = available ? "房间在指定时间段可用" : "房间在指定时间段不可用";
            return ApiResponse.success(message, available);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取房间信息
     */
    public ApiResponse<Object> getRoomById(Integer roomId) {
        try {
            Room room = roomService.getRoomById(roomId)
                    .orElseThrow(() -> new RuntimeException("房间不存在，ID: " + roomId));
            return ApiResponse.success("获取房间信息成功", room);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取酒店的可用房间
     */
    public ApiResponse<Object> getAvailableRoomsByHotel(Integer hotelId) {
        try {
            List<Room> rooms = roomService.getAvailableRoomsByHotelId(hotelId);
            return ApiResponse.success("获取可用房间列表成功", rooms);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据价格范围获取可用房间
     */
    public ApiResponse<Object> getAvailableRoomsByPriceRange(Integer hotelId, BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            List<Room> rooms = roomService.getRoomsByPriceRange(minPrice, maxPrice).stream()
                    .filter(room -> room.getHotelId().equals(hotelId))
                    .toList();
            return ApiResponse.success("获取价格范围内房间成功", rooms);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据状态获取预订
     */
    public ApiResponse<Object> getBookingsByStatus(String status) {
        try {
            List<Booking> bookings = bookingService.getBookingsByStatus(status);
            return ApiResponse.success("获取状态预订列表成功", bookings);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 更新预订状态
     */
    public ApiResponse<Object> updateBookingStatus(Integer bookingId, String status) {
        try {
            boolean result = bookingService.updateBookingStatus(bookingId, status);
            String message = result ? "预订状态更新成功" : "预订状态更新失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 删除预订
     */
    public ApiResponse<Object> deleteBooking(Integer bookingId) {
        try {
            boolean result = bookingService.deleteBooking(bookingId);
            String message = result ? "预订删除成功" : "预订删除失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 验证预订日期
     */
    public ApiResponse<Object> validateBookingDates(LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            if (checkInDate == null || checkOutDate == null) {
                return ApiResponse.success("日期验证失败", false);
            }
            
            LocalDate today = LocalDate.now();
            boolean valid = !checkInDate.isBefore(today) && 
                           checkOutDate.isAfter(checkInDate) && 
                           checkInDate.isAfter(today.plusDays(1));
            
            String message = valid ? "日期验证成功" : "日期验证失败";
            return ApiResponse.success(message, valid);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
}
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
import java.util.ArrayList;
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
            Booking booking = bookingService.createBooking(userId, hotelId, roomId, checkInDate, checkOutDate)
                    .orElseThrow(() -> new RuntimeException("预订创建失败，可能是日期不合法或房间不可用"));

            return ApiResponse.success("预订创建成功", booking);
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
            return ApiResponse.success(result ? "预订取消成功" : "预订取消失败", result);
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
            return ApiResponse.success(result ? "预订完成成功" : "预订完成失败", result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 支付
     */
    public ApiResponse<Object> processPayment(String bookingId, BigDecimal amount, String paymentMethod) {
        try {
            boolean result = paymentService.processPayment(bookingId, amount, paymentMethod);
            return ApiResponse.success(result ? "支付处理成功" : "支付处理失败", result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 退款
     */
    public ApiResponse<Object> processRefund(String bookingId) {
        try {
            boolean result = paymentService.processRefund(bookingId);
            return ApiResponse.success(result ? "退款处理成功" : "退款处理失败", result);
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
                    .map(p -> p.getPaymentStatus())
                    .orElse("NOT_FOUND");

            return ApiResponse.success("获取支付状态成功", status);
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
        List<Room> rooms = roomService.getRoomsByPriceRange(minPrice, maxPrice);
        
        // 创建新的列表，避免修改原始列表
        List<Room> filteredRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (hotelId.equals(room.getHotelId())) {
                filteredRooms.add(room);
            }
        }
        
        return ApiResponse.success("获取价格范围内房间成功", filteredRooms);
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
            return ApiResponse.success(result ? "预订状态更新成功" : "预订状态更新失败", result);
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
            return ApiResponse.success(result ? "预订删除成功" : "预订删除失败", result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
}
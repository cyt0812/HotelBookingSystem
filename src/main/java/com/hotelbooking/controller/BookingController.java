package com.hotelbooking.controller;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.PaymentService;
import com.hotelbooking.service.RoomService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public Booking createBooking(Integer userId, Integer hotelId, Integer roomId, 
                               LocalDate checkInDate, LocalDate checkOutDate) {
        Optional<Booking> booking = bookingService.createBooking(userId, hotelId, roomId, checkInDate, checkOutDate);
        return booking.orElseThrow(() -> new RuntimeException("Failed to create booking"));
    }
    
    /**
     * 根据ID获取预订
     */
    public Booking getBookingById(Integer bookingId) {
        return bookingService.getBookingById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
    }
    
    /**
     * 获取用户的所有预订
     */
    public List<Booking> getBookingsByUserId(Integer userId) {
        return bookingService.getBookingsByUserId(userId);
    }
    
    /**
     * 获取酒店的所有预订
     */
    public List<Booking> getBookingsByHotelId(Integer hotelId) {
        return bookingService.getBookingsByHotelId(hotelId);
    }
    
    /**
     * 取消预订
     */
    public boolean cancelBooking(Integer bookingId) {
        return bookingService.cancelBooking(bookingId);
    }
    
    /**
     * 完成预订（退房）
     */
    public boolean completeBooking(Integer bookingId) {
        return bookingService.completeBooking(bookingId);
    }
    
    /**
     * 处理支付
     */
    public boolean processPayment(Integer bookingId, String cardNumber, String expiryDate, String cvv) {
        return paymentService.processPayment(bookingId, cardNumber, expiryDate, cvv);
    }
    
    /**
     * 处理退款
     */
    public boolean refundPayment(Integer bookingId) {
        return paymentService.refundPayment(bookingId);
    }
    
    /**
     * 获取预订金额
     */
    public BigDecimal getBookingAmount(Integer bookingId) {
        return paymentService.getBookingAmount(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking amount not found for id: " + bookingId));
    }
    
    /**
     * 计算应退金额
     */
    public BigDecimal calculateRefundAmount(Integer bookingId) {
        return paymentService.calculateRefundAmount(bookingId)
                .orElseThrow(() -> new RuntimeException("Cannot calculate refund amount for booking: " + bookingId));
    }
    
    /**
     * 检查房间是否可用
     */
    public boolean checkRoomAvailability(Integer roomId) {
        return roomService.isRoomAvailable(roomId);
    }
    
    /**
     * 获取房间信息
     */
    public Room getRoomById(Integer roomId) {
        return roomService.getRoomById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
    }
    
    /**
     * 获取酒店的可用房间
     */
    public List<Room> getAvailableRoomsByHotel(Integer hotelId) {
        return roomService.getAvailableRoomsByHotelId(hotelId);
    }
    
    /**
     * 根据状态获取预订
     */
    public List<Booking> getBookingsByStatus(String status) {
        return bookingService.getBookingsByStatus(status);
    }
    
    /**
     * 更新预订状态
     */
    public boolean updateBookingStatus(Integer bookingId, String status) {
        return bookingService.updateBookingStatus(bookingId, status);
    }
}
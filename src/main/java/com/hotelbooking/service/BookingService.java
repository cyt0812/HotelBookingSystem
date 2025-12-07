package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    
    public BookingService(BookingDAO bookingDAO, RoomDAO roomDAO) {
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
    }
    
    
    
    /**
<<<<<<< HEAD
     * Create booking
     */
    public Optional<Booking> createBooking(Integer userId, Integer hotelId, Integer roomId, 
                                         LocalDate checkInDate, LocalDate checkOutDate) {
        // Validate dates
=======
     * 创建预订
     */
    public Optional<Booking> createBooking(Integer userId, Integer hotelId, Integer roomId, 
                                         LocalDate checkInDate, LocalDate checkOutDate) {
        // 验证日期
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        if (checkInDate == null || checkOutDate == null || 
            checkInDate.isBefore(LocalDate.now()) || 
            !checkOutDate.isAfter(checkInDate)) {
            return Optional.empty();
        }
        
<<<<<<< HEAD
        // Check if room exists and is available
=======
        // 检查房间是否存在且可用
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        Optional<Room> roomOpt = roomDAO.getRoomById(roomId);
        if (roomOpt.isEmpty() || !roomOpt.get().isAvailable()) {
            return Optional.empty();
        }
        
        Room room = roomOpt.get();
        
<<<<<<< HEAD
        // Calculate total price
=======
        // 计算总价
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal roomPrice = BigDecimal.valueOf(room.getPricePerNight());
        BigDecimal totalPrice = roomPrice.multiply(BigDecimal.valueOf(numberOfNights));
        
<<<<<<< HEAD
        // Create booking
=======
        // 创建预订
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        Booking booking = new Booking(userId, hotelId, roomId, checkInDate, checkOutDate, 
                                    totalPrice, "CONFIRMED");
        
        Booking createdBooking = bookingDAO.createBooking(booking);
        
<<<<<<< HEAD
        // Update room status to unavailable
=======
        // 更新房间状态为不可用
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        room.setIsAvailable(false);
        roomDAO.updateRoom(room);
        
        return Optional.of(createdBooking);
    }
    
    /**
<<<<<<< HEAD
     * Get booking by ID
=======
     * 根据ID获取预订
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public Optional<Booking> getBookingById(Integer id) {
        return bookingDAO.getBookingById(id);
    }
    
    /**
<<<<<<< HEAD
     * Get all bookings by user ID
=======
     * 获取用户的所有预订
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public List<Booking> getBookingsByUserId(Integer userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }
    
    /**
<<<<<<< HEAD
     * Get all bookings by hotel ID
=======
     * 获取酒店的所有预订
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public List<Booking> getBookingsByHotelId(Integer hotelId) {
        return bookingDAO.getBookingsByHotelId(hotelId);
    }
    
    /**
<<<<<<< HEAD
     * Get bookings by status
=======
     * 根据状态获取预订
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public List<Booking> getBookingsByStatus(String status) {
        return bookingDAO.getBookingsByStatus(status);
    }
    
    /**
<<<<<<< HEAD
     * Cancel booking
=======
     * 取消预订
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public boolean cancelBooking(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
<<<<<<< HEAD
            // Update booking status
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
            
            if (updated) {
                // Restore room availability
=======
            // 更新预订状态
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
            
            if (updated) {
                // 恢复房间可用性
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
                Optional<Room> roomOpt = roomDAO.getRoomById(booking.getRoomId());
                if (roomOpt.isPresent()) {
                    Room room = roomOpt.get();
                    room.setIsAvailable(true);
                    roomDAO.updateRoom(room);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
<<<<<<< HEAD
     * Update booking status
=======
     * 更新预订状态
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public boolean updateBookingStatus(Integer bookingId, String status) {
        return bookingDAO.updateBookingStatus(bookingId, status);
    }
    
    /**
<<<<<<< HEAD
     * Delete booking
=======
     * 删除预订
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public boolean deleteBooking(Integer bookingId) {
        return bookingDAO.deleteBooking(bookingId);
    }
    
    /**
<<<<<<< HEAD
     * Complete booking (checkout)
=======
     * 完成预订（退房）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public boolean completeBooking(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
<<<<<<< HEAD
            // Update booking status
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "COMPLETED");
            
            if (updated) {
                // Restore room availability
=======
            // 更新预订状态
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "COMPLETED");
            
            if (updated) {
                // 恢复房间可用性
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
                Optional<Room> roomOpt = roomDAO.getRoomById(booking.getRoomId());
                if (roomOpt.isPresent()) {
                    Room room = roomOpt.get();
                    room.setIsAvailable(true);
                    roomDAO.updateRoom(room);
                }
                return true;
            }
        }
        return false;
    }
}
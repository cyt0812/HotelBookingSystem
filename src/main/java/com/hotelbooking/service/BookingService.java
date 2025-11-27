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
    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;
    
    public BookingService(BookingDAO bookingDAO, RoomDAO roomDAO) {
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
    }
    
    /**
     * 创建预订
     */
    public Optional<Booking> createBooking(Integer userId, Integer hotelId, Integer roomId, 
                                         LocalDate checkInDate, LocalDate checkOutDate) {
        // 验证日期
        if (checkInDate == null || checkOutDate == null || 
            checkInDate.isBefore(LocalDate.now()) || 
            !checkOutDate.isAfter(checkInDate)) {
            return Optional.empty();
        }
        
        // 检查房间是否存在且可用
        Optional<Room> roomOpt = roomDAO.getRoomById(roomId);
        if (roomOpt.isEmpty() || !roomOpt.get().isAvailable()) {
            return Optional.empty();
        }
        
        Room room = roomOpt.get();
        
        // 计算总价
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(numberOfNights));
        
        // 创建预订
        Booking booking = new Booking(userId, hotelId, roomId, checkInDate, checkOutDate, 
                                    totalPrice, "CONFIRMED");
        
        Booking createdBooking = bookingDAO.createBooking(booking);
        
        // 更新房间状态为不可用
        room.setAvailable(false);
        roomDAO.updateRoom(room);
        
        return Optional.of(createdBooking);
    }
    
    /**
     * 根据ID获取预订
     */
    public Optional<Booking> getBookingById(Integer id) {
        return bookingDAO.getBookingById(id);
    }
    
    /**
     * 获取用户的所有预订
     */
    public List<Booking> getBookingsByUserId(Integer userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }
    
    /**
     * 获取酒店的所有预订
     */
    public List<Booking> getBookingsByHotelId(Integer hotelId) {
        return bookingDAO.getBookingsByHotelId(hotelId);
    }
    
    /**
     * 根据状态获取预订
     */
    public List<Booking> getBookingsByStatus(String status) {
        return bookingDAO.getBookingsByStatus(status);
    }
    
    /**
     * 取消预订
     */
    public boolean cancelBooking(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
            // 更新预订状态
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
            
            if (updated) {
                // 恢复房间可用性
                Optional<Room> roomOpt = roomDAO.getRoomById(booking.getRoomId());
                if (roomOpt.isPresent()) {
                    Room room = roomOpt.get();
                    room.setAvailable(true);
                    roomDAO.updateRoom(room);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * 更新预订状态
     */
    public boolean updateBookingStatus(Integer bookingId, String status) {
        return bookingDAO.updateBookingStatus(bookingId, status);
    }
    
    /**
     * 删除预订
     */
    public boolean deleteBooking(Integer bookingId) {
        return bookingDAO.deleteBooking(bookingId);
    }
    
    /**
     * 完成预订（退房）
     */
    public boolean completeBooking(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
            // 更新预订状态
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "COMPLETED");
            
            if (updated) {
                // 恢复房间可用性
                Optional<Room> roomOpt = roomDAO.getRoomById(booking.getRoomId());
                if (roomOpt.isPresent()) {
                    Room room = roomOpt.get();
                    room.setAvailable(true);
                    roomDAO.updateRoom(room);
                }
                return true;
            }
        }
        return false;
    }
}
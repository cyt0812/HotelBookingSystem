package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingService {
    private final BookingDAO bookingDAO = new BookingDAO();
    private final RoomDAO roomDAO = new RoomDAO();

    // 创建预订（核心业务逻辑）
    public boolean makeBooking(Booking booking) {
        // 1. 检查房间在预定日期内是否可用
        if (!isRoomAvailable(booking.getRoomId(), booking.getCheckInDate(), booking.getCheckOutDate())) {
            System.out.println("房间在选定日期内不可用");
            return false;
        }

        // 2. 计算总价
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        if (nights <= 0) {
            System.out.println("入住日期必须早于退房日期");
            return false;
        }

        Room room = roomDAO.getRoomById(booking.getRoomId());
        if (room == null) {
            System.out.println("房间不存在");
            return false;
        }

        double totalPrice = nights * room.getPricePerNight();
        booking.setTotalPrice(totalPrice);

        // 3. 创建预订
        boolean success = bookingDAO.createBooking(booking);
        if (success) {
            System.out.println("预订成功！总价: ￥" + totalPrice + " (" + nights + "晚)");
        }
        return success;
    }

    // 检查房间可用性 - 改为 public
    public boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut) {
        // 验证日期
        if (checkIn.isBefore(LocalDate.now()) || checkOut.isBefore(checkIn)) {
            return false;
        }

        // 检查房间是否存在且可用
        Room room = roomDAO.getRoomById(roomId);
        if (room == null || !room.isAvailable()) {
            return false;
        }

        // 检查日期冲突
        List<Booking> conflicts = bookingDAO.getConflictingBookings(roomId, checkIn, checkOut);
        return conflicts.isEmpty();
    }

    // 获取用户的所有预订
    public List<Booking> getUserBookings(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    // 取消预订
    public boolean cancelBooking(int bookingId) {
        return bookingDAO.cancelBooking(bookingId);
    }

    // 根据ID获取预订详情
    public Booking getBookingById(int bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }

    // 计算预订价格（用于预览）- 添加这个方法
    public double calculateBookingPrice(int roomId, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) return 0.0;

        Room room = roomDAO.getRoomById(roomId);
        if (room == null) return 0.0;

        return nights * room.getPricePerNight();
    }
}
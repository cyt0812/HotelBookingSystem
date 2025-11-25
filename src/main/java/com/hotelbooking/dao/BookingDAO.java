package com.hotelbooking.dao;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // 创建预订
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, room_id, check_in_date, check_out_date, total_price, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getRoomId());
            ps.setDate(3, Date.valueOf(booking.getCheckInDate()));
            ps.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            ps.setDouble(5, booking.getTotalPrice());
            ps.setString(6, booking.getStatus());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("创建预订失败: " + e.getMessage());
            return false;
        }
    }

    // 根据用户ID获取预订
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Booking booking = extractBookingFromResultSet(rs);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("根据用户ID获取预订失败: " + e.getMessage());
        }
        return bookings;
    }

    // 根据房间ID和日期检查冲突预订
    public List<Booking> getConflictingBookings(int roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE room_id = ? AND status = 'CONFIRMED' " +
                    "AND ((check_in_date <= ? AND check_out_date > ?) OR " +
                    "(check_in_date < ? AND check_out_date >= ?) OR " +
                    "(check_in_date >= ? AND check_out_date <= ?))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            ps.setDate(2, Date.valueOf(checkOut));    // 修复：使用 Date.valueOf()
            ps.setDate(3, Date.valueOf(checkIn));     // 修复：使用 Date.valueOf()
            ps.setDate(4, Date.valueOf(checkOut));    // 修复：使用 Date.valueOf()
            ps.setDate(5, Date.valueOf(checkIn));     // 修复：使用 Date.valueOf()
            ps.setDate(6, Date.valueOf(checkIn));     // 修复：使用 Date.valueOf()
            ps.setDate(7, Date.valueOf(checkOut));    // 修复：使用 Date.valueOf()
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Booking booking = extractBookingFromResultSet(rs);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("检查冲突预订失败: " + e.getMessage());
        }
        return bookings;
    }

    // 根据预订ID获取预订
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("根据ID获取预订失败: " + e.getMessage());
        }
        return null;
    }

    // 取消预订
    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("取消预订失败: " + e.getMessage());
            return false;
        }
    }

    // 从ResultSet提取Booking对象
    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
        booking.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
        booking.setTotalPrice(rs.getDouble("total_price"));
        booking.setStatus(rs.getString("status"));
        booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return booking;
    }

    // 测试方法
    public static void main(String[] args) {
        BookingDAO bookingDAO = new BookingDAO();
        
        // 测试获取用户预订
        List<Booking> userBookings = bookingDAO.getBookingsByUserId(1);
        System.out.println("用户1的预订数量: " + userBookings.size());
        
        // 测试冲突检查
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        List<Booking> conflicts = bookingDAO.getConflictingBookings(1, checkIn, checkOut);
        System.out.println("冲突预订数量: " + conflicts.size());
    }
}
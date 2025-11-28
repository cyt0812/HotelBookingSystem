package com.hotelbooking.dao;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.util.DatabaseConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDAO {
    
    public Booking createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (booking_id, user_id, hotel_id, room_id, check_in_date, check_out_date, " +
                    "total_price, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, booking.getBookingId());
            stmt.setInt(2, booking.getUserId());
            stmt.setInt(3, booking.getHotelId());
            stmt.setInt(4, booking.getRoomId());
            stmt.setDate(5, Date.valueOf(booking.getCheckInDate()));
            stmt.setDate(6, Date.valueOf(booking.getCheckOutDate()));
            stmt.setBigDecimal(7, booking.getTotalPrice());
            stmt.setString(8, booking.getStatus());
            stmt.setTimestamp(9, Timestamp.valueOf(booking.getCreatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        booking.setId(rs.getInt(1));
                    }
                }
            }
            return booking;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating booking", e);
        }
    }

    public Optional<Booking> getBookingById(Integer id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBooking(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting booking by id: " + id, e);
        }
    }

    /**
     * 根据booking_id（字符串）获取预订
     */
    public Optional<Booking> getBookingByBookingId(String bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBooking(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting booking by booking id: " + bookingId, e);
        }
    }

    public List<Booking> getBookingsByUserId(Integer userId) {
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY created_at DESC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting bookings by user id: " + userId, e);
        }
    }

    public List<Booking> getBookingsByHotelId(Integer hotelId) {
        String sql = "SELECT * FROM bookings WHERE hotel_id = ? ORDER BY created_at DESC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting bookings by hotel id: " + hotelId, e);
        }
    }

    public List<Booking> getBookingsByStatus(String status) {
        String sql = "SELECT * FROM bookings WHERE status = ? ORDER BY created_at DESC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting bookings by status: " + status, e);
        }
    }

    /**
     * 根据房间ID获取预订列表
     */
    public List<Booking> getBookingsByRoomId(Integer roomId) {
        String sql = "SELECT * FROM bookings WHERE room_id = ? ORDER BY check_in_date ASC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting bookings by room id: " + roomId, e);
        }
    }

    /**
     * 检查房间在指定时间段是否有冲突的预订
     */
    public boolean hasBookingConflict(Integer roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE room_id = ? AND status NOT IN ('CANCELLED', 'COMPLETED') " +
                    "AND ((check_in_date BETWEEN ? AND ?) OR (check_out_date BETWEEN ? AND ?) " +
                    "OR (check_in_date <= ? AND check_out_date >= ?))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkInDate));
            stmt.setDate(3, Date.valueOf(checkOutDate.minusDays(1)));
            stmt.setDate(4, Date.valueOf(checkInDate.plusDays(1)));
            stmt.setDate(5, Date.valueOf(checkOutDate));
            stmt.setDate(6, Date.valueOf(checkInDate));
            stmt.setDate(7, Date.valueOf(checkOutDate));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error checking booking conflict for room: " + roomId, e);
        }
    }

    /**
     * 取消预订（更新状态为CANCELLED）
     */
    public boolean cancelBooking(String bookingId) {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, bookingId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error cancelling booking: " + bookingId, e);
        }
    }

    /**
     * 更新预订状态（使用booking_id）
     */
    public boolean updateBookingStatus(String bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setString(2, bookingId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking status: " + bookingId, e);
        }
    }

    /**
     * 更新预订总价
     */
    public boolean updateBookingPrice(Integer bookingId, BigDecimal newPrice) {
        String sql = "UPDATE bookings SET total_price = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, newPrice);
            stmt.setInt(2, bookingId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking price: " + bookingId, e);
        }
    }

    /**
     * 获取所有预订（分页查询）
     */
    public List<Booking> getAllBookings(int limit, int offset) {
        String sql = "SELECT * FROM bookings ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all bookings", e);
        }
    }

    /**
     * 获取用户最近的预订
     */
    public Optional<Booking> getLatestBookingByUserId(Integer userId) {
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBooking(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting latest booking for user: " + userId, e);
        }
    }

    /**
     * 更新预订状态（使用id）
     */
    public boolean updateBookingStatus(Integer bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking status: " + bookingId, e);
        }
    }

    public boolean deleteBooking(Integer id) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking: " + id, e);
        }
    }

    /**
     * 获取所有预订（无分页）
     */
    public List<Booking> getAllBookings() {
        String sql = "SELECT * FROM bookings ORDER BY created_at DESC";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all bookings", e);
        }
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setBookingId(rs.getString("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setHotelId(rs.getInt("hotel_id"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
        booking.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
        booking.setTotalPrice(rs.getBigDecimal("total_price"));
        booking.setStatus(rs.getString("status"));
        booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return booking;
    }
}
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
    stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            
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
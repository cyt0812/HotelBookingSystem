package com.hotelbooking.dao;

import com.hotelbooking.entity.Room;
import com.hotelbooking.util.DatabaseConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO {
    
    public Room createRoom(Room room) {
        String sql = "INSERT INTO rooms (hotel_id, room_number, room_type, price, available) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, room.getHotelId());
            stmt.setString(2, room.getRoomNumber());
            stmt.setString(3, room.getRoomType());
            stmt.setBigDecimal(4, room.getPrice());
            stmt.setBoolean(5, room.isAvailable());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        room.setId(rs.getInt(1));
                    }
                }
            }
            return room;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating room", e);
        }
    }

    public Optional<Room> getRoomById(Integer id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToRoom(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting room by id: " + id, e);
        }
    }

    public List<Room> getRoomsByHotelId(Integer hotelId) {
        String sql = "SELECT * FROM rooms WHERE hotel_id = ?";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting rooms by hotel id: " + hotelId, e);
        }
    }

    public List<Room> getAvailableRoomsByHotelId(Integer hotelId) {
        String sql = "SELECT * FROM rooms WHERE hotel_id = ? AND available = true";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting available rooms by hotel id: " + hotelId, e);
        }
    }

    public List<Room> getRoomsByType(String roomType) {
        String sql = "SELECT * FROM rooms WHERE room_type = ?";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting rooms by type: " + roomType, e);
        }
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET hotel_id = ?, room_number = ?, room_type = ?, price = ?, available = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, room.getHotelId());
            stmt.setString(2, room.getRoomNumber());
            stmt.setString(3, room.getRoomType());
            stmt.setBigDecimal(4, room.getPrice());
            stmt.setBoolean(5, room.isAvailable());
            stmt.setInt(6, room.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating room: " + room.getId(), e);
        }
    }

    public boolean deleteRoom(Integer id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting room: " + id, e);
        }
    }

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setHotelId(rs.getInt("hotel_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(rs.getString("room_type"));
        room.setPrice(rs.getBigDecimal("price"));
        room.setAvailable(rs.getBoolean("available"));
        return room;
    }
}
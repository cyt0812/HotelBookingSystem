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
        String sql = "INSERT INTO rooms (hotel_id, room_number, room_type, price, available, description) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, room.getHotelId());
            stmt.setString(2, room.getRoomNumber());
            stmt.setString(3, room.getRoomType());
            stmt.setBigDecimal(4, room.getPrice());
            stmt.setBoolean(5, room.isAvailable());
            stmt.setString(6, room.getDescription());
            
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
        String sql = "SELECT * FROM rooms WHERE hotel_id = ? ORDER BY room_number ASC";
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
        String sql = "SELECT * FROM rooms WHERE hotel_id = ? AND available = true ORDER BY room_number ASC";
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
        String sql = "SELECT * FROM rooms WHERE room_type = ? ORDER BY price ASC";
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

    /**
     * 根据价格范围查询房间
     */
    public List<Room> getRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT * FROM rooms WHERE price BETWEEN ? AND ? AND available = true ORDER BY price ASC";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, minPrice);
            stmt.setBigDecimal(2, maxPrice);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting rooms by price range: " + minPrice + " - " + maxPrice, e);
        }
    }

    /**
     * 获取酒店内特定类型的可用房间
     */
    public List<Room> getAvailableRoomsByHotelAndType(Integer hotelId, String roomType) {
        String sql = "SELECT * FROM rooms WHERE hotel_id = ? AND room_type = ? AND available = true ORDER BY price ASC";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            stmt.setString(2, roomType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting available rooms by hotel and type. Hotel: " + hotelId + ", Type: " + roomType, e);
        }
    }

    /**
     * 更新房间可用状态
     */
    public boolean updateRoomAvailability(Integer roomId, boolean available) {
        String sql = "UPDATE rooms SET available = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, available);
            stmt.setInt(2, roomId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating room availability: " + roomId, e);
        }
    }

    /**
     * 更新房间价格
     */
    public boolean updateRoomPrice(Integer roomId, BigDecimal newPrice) {
        String sql = "UPDATE rooms SET price = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, newPrice);
            stmt.setInt(2, roomId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating room price: " + roomId, e);
        }
    }

    /**
     * 检查房间号是否在酒店中已存在
     */
    public boolean isRoomNumberExists(Integer hotelId, String roomNumber) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE hotel_id = ? AND room_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            stmt.setString(2, roomNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error checking room number existence. Hotel: " + hotelId + ", Room: " + roomNumber, e);
        }
    }

    /**
     * 获取所有可用房间
     */
    public List<Room> getAllAvailableRooms() {
        String sql = "SELECT * FROM rooms WHERE available = true ORDER BY hotel_id, room_number ASC";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all available rooms", e);
        }
    }

    /**
     * 获取酒店的房间数量统计
     */
    public int getRoomCountByHotel(Integer hotelId) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting room count for hotel: " + hotelId, e);
        }
    }

    /**
     * 获取酒店的可用房间数量
     */
    public int getAvailableRoomCountByHotel(Integer hotelId) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE hotel_id = ? AND available = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting available room count for hotel: " + hotelId, e);
        }
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET hotel_id = ?, room_number = ?, room_type = ?, price = ?, available = ?, description = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, room.getHotelId());
            stmt.setString(2, room.getRoomNumber());
            stmt.setString(3, room.getRoomType());
            stmt.setBigDecimal(4, room.getPrice());
            stmt.setBoolean(5, room.isAvailable());
            stmt.setString(6, room.getDescription());
            stmt.setInt(7, room.getId());
            
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
        room.setDescription(rs.getString("description"));
        return room;
    }
}
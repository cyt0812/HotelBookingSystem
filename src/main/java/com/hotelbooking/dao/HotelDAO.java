package com.hotelbooking.dao;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelDAO {
    
    public Hotel createHotel(Hotel hotel) {
        String sql = "INSERT INTO hotels (name, location, description, available_rooms) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getLocation());
            stmt.setString(3, hotel.getDescription());
            stmt.setInt(4, hotel.getAvailableRooms());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        hotel.setId(rs.getInt(1));
                    }
                }
            }
            return hotel;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating hotel", e);
        }
    }

    public Optional<Hotel> getHotelById(Integer id) {
        String sql = "SELECT * FROM hotels WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToHotel(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting hotel by id: " + id, e);
        }
    }

    public List<Hotel> getAllHotels() {
        String sql = "SELECT * FROM hotels ORDER BY name ASC";
        List<Hotel> hotels = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("✅ 从数据库读取所有酒店...");
            
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }
            return hotels;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all hotels", e);
        }
    }

    public List<Hotel> getHotelsByLocation(String location) {
        String sql = "SELECT * FROM hotels WHERE LOWER(location) LIKE LOWER(?) ORDER BY name ASC";
        List<Hotel> hotels = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }
            return hotels;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting hotels by location: " + location, e);
        }
    }

    /**
     * 搜索酒店（按名称或位置）
     */
    public List<Hotel> searchHotels(String keyword) {
        System.out.println("✅ 已进入 HotelDAO.searchHotels");
        String sql = "SELECT * FROM hotels WHERE LOWER(name) LIKE LOWER(?) OR LOWER(location) LIKE LOWER(?) ORDER BY name ASC";
        List<Hotel> hotels = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + keyword + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }
            return hotels;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error searching hotels: " + keyword, e);
        }
    }

    /**
     * 更新酒店可用房间数量
     */
    public boolean updateAvailableRooms(Integer hotelId, int availableRooms) {
        String sql = "UPDATE hotels SET available_rooms = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, availableRooms);
            stmt.setInt(2, hotelId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating available rooms for hotel: " + hotelId, e);
        }
    }

    /**
     * 增加酒店可用房间数量
     */
    public boolean incrementAvailableRooms(Integer hotelId) {
        String sql = "UPDATE hotels SET available_rooms = available_rooms + 1 WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error incrementing available rooms for hotel: " + hotelId, e);
        }
    }

    /**
     * 减少酒店可用房间数量
     */
    public boolean decrementAvailableRooms(Integer hotelId) {
        String sql = "UPDATE hotels SET available_rooms = available_rooms - 1 WHERE id = ? AND available_rooms > 0";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hotelId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error decrementing available rooms for hotel: " + hotelId, e);
        }
    }

    public boolean updateHotel(Hotel hotel) {
        String sql = "UPDATE hotels SET name = ?, location = ?, description = ?, available_rooms = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getLocation());
            stmt.setString(3, hotel.getDescription());
            stmt.setInt(4, hotel.getAvailableRooms());
            stmt.setInt(5, hotel.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating hotel: " + hotel.getId(), e);
        }
    }

    public boolean deleteHotel(Integer id) {
        String sql = "DELETE FROM hotels WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting hotel: " + id, e);
        }
    }

    /**
     * 获取酒店数量统计
     */
    public int getHotelCount() {
        String sql = "SELECT COUNT(*) FROM hotels";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting hotel count", e);
        }
    }
    
    public List<Room> getRoomsByHotelId(int hotelId) {
        System.out.println("✅ 正在用 hotel_id 查询房间 = " + hotelId);
        String sql = "SELECT * FROM rooms WHERE hotel_id = ?";  // 根据 hotel_id 查询房间
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hotelId);  // 设置查询参数
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // 将结果集映射为 Room 对象并添加到列表中
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException("Error getting rooms for hotel: " + hotelId, e);
        }
    }

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
    // 使用有参构造函数
    int hotelId = rs.getInt("hotel_id");
    String roomNumber = rs.getString("room_number");
    String roomType = rs.getString("room_type");
    double pricePerNight = rs.getBigDecimal("price").doubleValue();
    
    // 检查是否有这些列，没有就使用默认值
    int maxOccupancy = 0;
    try {
        maxOccupancy = rs.getInt("max_occupancy");
    } catch (SQLException e) {
        maxOccupancy = 2; // 默认值
    }
    
    short availableValue = rs.getShort("available");
    boolean isAvailable = availableValue == 1;
    
    String description = "";
    try {
        description = rs.getString("description");
    } catch (SQLException e) {
        description = "";
    }
    
    // 使用7个参数的构造函数
    Room room = new Room(hotelId, roomNumber, roomType, pricePerNight, 
                        maxOccupancy, isAvailable, description);
    
    // 设置ID
    room.setId(rs.getInt("id"));
    
    return room;
}

    private Hotel mapResultSetToHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(rs.getInt("id"));
        hotel.setName(rs.getString("name"));
        hotel.setLocation(rs.getString("location"));
        hotel.setDescription(rs.getString("description"));
        hotel.setAvailableRooms(rs.getInt("available_rooms"));
        return hotel;
    }
}
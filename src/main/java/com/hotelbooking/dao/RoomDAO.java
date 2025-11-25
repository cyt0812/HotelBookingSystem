package com.hotelbooking.dao;

import com.hotelbooking.entity.Room;
import com.hotelbooking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // 获取所有房间
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = extractRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("获取房间列表失败: " + e.getMessage());
        }
        return rooms;
    }

    // 根据酒店ID获取房间
    public List<Room> getRoomsByHotelId(int hotelId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, hotelId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Room room = extractRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("根据酒店ID获取房间失败: " + e.getMessage());
        }
        return rooms;
    }

    // 根据房间ID获取房间
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractRoomFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("根据ID获取房间失败: " + e.getMessage());
        }
        return null;
    }

    // 获取可用房间
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE is_available = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = extractRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("获取可用房间失败: " + e.getMessage());
        }
        return rooms;
    }

    // 添加新房间
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms (hotel_id, room_number, room_type, price_per_night, max_occupancy, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, room.getHotelId());
            ps.setString(2, room.getRoomNumber());
            ps.setString(3, room.getRoomType());
            ps.setDouble(4, room.getPricePerNight());
            ps.setInt(5, room.getMaxOccupancy());
            ps.setBoolean(6, room.isAvailable());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("添加房间失败: " + e.getMessage());
            return false;
        }
    }

    // 更新房间可用状态
    public boolean updateRoomAvailability(int roomId, boolean isAvailable) {
        String sql = "UPDATE rooms SET is_available = ? WHERE room_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBoolean(1, isAvailable);
            ps.setInt(2, roomId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("更新房间状态失败: " + e.getMessage());
            return false;
        }
    }

    // 从ResultSet提取Room对象
    private Room extractRoomFromResultSet(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setHotelId(rs.getInt("hotel_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(rs.getString("room_type"));
        room.setPricePerNight(rs.getDouble("price_per_night"));
        room.setMaxOccupancy(rs.getInt("max_occupancy"));
        room.setAvailable(rs.getBoolean("is_available"));
        return room;
    }

    // 测试方法
    public static void main(String[] args) {
        RoomDAO roomDAO = new RoomDAO();
        
        // 测试获取所有房间
        List<Room> rooms = roomDAO.getAllRooms();
        System.out.println("房间数量: " + rooms.size());
        for (Room room : rooms) {
            System.out.println("房间: " + room.getRoomNumber() + " - " + room.getRoomType() + " - ￥" + room.getPricePerNight());
        }
        
        // 测试根据酒店ID获取房间
        List<Room> hotelRooms = roomDAO.getRoomsByHotelId(1);
        System.out.println("酒店1的房间数量: " + hotelRooms.size());
    }
}
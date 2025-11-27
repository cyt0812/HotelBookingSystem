package com.hotelbooking.dao;

import com.hotelbooking.entity.Hotel;
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
        String sql = "SELECT * FROM hotels";
        List<Hotel> hotels = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }
            return hotels;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all hotels", e);
        }
    }

    public List<Hotel> getHotelsByLocation(String location) {
        String sql = "SELECT * FROM hotels WHERE location = ?";
        List<Hotel> hotels = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }
            return hotels;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting hotels by location: " + location, e);
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
            int affectedRooms = stmt.executeUpdate();
            return affectedRooms > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting hotel: " + id, e);
        }
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
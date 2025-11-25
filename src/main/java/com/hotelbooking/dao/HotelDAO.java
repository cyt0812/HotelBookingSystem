package com.hotelbooking.dao;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {
    
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Hotel hotel = new Hotel();
                hotel.setHotelId(rs.getInt("hotel_id"));
                hotel.setName(rs.getString("name"));
                hotel.setAddress(rs.getString("address"));
                hotel.setDescription(rs.getString("description"));
                hotel.setAmenities(rs.getString("amenities"));
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }
    
    public Hotel getHotelById(int hotelId) {
        String sql = "SELECT * FROM hotels WHERE hotel_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, hotelId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Hotel hotel = new Hotel();
                hotel.setHotelId(rs.getInt("hotel_id"));
                hotel.setName(rs.getString("name"));
                hotel.setAddress(rs.getString("address"));
                hotel.setDescription(rs.getString("description"));
                hotel.setAmenities(rs.getString("amenities"));
                return hotel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addHotel(Hotel hotel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
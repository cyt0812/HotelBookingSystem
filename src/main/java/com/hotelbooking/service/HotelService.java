package com.hotelbooking.service;

import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.entity.Hotel;
import java.util.List;
import java.util.Optional;

public class HotelService {
    private final HotelDAO hotelDAO;
    
    public HotelService(HotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO;
    }
    
    /**
     * 创建酒店
     */
    public Hotel createHotel(String name, String location, String description, Integer availableRooms) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel name cannot be empty");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        if (availableRooms == null || availableRooms < 0) {
            throw new IllegalArgumentException("Available rooms must be non-negative");
        }
        
        Hotel hotel = new Hotel(name.trim(), location.trim(), description, availableRooms);
        return hotelDAO.createHotel(hotel);
    }
    
    /**
     * 根据ID获取酒店
     */
    public Optional<Hotel> getHotelById(Integer id) {
        return hotelDAO.getHotelById(id);
    }
    
    /**
     * 获取所有酒店
     */
    public List<Hotel> getAllHotels() {
        return hotelDAO.getAllHotels();
    }
    
    /**
     * 根据位置获取酒店
     */
    public List<Hotel> getHotelsByLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return List.of(); // 返回空列表而不是抛出异常
        }
        return hotelDAO.getHotelsByLocation(location.trim());
    }
    
    /**
     * 更新酒店信息
     */
    public boolean updateHotel(Hotel hotel) {
        if (hotel == null || hotel.getId() == null) {
            return false;
        }
        return hotelDAO.updateHotel(hotel);
    }
    
    /**
     * 删除酒店
     */
    public boolean deleteHotel(Integer hotelId) {
        return hotelDAO.deleteHotel(hotelId);
    }
}
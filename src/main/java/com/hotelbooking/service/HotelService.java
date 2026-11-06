// HotelService.java - Complete fixed version
package com.hotelbooking.service;

import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelService {
    
    private final HotelDAO hotelDAO;
    private volatile List<Hotel> hotelCache; // Use volatile to ensure visibility
    
    // Default constructor
    public HotelService() {
        this.hotelDAO = new HotelDAO();
        this.hotelCache = null; // Lazy initialization
    }
    
    // Support dependency injection
    public HotelService(HotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO != null ? hotelDAO : new HotelDAO();
        this.hotelCache = null; // Lazy initialization
    }
    
    /**
     * Get hotel cache (lazy loading)
     */
    private List<Hotel> getHotelCache() {
        if (hotelCache == null) {
            synchronized (this) {
                if (hotelCache == null) {
                    hotelCache = hotelDAO.getAllHotels();
                }
            }
        }
        return hotelCache;
    }
    
    /**
     * Clear cache (used after testing or data updates)
     */
    public void clearCache() {
        synchronized (this) {
            hotelCache = null;
        }
    }
    
    /**
     * Create hotel
     */
    public Hotel createHotel(String name, String location, String description, String amenities, Integer availableRooms) {
        try {
            // Parameter validation
            validateHotelParameters(name, location, availableRooms);
            
            Hotel hotel = new Hotel(name.trim(), location.trim(), description, amenities, availableRooms);
            Hotel createdHotel = hotelDAO.createHotel(hotel);
            
            // Clear cache after successful creation
            clearCache();
            
            return createdHotel;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to create hotel: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parameter validation
     */
    private void validateHotelParameters(String name, String location, Integer availableRooms) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Hotel name cannot be empty");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new ValidationException("Location cannot be empty");
        }
        if (availableRooms == null || availableRooms < 0) {
            throw new ValidationException("Available rooms must be non-negative");
        }
    }
    
    /**
     * Get hotel by ID
     */
    public Optional<Hotel> getHotelById(Integer id) {
        try {
            return hotelDAO.getHotelById(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get hotel information: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all hotels
     */
    public List<Hotel> getAllHotels() {
        try {
            return hotelDAO.getAllHotels();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get hotel list: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get hotels by location
     */
    public List<Hotel> getHotelsByLocation(String location) {
        try {
            if (location == null || location.trim().isEmpty()) {
                return List.of();
            }
            return hotelDAO.getHotelsByLocation(location.trim());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get hotels by location: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update hotel information
     */
    public boolean updateHotel(Hotel hotel) {
        try {
            if (hotel == null || hotel.getId() == null) {
                return false;
            }
            boolean result = hotelDAO.updateHotel(hotel);
            if (result) {
                clearCache(); // Clear cache after successful update
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to update hotel information: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete hotel
     */
    public boolean deleteHotel(Integer hotelId) {
        try {
            boolean result = hotelDAO.deleteHotel(hotelId);
            if (result) {
                clearCache(); // Clear cache after successful deletion
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to delete hotel: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search hotels
     */
    public List<Hotel> searchHotels(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllHotels();
            }
            return hotelDAO.searchHotels(keyword.trim());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR,
                "Failed to search hotels: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get rooms by hotel ID
     */
    public List<Room> getRoomsByHotelId(int hotelId) {
        try {
            return hotelDAO.getRoomsByHotelId(hotelId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get rooms by hotel ID: " + e.getMessage(), e);
        }
    }
}
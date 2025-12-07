<<<<<<< HEAD
// HotelService.java - Complete fixed version
=======
// HotelService.java - 完整修复版本
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
<<<<<<< HEAD
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
=======
    private volatile List<Hotel> hotelCache; // 使用 volatile 保证可见性
    
    // 默认构造
    public HotelService() {
        this.hotelDAO = new HotelDAO();
        this.hotelCache = null; // 延迟初始化
    }
    
    // 支持依赖注入
    public HotelService(HotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO != null ? hotelDAO : new HotelDAO();
        this.hotelCache = null; // 延迟初始化
    }
    
    /**
     * 获取酒店缓存（懒加载）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
<<<<<<< HEAD
     * Clear cache (used after testing or data updates)
=======
     * 清空缓存（用于测试或数据更新后）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public void clearCache() {
        synchronized (this) {
            hotelCache = null;
        }
    }
    
    /**
<<<<<<< HEAD
     * Create hotel
     */
    public Hotel createHotel(String name, String location, String description, String amenities, Integer availableRooms) {
        try {
            // Parameter validation
=======
     * 创建酒店
     */
    public Hotel createHotel(String name, String location, String description, String amenities, Integer availableRooms) {
        try {
            // 参数验证
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            validateHotelParameters(name, location, availableRooms);
            
            Hotel hotel = new Hotel(name.trim(), location.trim(), description, amenities, availableRooms);
            Hotel createdHotel = hotelDAO.createHotel(hotel);
            
<<<<<<< HEAD
            // Clear cache after successful creation
=======
            // 创建成功后清空缓存
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            clearCache();
            
            return createdHotel;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Failed to create hotel: " + e.getMessage(), e);
=======
                "创建酒店失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
<<<<<<< HEAD
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
=======
     * 参数验证
     */
    private void validateHotelParameters(String name, String location, Integer availableRooms) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("酒店名称不能为空");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new ValidationException("位置不能为空");
        }
        if (availableRooms == null || availableRooms < 0) {
            throw new ValidationException("可用房间数必须为非负数");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
<<<<<<< HEAD
     * Get hotel by ID
=======
     * 根据ID获取酒店
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public Optional<Hotel> getHotelById(Integer id) {
        try {
            return hotelDAO.getHotelById(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Failed to get hotel information: " + e.getMessage(), e);
=======
                "获取酒店信息失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
<<<<<<< HEAD
     * Get all hotels
=======
     * 获取所有酒店
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public List<Hotel> getAllHotels() {
        try {
            return hotelDAO.getAllHotels();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Failed to get hotel list: " + e.getMessage(), e);
=======
                "获取酒店列表失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
<<<<<<< HEAD
     * Get hotels by location
=======
     * 根据位置获取酒店
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public List<Hotel> getHotelsByLocation(String location) {
        try {
            if (location == null || location.trim().isEmpty()) {
                return List.of();
            }
            return hotelDAO.getHotelsByLocation(location.trim());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Failed to get hotels by location: " + e.getMessage(), e);
=======
                "根据位置获取酒店失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
<<<<<<< HEAD
     * Update hotel information
=======
     * 更新酒店信息
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public boolean updateHotel(Hotel hotel) {
        try {
            if (hotel == null || hotel.getId() == null) {
                return false;
            }
            boolean result = hotelDAO.updateHotel(hotel);
            if (result) {
<<<<<<< HEAD
                clearCache(); // Clear cache after successful update
=======
                clearCache(); // 更新成功后清空缓存
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Failed to update hotel information: " + e.getMessage(), e);
=======
                "更新酒店信息失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
<<<<<<< HEAD
     * Delete hotel
=======
     * 删除酒店
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public boolean deleteHotel(Integer hotelId) {
        try {
            boolean result = hotelDAO.deleteHotel(hotelId);
            if (result) {
<<<<<<< HEAD
                clearCache(); // Clear cache after successful deletion
=======
                clearCache(); // 删除成功后清空缓存
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Failed to delete hotel: " + e.getMessage(), e);
=======
                "删除酒店失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
<<<<<<< HEAD
     * Search hotels
=======
     * 搜索酒店
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public List<Hotel> searchHotels(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllHotels();
            }
            return hotelDAO.searchHotels(keyword.trim());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR,
<<<<<<< HEAD
                "Failed to search hotels: " + e.getMessage(), e);
=======
                "搜索酒店失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
<<<<<<< HEAD
     * Get rooms by hotel ID
=======
     * 根据酒店ID获取房间
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public List<Room> getRoomsByHotelId(int hotelId) {
        try {
            return hotelDAO.getRoomsByHotelId(hotelId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Failed to get rooms by hotel ID: " + e.getMessage(), e);
=======
                "根据酒店ID获取房间失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
}
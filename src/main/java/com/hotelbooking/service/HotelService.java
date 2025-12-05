// HotelService.java - 完整修复版本
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
     * 清空缓存（用于测试或数据更新后）
     */
    public void clearCache() {
        synchronized (this) {
            hotelCache = null;
        }
    }
    
    /**
     * 创建酒店
     */
    public Hotel createHotel(String name, String location, String description, String amenities, Integer availableRooms) {
        try {
            // 参数验证
            validateHotelParameters(name, location, availableRooms);
            
            Hotel hotel = new Hotel(name.trim(), location.trim(), description, amenities, availableRooms);
            Hotel createdHotel = hotelDAO.createHotel(hotel);
            
            // 创建成功后清空缓存
            clearCache();
            
            return createdHotel;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "创建酒店失败: " + e.getMessage(), e);
        }
    }
    
    /**
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
        }
    }
    
    /**
     * 根据ID获取酒店
     */
    public Optional<Hotel> getHotelById(Integer id) {
        try {
            return hotelDAO.getHotelById(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取酒店信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取所有酒店
     */
    public List<Hotel> getAllHotels() {
        try {
            return hotelDAO.getAllHotels();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取酒店列表失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据位置获取酒店
     */
    public List<Hotel> getHotelsByLocation(String location) {
        try {
            if (location == null || location.trim().isEmpty()) {
                return List.of();
            }
            return hotelDAO.getHotelsByLocation(location.trim());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "根据位置获取酒店失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新酒店信息
     */
    public boolean updateHotel(Hotel hotel) {
        try {
            if (hotel == null || hotel.getId() == null) {
                return false;
            }
            boolean result = hotelDAO.updateHotel(hotel);
            if (result) {
                clearCache(); // 更新成功后清空缓存
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "更新酒店信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除酒店
     */
    public boolean deleteHotel(Integer hotelId) {
        try {
            boolean result = hotelDAO.deleteHotel(hotelId);
            if (result) {
                clearCache(); // 删除成功后清空缓存
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "删除酒店失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 搜索酒店
     */
    public List<Hotel> searchHotels(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllHotels();
            }
            return hotelDAO.searchHotels(keyword.trim());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR,
                "搜索酒店失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据酒店ID获取房间
     */
    public List<Room> getRoomsByHotelId(int hotelId) {
        try {
            return hotelDAO.getRoomsByHotelId(hotelId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "根据酒店ID获取房间失败: " + e.getMessage(), e);
        }
    }
}
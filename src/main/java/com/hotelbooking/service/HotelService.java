package com.hotelbooking.service;

import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
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
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new ValidationException("酒店名称不能为空");
            }
            if (location == null || location.trim().isEmpty()) {
                throw new ValidationException("位置不能为空");
            }
            if (availableRooms == null || availableRooms < 0) {
                throw new ValidationException("可用房间数必须为非负数");
            }
            
            Hotel hotel = new Hotel(name.trim(), location.trim(), description, availableRooms);
            return hotelDAO.createHotel(hotel);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "创建酒店失败: " + e.getMessage(), e);
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
                return List.of(); // 返回空列表而不是抛出异常
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
            return hotelDAO.updateHotel(hotel);
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
            return hotelDAO.deleteHotel(hotelId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "删除酒店失败: " + e.getMessage(), e);
        }
    }
}
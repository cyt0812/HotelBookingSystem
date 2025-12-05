package com.hotelbooking.service;

import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Room;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class RoomService {
    private RoomDAO roomDAO;
    
    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }
    
    /**
     * 创建房间
     */
    public Room createRoom(Integer hotelId, String roomNumber, String roomType, 
                      BigDecimal price, boolean available) {
    try {
        if (hotelId == null) {
            throw new ValidationException("酒店ID不能为空");
        }
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new ValidationException("房间号不能为空");
        }
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new ValidationException("房间类型不能为空");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("价格必须为正数");
        }
        
        // 检查房间号是否已存在
        if (roomDAO.isRoomNumberExists(hotelId, roomNumber)) {
            throw new BusinessException(ErrorType.ROOM_ALREADY_EXISTS);
        }
        
        int defaultMaxOccupancy = 2;
        String defaultDescription = roomType.trim() + " room"; // 添加默认描述
        
        // 使用7个参数的构造函数
        Room room = new Room(hotelId, roomNumber.trim(), roomType.trim(), 
                            price.doubleValue(), defaultMaxOccupancy, 
                            available, defaultDescription);
        
        return roomDAO.createRoom(room);
    } catch (BusinessException | ValidationException e) {
        throw e;
    } catch (Exception e) {
        throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
            "创建房间失败: " + e.getMessage(), e);
    }
}
    
    /**
     * 根据ID获取房间
     */
    public Optional<Room> getRoomById(Integer id) {
        try {
            return roomDAO.getRoomById(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取房间信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取酒店的所有房间
     */
    public List<Room> getRoomsByHotelId(Integer hotelId) {
        try {
             if (hotelId == null || hotelId <= 0) {
            throw new ValidationException("酒店ID不能为空");
        }
            return roomDAO.getRoomsByHotelId(hotelId);
        } catch (ValidationException e) {
        throw e;
    }catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取酒店房间列表失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取酒店的可用房间
     */
    public List<Room> getAvailableRoomsByHotelId(Integer hotelId) {
        try {
               if (hotelId == null || hotelId <= 0) {
            throw new ValidationException("酒店ID不能为空");
        }
            return roomDAO.getAvailableRoomsByHotelId(hotelId);
        } catch (ValidationException e) {
        throw e;
    } catch (Exception e) {
          throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
            "获取可用房间列表失败: " + e.getMessage(), e);
    }
    }
    
    /**
     * 根据房型获取房间
     */
    public List<Room> getRoomsByType(String roomType) {
        try {
            if (roomType == null || roomType.trim().isEmpty()) {
                return List.of();
            }
            return roomDAO.getRoomsByType(roomType.trim());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "根据房型获取房间失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据价格范围获取房间
     */
    public List<Room> getRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            if (minPrice == null || maxPrice == null) {
                throw new ValidationException("价格范围不能为空");
            }
            if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("价格不能为负数");
            }
            if (minPrice.compareTo(maxPrice) > 0) {
                throw new ValidationException("最低价格不能大于最高价格");
            }
            
            return roomDAO.getRoomsByPriceRange(minPrice, maxPrice);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "根据价格范围获取房间失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取酒店内特定类型的可用房间
     */
    public List<Room> getAvailableRoomsByHotelAndType(Integer hotelId, String roomType) {
        try {
            if (hotelId == null) {
                throw new ValidationException("酒店ID不能为空");
            }
            if (roomType == null || roomType.trim().isEmpty()) {
                return List.of();
            }
            
            return roomDAO.getAvailableRoomsByHotelAndType(hotelId, roomType.trim());
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取特定类型可用房间失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新房间价格
     */
    public boolean updateRoomPrice(Integer roomId, BigDecimal newPrice) {
        try {
            if (roomId == null) {
                throw new ValidationException("房间ID不能为空");
            }
            if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("价格必须为正数");
            }
            
            return roomDAO.updateRoomPrice(roomId, newPrice);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "更新房间价格失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检查房间号是否在酒店中已存在
     */
    public boolean isRoomNumberExists(Integer hotelId, String roomNumber) {
        try {
            if (hotelId == null) {
                throw new ValidationException("酒店ID不能为空");
            }
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                throw new ValidationException("房间号不能为空");
            }
            
            return roomDAO.isRoomNumberExists(hotelId, roomNumber.trim());
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "检查房间号是否存在失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取所有可用房间
     */
    public List<Room> getAllAvailableRooms() {
        try {
            return roomDAO.getAllAvailableRooms();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取所有可用房间失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取酒店的房间数量统计
     */
    public int getRoomCountByHotel(Integer hotelId) {
        try {
            if (hotelId == null) {
                throw new ValidationException("酒店ID不能为空");
            }
            return roomDAO.getRoomCountByHotel(hotelId);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取房间数量统计失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取酒店的可用房间数量
     */
    public int getAvailableRoomCountByHotel(Integer hotelId) {
        try {
            if (hotelId == null) {
                throw new ValidationException("酒店ID不能为空");
            }
            return roomDAO.getAvailableRoomCountByHotel(hotelId);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取可用房间数量失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新房间信息
     */
    public boolean updateRoom(Room room) {
        try {
            if (room == null || room.getRoomId() == 0) {
                return false;
            }
            return roomDAO.updateRoom(room);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "更新房间信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新房间可用状态
     */
    public boolean updateRoomAvailability(Integer roomId, boolean available) {
        try {
            Optional<Room> roomOpt = roomDAO.getRoomById(roomId);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setIsAvailable(available);
                return roomDAO.updateRoom(room);
            }
            return false;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "更新房间可用状态失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除房间
     */
    public boolean deleteRoom(Integer roomId) {
        try {
            return roomDAO.deleteRoom(roomId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "删除房间失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检查房间是否可用
     */
    public boolean isRoomAvailable(Integer roomId) {
        try {
             if (roomId == null || roomId <= 0) {
            throw new ValidationException("房间ID不能为空或无效");
        }
            Optional<Room> room = roomDAO.getRoomById(roomId);
              if (room.isEmpty()) {
            throw new BusinessException(ErrorType.ROOM_NOT_FOUND);
        }
            return room.isPresent() && room.get().isAvailable();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "检查房间可用性失败: " + e.getMessage(), e);
        }
    }
}
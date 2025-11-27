package com.hotelbooking.service;

import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Room;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class RoomService {
    private final RoomDAO roomDAO;
    
    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }
    
    /**
     * 创建房间
     */
    public Room createRoom(Integer hotelId, String roomNumber, String roomType, 
                          BigDecimal price, boolean available) {
        if (hotelId == null) {
            throw new IllegalArgumentException("Hotel ID cannot be null");
        }
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be empty");
        }
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new IllegalArgumentException("Room type cannot be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        
        Room room = new Room(hotelId, roomNumber.trim(), roomType.trim(), price, available);
        return roomDAO.createRoom(room);
    }
    
    /**
     * 根据ID获取房间
     */
    public Optional<Room> getRoomById(Integer id) {
        return roomDAO.getRoomById(id);
    }
    
    /**
     * 获取酒店的所有房间
     */
    public List<Room> getRoomsByHotelId(Integer hotelId) {
        return roomDAO.getRoomsByHotelId(hotelId);
    }
    
    /**
     * 获取酒店的可用房间
     */
    public List<Room> getAvailableRoomsByHotelId(Integer hotelId) {
        return roomDAO.getAvailableRoomsByHotelId(hotelId);
    }
    
    /**
     * 根据房型获取房间
     */
    public List<Room> getRoomsByType(String roomType) {
        if (roomType == null || roomType.trim().isEmpty()) {
            return List.of();
        }
        return roomDAO.getRoomsByType(roomType.trim());
    }
    
    /**
     * 更新房间信息
     */
    public boolean updateRoom(Room room) {
        if (room == null || room.getId() == null) {
            return false;
        }
        return roomDAO.updateRoom(room);
    }
    
    /**
     * 更新房间可用状态
     */
    public boolean updateRoomAvailability(Integer roomId, boolean available) {
        Optional<Room> roomOpt = roomDAO.getRoomById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setAvailable(available);
            return roomDAO.updateRoom(room);
        }
        return false;
    }
    
    /**
     * 删除房间
     */
    public boolean deleteRoom(Integer roomId) {
        return roomDAO.deleteRoom(roomId);
    }
    
    /**
     * 检查房间是否可用
     */
    public boolean isRoomAvailable(Integer roomId) {
        Optional<Room> room = roomDAO.getRoomById(roomId);
        return room.isPresent() && room.get().isAvailable();
    }
}
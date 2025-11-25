package com.hotelbooking.service;

import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Room;
import java.util.List;

public class RoomService {
    private final RoomDAO roomDAO = new RoomDAO();

    // 获取所有房间
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    // 根据ID获取房间详情
    public Room getRoomById(int roomId) {
        return roomDAO.getRoomById(roomId);
    }

    // 获取可用房间列表
    public List<Room> getAvailableRooms() {
        return roomDAO.getAvailableRooms();
    }

    // 根据酒店ID获取房间
    public List<Room> getRoomsByHotel(int hotelId) {
        return roomDAO.getRoomsByHotelId(hotelId);
    }

    // 添加新房间
    public boolean addRoom(Room room) {
        return roomDAO.addRoom(room);
    }

    // 更新房间状态
    public boolean updateRoomAvailability(int roomId, boolean isAvailable) {
        return roomDAO.updateRoomAvailability(roomId, isAvailable);
    }

    // 根据房型搜索房间
    public List<Room> searchRoomsByType(String roomType) {
        List<Room> allRooms = roomDAO.getAllRooms();
        return allRooms.stream()
                .filter(room -> room.getRoomType().equalsIgnoreCase(roomType))
                .filter(Room::isAvailable)
                .toList();
    }
}
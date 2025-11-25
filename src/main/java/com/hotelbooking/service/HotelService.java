package com.hotelbooking.service;

import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import java.util.List;

public class HotelService {
    private final HotelDAO hotelDAO = new HotelDAO();
    private final RoomDAO roomDAO = new RoomDAO();

    // 获取所有酒店列表
    public List<Hotel> getAllHotels() {
        return hotelDAO.getAllHotels();
    }

    // 根据ID获取酒店详情
    public Hotel getHotelById(int hotelId) {
        return hotelDAO.getHotelById(hotelId);
    }

    // 获取酒店的所有房间
    public List<Room> getHotelRooms(int hotelId) {
        return roomDAO.getRoomsByHotelId(hotelId);
    }

    // 搜索可用房间
    public List<Room> searchAvailableRooms(int hotelId) {
        List<Room> allRooms = roomDAO.getRoomsByHotelId(hotelId);
        // 过滤出可用房间
        return allRooms.stream()
                .filter(Room::isAvailable)
                .toList();
    }

    // 添加新酒店
    public boolean addHotel(Hotel hotel) {
        return hotelDAO.addHotel(hotel);
    }
}
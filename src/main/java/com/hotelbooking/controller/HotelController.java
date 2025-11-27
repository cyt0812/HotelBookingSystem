package com.hotelbooking.controller;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.service.RoomService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class HotelController {
    private final HotelService hotelService;
    private final RoomService roomService;
    
    public HotelController(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }
    
    /**
     * 创建酒店
     */
    public Hotel createHotel(String name, String location, String description, Integer availableRooms) {
        return hotelService.createHotel(name, location, description, availableRooms);
    }
    
    /**
     * 根据ID获取酒店
     */
    public Hotel getHotelById(Integer hotelId) {
        return hotelService.getHotelById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));
    }
    
    /**
     * 获取所有酒店
     */
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }
    
    /**
     * 根据位置获取酒店
     */
    public List<Hotel> getHotelsByLocation(String location) {
        return hotelService.getHotelsByLocation(location);
    }
    
    /**
     * 更新酒店信息
     */
    public boolean updateHotel(Hotel hotel) {
        return hotelService.updateHotel(hotel);
    }
    
    /**
     * 删除酒店
     */
    public boolean deleteHotel(Integer hotelId) {
        return hotelService.deleteHotel(hotelId);
    }
    
    /**
     * 创建房间
     */
    public Room createRoom(Integer hotelId, String roomNumber, String roomType, 
                          BigDecimal price, boolean available) {
        return roomService.createRoom(hotelId, roomNumber, roomType, price, available);
    }
    
    /**
     * 根据ID获取房间
     */
    public Room getRoomById(Integer roomId) {
        return roomService.getRoomById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
    }
    
    /**
     * 获取酒店的所有房间
     */
    public List<Room> getRoomsByHotel(Integer hotelId) {
        return roomService.getRoomsByHotelId(hotelId);
    }
    
    /**
     * 获取酒店的可用房间
     */
    public List<Room> getAvailableRoomsByHotel(Integer hotelId) {
        return roomService.getAvailableRoomsByHotelId(hotelId);
    }
    
    /**
     * 根据房型获取房间
     */
    public List<Room> getRoomsByType(String roomType) {
        return roomService.getRoomsByType(roomType);
    }
    
    /**
     * 更新房间信息
     */
    public boolean updateRoom(Room room) {
        return roomService.updateRoom(room);
    }
    
    /**
     * 更新房间可用状态
     */
    public boolean updateRoomAvailability(Integer roomId, boolean available) {
        return roomService.updateRoomAvailability(roomId, available);
    }
    
    /**
     * 删除房间
     */
    public boolean deleteRoom(Integer roomId) {
        return roomService.deleteRoom(roomId);
    }
    
    /**
     * 搜索酒店（根据名称和位置）
     */
    public List<Hotel> searchHotels(String name, String location) {
        List<Hotel> allHotels = hotelService.getAllHotels();
        
        return allHotels.stream()
                .filter(hotel -> 
                    (name == null || hotel.getName().toLowerCase().contains(name.toLowerCase())) &&
                    (location == null || hotel.getLocation().toLowerCase().contains(location.toLowerCase()))
                )
                .toList();
    }
    
    /**
     * 搜索可用房间（根据酒店、房型、价格范围）
     */
    public List<Room> searchAvailableRooms(Integer hotelId, String roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Room> availableRooms = roomService.getAvailableRoomsByHotelId(hotelId);
        
        return availableRooms.stream()
                .filter(room -> 
                    (roomType == null || room.getRoomType().equalsIgnoreCase(roomType)) &&
                    (minPrice == null || room.getPrice().compareTo(minPrice) >= 0) &&
                    (maxPrice == null || room.getPrice().compareTo(maxPrice) <= 0)
                )
                .toList();
    }
}
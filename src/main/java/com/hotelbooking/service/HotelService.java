/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.service;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import java.util.*;

/**
 * 酒店服务类 - 管理酒店和房间数据
 */
public class HotelService {
    
    // 模拟数据库存储
    private static Map<Integer, Hotel> hotelDatabase = new HashMap<>();
    private static Map<Integer, List<Room>> roomDatabase = new HashMap<>();
    private static int hotelIdCounter = 1;
    private static int roomIdCounter = 1;
    
    // 静态初始化 - 添加测试数据
    static {
        initializeSampleData();
    }
    
    /**
     * 初始化示例数据
     */
    private static void initializeSampleData() {
        // 添加酒店1
        Hotel hotel1 = new Hotel(
            "Grand Luxury Hotel",
            "123 Main Street, Singapore",
            "Experience luxury at its finest with panoramic city views",
            "WiFi, Pool, Gym, Restaurant, Bar, Spa"
        );
        hotel1.setHotelId(hotelIdCounter++);
        hotelDatabase.put(hotel1.getHotelId(), hotel1);
        
        // 酒店1的房间
        List<Room> rooms1 = new ArrayList<>();
        rooms1.add(createRoom(hotel1.getHotelId(), "101", "Deluxe Room", 150.0, 2));
        rooms1.add(createRoom(hotel1.getHotelId(), "102", "Superior Room", 200.0, 2));
        rooms1.add(createRoom(hotel1.getHotelId(), "201", "Executive Suite", 350.0, 4));
        rooms1.add(createRoom(hotel1.getHotelId(), "301", "Presidential Suite", 800.0, 6));
        roomDatabase.put(hotel1.getHotelId(), rooms1);
        
        // 添加酒店2
        Hotel hotel2 = new Hotel(
            "Marina Bay Resort",
            "456 Beach Road, Singapore",
            "Beachfront paradise with world-class amenities",
            "WiFi, Beach Access, Pool, Gym, Restaurant, Kids Club"
        );
        hotel2.setHotelId(hotelIdCounter++);
        hotelDatabase.put(hotel2.getHotelId(), hotel2);
        
        // 酒店2的房间
        List<Room> rooms2 = new ArrayList<>();
        rooms2.add(createRoom(hotel2.getHotelId(), "A101", "Ocean View Room", 180.0, 2));
        rooms2.add(createRoom(hotel2.getHotelId(), "A102", "Deluxe Ocean View", 250.0, 3));
        rooms2.add(createRoom(hotel2.getHotelId(), "B201", "Family Suite", 400.0, 5));
        rooms2.add(createRoom(hotel2.getHotelId(), "C301", "Penthouse", 1000.0, 8));
        roomDatabase.put(hotel2.getHotelId(), rooms2);
        
        // 添加酒店3
        Hotel hotel3 = new Hotel(
            "Business Hub Hotel",
            "789 Office Park, Singapore",
            "Perfect for business travelers with modern facilities",
            "WiFi, Meeting Rooms, Business Center, Gym, Restaurant"
        );
        hotel3.setHotelId(hotelIdCounter++);
        hotelDatabase.put(hotel3.getHotelId(), hotel3);
        
        // 酒店3的房间
        List<Room> rooms3 = new ArrayList<>();
        rooms3.add(createRoom(hotel3.getHotelId(), "1001", "Business Room", 120.0, 2));
        rooms3.add(createRoom(hotel3.getHotelId(), "1002", "Executive Room", 180.0, 2));
        rooms3.add(createRoom(hotel3.getHotelId(), "2001", "Junior Suite", 280.0, 3));
        roomDatabase.put(hotel3.getHotelId(), rooms3);
        
        // 添加酒店4
        Hotel hotel4 = new Hotel(
            "Garden View Inn",
            "321 Nature Lane, Singapore",
            "Peaceful retreat surrounded by lush gardens",
            "WiFi, Garden, Pool, Restaurant, Spa, Yoga Classes"
        );
        hotel4.setHotelId(hotelIdCounter++);
        hotelDatabase.put(hotel4.getHotelId(), hotel4);
        
        // 酒店4的房间
        List<Room> rooms4 = new ArrayList<>();
        rooms4.add(createRoom(hotel4.getHotelId(), "G01", "Garden Room", 100.0, 2));
        rooms4.add(createRoom(hotel4.getHotelId(), "G02", "Superior Garden Room", 140.0, 2));
        rooms4.add(createRoom(hotel4.getHotelId(), "V01", "Villa", 350.0, 4));
        roomDatabase.put(hotel4.getHotelId(), rooms4);
        
        System.out.println("✅ 已加载 " + hotelDatabase.size() + " 家酒店数据");
    }
    
    /**
     * 创建房间
     */
    private static Room createRoom(int hotelId, String roomNumber, String roomType, 
                                   double price, int maxOccupancy) {
        Room room = new Room(hotelId, roomNumber, roomType, price, maxOccupancy);
        room.setRoomId(roomIdCounter++);
        return room;
    }
    
    /**
     * 搜索酒店
     */
    public List<Hotel> searchHotels(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllHotels();
        }
        
        List<Hotel> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Hotel hotel : hotelDatabase.values()) {
            if (hotel.getName().toLowerCase().contains(lowerKeyword) ||
                hotel.getAddress().toLowerCase().contains(lowerKeyword) ||
                hotel.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(hotel);
            }
        }
        
        System.out.println("🔍 搜索 '" + keyword + "' 找到 " + results.size() + " 家酒店");
        return results;
    }
    
    /**
     * 获取所有酒店
     */
    public List<Hotel> getAllHotels() {
        return new ArrayList<>(hotelDatabase.values());
    }
    
    /**
     * 根据ID获取酒店
     */
    public Hotel getHotelById(int hotelId) {
        return hotelDatabase.get(hotelId);
    }
    
    /**
     * 获取酒店的所有房间
     */
    public List<Room> getRoomsByHotelId(int hotelId) {
        return roomDatabase.getOrDefault(hotelId, new ArrayList<>());
    }
    
    /**
     * 获取酒店的可用房间
     */
    public List<Room> getAvailableRooms(int hotelId) {
        List<Room> allRooms = getRoomsByHotelId(hotelId);
        List<Room> availableRooms = new ArrayList<>();
        
        for (Room room : allRooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        
        return availableRooms;
    }
    
    /**
     * 根据ID获取房间
     */
    public Room getRoomById(int roomId) {
        for (List<Room> rooms : roomDatabase.values()) {
            for (Room room : rooms) {
                if (room.getRoomId() == roomId) {
                    return room;
                }
            }
        }
        return null;
    }
    
    /**
     * 获取最低价格
     */
    public double getMinPrice(int hotelId) {
        List<Room> rooms = getRoomsByHotelId(hotelId);
        return rooms.stream()
                .mapToDouble(Room::getPricePerNight)
                .min()
                .orElse(0.0);
    }
}

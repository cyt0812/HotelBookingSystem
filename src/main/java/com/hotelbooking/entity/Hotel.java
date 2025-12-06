package com.hotelbooking.entity;

import java.util.List;

public class Hotel {
    private Integer id;  // 改为 id 而不是 hotelId
    private String name;
    private String location;  // 改为 location 而不是 address
    private String description;
    private String amenities;
    private int availableRooms;  // 添加这个字段
    private List<Room> rooms;
    
    // 保留原有构造函数，添加新字段
    public Hotel() {
    }
    
    public Hotel(String name, String location, String description, String amenities, int availableRooms) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.amenities = amenities;
        this.availableRooms = availableRooms;
    }
    
    // 修改 getter 和 setter 方法以匹配 HotelDAO 的调用
    
    public Integer getId() {  // 改为 getId() 而不是 getHotelId()
        return id;
    }
    
    public void setId(Integer id) {  // 改为 setId() 而不是 setHotelId()
        this.id = id;
    }
    
//    // 保持原来的 getHotelId() 作为兼容方法
//    public int getHotelId() {
//        return id;
//    }
//    
//    public void setHotelId(int hotelId) {
//        this.id = hotelId;
//    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // 改为 getLocation() 而不是 getAddress()
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {  // 改为 setLocation()
        this.location = location;
    }
    
    // 保持原来的 getAddress() 作为兼容方法
    public String getAddress() {
        return location;
    }
    
    public void setAddress(String address) {
        this.location = address;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAmenities() {
        return amenities;
    }
    
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
    
    // 添加 availableRooms 的 getter 和 setter
    public int getAvailableRooms() {
        return availableRooms;
    }
    
    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }
    
    // 获取酒店的所有房间
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

//    // 获取最低价格
//    public double getMinPrice() {
//        return rooms.stream()
//                .filter(Room::isAvailable)  // 只计算可用房间的价格
//                .mapToDouble(Room::getPrice)  // 获取每个房间的价格
//                .min()
//                .orElseThrow(() -> new RuntimeException("没有可用房间"));
//    }
}
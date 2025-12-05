package com.hotelbooking.entity;

import java.math.BigDecimal;

public class Room {
    private Integer roomId;
    private Integer hotelId;
    private String roomNumber;
    private String roomType;
    private double pricePerNight;
    private int maxOccupancy;
    private boolean isAvailable;
    private String description; // 添加 description 字段
    
  
    
    public Room(int hotelId, String roomNumber, String roomType, 
            double pricePerNight, int maxOccupancy, boolean isAvailable,
            String description) {
    this.hotelId = hotelId;
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    this.pricePerNight = pricePerNight;
    this.maxOccupancy = maxOccupancy;
    this.isAvailable = isAvailable;
    this.description = description;
    }
    
    // ========== 原始方法 ==========
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public Integer getHotelId() {
        return hotelId;
    }
    
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public double getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public int getMaxOccupancy() {
        return maxOccupancy;
    }
    
    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }
    
    public boolean isIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    // ========== 为 RoomDAO 添加的兼容方法 ==========
    
    // 1. 添加 description 字段的 getter 和 setter
    public String getDescription() {
        if (description == null) {
            // 如果 description 为空，返回 roomType 作为描述
            return roomType != null ? roomType : "";
        }
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    // 2. 添加 getId() 方法（RoomDAO 需要）
    public Integer getId() {
        return roomId;
    }
    
    // 3. 添加 setId() 方法（RoomDAO 需要）
    public void setId(Integer id) {
        this.roomId = id;
    }
    
    // 4. 添加 getPrice() 方法返回 BigDecimal（RoomDAO 需要）
    public BigDecimal getPrice() {
        return BigDecimal.valueOf(pricePerNight);
    }
    
    // 5. 添加 setPrice() 方法接受 BigDecimal（RoomDAO 需要）
    public void setPrice(BigDecimal price) {
        if (price != null) {
            this.pricePerNight = price.doubleValue();
        }
    }
    
    // 6. 添加 setAvailable() 方法作为 setIsAvailable() 的别名（RoomDAO 需要）
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
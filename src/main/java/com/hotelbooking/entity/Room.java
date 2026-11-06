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
    private String description; // Add description field
    
  
    
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
    
    // ========== Original Methods ==========
    
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
    
    // ========== Compatibility Methods for RoomDAO ==========
    
    // 1. Add getter and setter for description field
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
    
    // 2. Add getId() method (required by RoomDAO)
    public Integer getId() {
        return roomId;
    }
    
    // 3. Add setId() method (required by RoomDAO)
    public void setId(Integer id) {
        this.roomId = id;
    }
    
    // 4. Add getPrice() method returning BigDecimal (required by RoomDAO)
    public BigDecimal getPrice() {
        return BigDecimal.valueOf(pricePerNight);
    }
    
    // 5. Add setPrice() method accepting BigDecimal (required by RoomDAO)
    public void setPrice(BigDecimal price) {
        if (price != null) {
            this.pricePerNight = price.doubleValue();
        }
    }
    
    // 6. Add setAvailable() method as alias for setIsAvailable() (required by RoomDAO)
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
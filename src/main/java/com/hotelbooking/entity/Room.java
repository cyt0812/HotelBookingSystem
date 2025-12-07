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
<<<<<<< HEAD
    private String description; // Add description field
=======
    private String description; // 添加 description 字段
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    
  
    
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
    
<<<<<<< HEAD
    // ========== Original Methods ==========
=======
    // ========== 原始方法 ==========
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    
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
    
<<<<<<< HEAD
    // ========== Compatibility Methods for RoomDAO ==========
    
    // 1. Add getter and setter for description field
=======
    // ========== 为 RoomDAO 添加的兼容方法 ==========
    
    // 1. 添加 description 字段的 getter 和 setter
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
    
<<<<<<< HEAD
    // 2. Add getId() method (required by RoomDAO)
=======
    // 2. 添加 getId() 方法（RoomDAO 需要）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    public Integer getId() {
        return roomId;
    }
    
<<<<<<< HEAD
    // 3. Add setId() method (required by RoomDAO)
=======
    // 3. 添加 setId() 方法（RoomDAO 需要）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    public void setId(Integer id) {
        this.roomId = id;
    }
    
<<<<<<< HEAD
    // 4. Add getPrice() method returning BigDecimal (required by RoomDAO)
=======
    // 4. 添加 getPrice() 方法返回 BigDecimal（RoomDAO 需要）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    public BigDecimal getPrice() {
        return BigDecimal.valueOf(pricePerNight);
    }
    
<<<<<<< HEAD
    // 5. Add setPrice() method accepting BigDecimal (required by RoomDAO)
=======
    // 5. 添加 setPrice() 方法接受 BigDecimal（RoomDAO 需要）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    public void setPrice(BigDecimal price) {
        if (price != null) {
            this.pricePerNight = price.doubleValue();
        }
    }
    
<<<<<<< HEAD
    // 6. Add setAvailable() method as alias for setIsAvailable() (required by RoomDAO)
=======
    // 6. 添加 setAvailable() 方法作为 setIsAvailable() 的别名（RoomDAO 需要）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
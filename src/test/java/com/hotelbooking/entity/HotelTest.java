package com.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HotelTest {

    @Test
    void hotelCreation_WithAllFields_ShouldSetPropertiesCorrectly() {
        // 准备 & 执行
        Hotel hotel = new Hotel("Grand Hotel", "New York", "Luxury hotel in downtown","gym", 50);
        
        // 验证
        assertEquals("Grand Hotel", hotel.getName());
        assertEquals("New York", hotel.getLocation());
        assertEquals("Luxury hotel in downtown", hotel.getDescription());
        assertEquals(50, hotel.getAvailableRooms());
        assertNull(hotel.getId()); // ID 应该为 null，直到保存到数据库
    }

    @Test
    void hotelSetters_ShouldUpdateFieldsCorrectly() {
        // 准备
        Hotel hotel = new Hotel();
        
        // 执行
        hotel.setId(1);
        hotel.setName("Beach Resort");
        hotel.setLocation("Miami");
        hotel.setDescription("Beachfront property with ocean view");
        hotel.setAvailableRooms(25);
        
        // 验证
        assertEquals(1, hotel.getId());
        assertEquals("Beach Resort", hotel.getName());
        assertEquals("Miami", hotel.getLocation());
        assertEquals("Beachfront property with ocean view", hotel.getDescription());
        assertEquals(25, hotel.getAvailableRooms());
    }

    @Test
    void decreaseAvailableRooms_ShouldUpdateRoomCount() {
        // 准备
        Hotel hotel = new Hotel("Test Hotel", "Location", "Desc","gym", 10);
        
        // 执行
        hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
        
        // 验证
        assertEquals(9, hotel.getAvailableRooms());
    }
}
package com.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void roomCreation_WithAllFields_ShouldSetPropertiesCorrectly() {
        // 准备 & 执行
        Room room = new Room(1, "101", "SINGLE", new BigDecimal("99.99"), true);
        
        // 验证
        assertEquals(1, room.getHotelId());
        assertEquals("101", room.getRoomNumber());
        assertEquals("SINGLE", room.getRoomType());
        assertEquals(new BigDecimal("99.99"), room.getPrice());
        assertTrue(room.isAvailable());
        assertNull(room.getId()); // ID 应该为 null，直到保存到数据库
    }

    @Test
    void roomSetters_ShouldUpdateFieldsCorrectly() {
        // 准备
        Room room = new Room();
        
        // 执行
        room.setId(1);
        room.setHotelId(2);
        room.setRoomNumber("202");
        room.setRoomType("DOUBLE");
        room.setPrice(new BigDecimal("149.99"));
        room.setAvailable(false);
        
        // 验证
        assertEquals(1, room.getId());
        assertEquals(2, room.getHotelId());
        assertEquals("202", room.getRoomNumber());
        assertEquals("DOUBLE", room.getRoomType());
        assertEquals(new BigDecimal("149.99"), room.getPrice());
        assertFalse(room.isAvailable());
    }

    @Test
    void roomTypes_ShouldHaveValidValues() {
        // 测试房间类型常量
        Room room1 = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        Room room2 = new Room(1, "102", "DOUBLE", new BigDecimal("150"), true);
        Room room3 = new Room(1, "103", "SUITE", new BigDecimal("250"), true);
        
        assertEquals("SINGLE", room1.getRoomType());
        assertEquals("DOUBLE", room2.getRoomType());
        assertEquals("SUITE", room3.getRoomType());
    }
}
package com.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void roomCreation_WithAllFields_ShouldSetPropertiesCorrectly() {
        // 修复：添加 description 参数
        Room room = new Room(1, "101", "SINGLE", 99.99, 2, true, "Standard single room");
        
        // 验证
        assertEquals(1, room.getHotelId());
        assertEquals("101", room.getRoomNumber());
        assertEquals("SINGLE", room.getRoomType());
        assertEquals(new BigDecimal("99.99"), room.getPrice());
        assertEquals("Standard single room", room.getDescription());
        assertTrue(room.isAvailable());
        assertNull(room.getId()); // ID 应该为 null，直到保存到数据库
    }

    @Test
    void roomSetters_ShouldUpdateFieldsCorrectly() {
        // 准备 - 需要无参构造函数，添加一个
        Room room = new Room(0, "", "", 0.0, 0, false, ""); // 使用有参构造函数
        
        // 执行
        room.setId(1);
        room.setHotelId(2);
        room.setRoomNumber("202");
        room.setRoomType("DOUBLE");
        room.setPrice(new BigDecimal("149.99"));
        room.setAvailable(false);
        room.setDescription("Updated description");
        
        // 验证
        assertEquals(1, room.getId());
        assertEquals(2, room.getHotelId());
        assertEquals("202", room.getRoomNumber());
        assertEquals("DOUBLE", room.getRoomType());
        assertEquals(new BigDecimal("149.99"), room.getPrice());
        assertEquals("Updated description", room.getDescription());
        assertFalse(room.isAvailable());
    }

    @Test
    void roomTypes_ShouldHaveValidValues() {
        // 测试房间类型常量
        Room room1 = new Room(1, "101", "SINGLE", 100, 2, true, "Single room");
        Room room2 = new Room(1, "102", "DOUBLE", 150, 2, true, "Double room");
        Room room3 = new Room(1, "103", "SUITE", 250, 2, true, "Suite room");
        
        assertEquals("SINGLE", room1.getRoomType());
        assertEquals("DOUBLE", room2.getRoomType());
        assertEquals("SUITE", room3.getRoomType());
    }
    
    @Test
    void testRoomConstructor_WithDefaultValues() {
        // 如果你给 Room 类添加了无参构造函数，可以这样测试
        Room room = new Room(0, "", "", 0.0, 0, false, "");
        
        // 设置值
        room.setId(100);
        room.setHotelId(200);
        room.setRoomNumber("301");
        room.setRoomType("DELUXE");
        room.setPrice(new BigDecimal("299.99"));
        room.setDescription("Deluxe room with view");
        room.setAvailable(true);
        
        // 验证
        assertEquals(100, room.getId());
        assertEquals(200, room.getHotelId());
        assertEquals("301", room.getRoomNumber());
        assertEquals("DELUXE", room.getRoomType());
        assertEquals(new BigDecimal("299.99"), room.getPrice());
        assertEquals("Deluxe room with view", room.getDescription());
        assertTrue(room.isAvailable());
    }
}
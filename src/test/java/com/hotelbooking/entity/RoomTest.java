package com.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void roomCreation_WithAllFields_ShouldSetPropertiesCorrectly() {
        // Fix: Add description parameter
        Room room = new Room(1, "101", "SINGLE", 99.99, 2, true, "Standard single room");
        
        // Assert
        assertEquals(1, room.getHotelId());
        assertEquals("101", room.getRoomNumber());
        assertEquals("SINGLE", room.getRoomType());
        assertEquals(new BigDecimal("99.99"), room.getPrice());
        assertEquals("Standard single room", room.getDescription());
        assertTrue(room.isAvailable());
        assertNull(room.getId()); // ID should be null until saved to database
    }

    @Test
    void roomSetters_ShouldUpdateFieldsCorrectly() {
        // Arrange - Need no-arg constructor, add one
        Room room = new Room(0, "", "", 0.0, 0, false, ""); // Using parameterized constructor
        
        // Act
        room.setId(1);
        room.setHotelId(2);
        room.setRoomNumber("202");
        room.setRoomType("DOUBLE");
        room.setPrice(new BigDecimal("149.99"));
        room.setAvailable(false);
        room.setDescription("Updated description");
        
        // Assert
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
        // Test room type constants
        Room room1 = new Room(1, "101", "SINGLE", 100, 2, true, "Single room");
        Room room2 = new Room(1, "102", "DOUBLE", 150, 2, true, "Double room");
        Room room3 = new Room(1, "103", "SUITE", 250, 2, true, "Suite room");
        
        assertEquals("SINGLE", room1.getRoomType());
        assertEquals("DOUBLE", room2.getRoomType());
        assertEquals("SUITE", room3.getRoomType());
    }
    
    @Test
    void testRoomConstructor_WithDefaultValues() {
        // If you add a no-arg constructor to Room class, you can test like this
        Room room = new Room(0, "", "", 0.0, 0, false, "");
        
        // Set values
        room.setId(100);
        room.setHotelId(200);
        room.setRoomNumber("301");
        room.setRoomType("DELUXE");
        room.setPrice(new BigDecimal("299.99"));
        room.setDescription("Deluxe room with view");
        room.setAvailable(true);
        
        // Assert
        assertEquals(100, room.getId());
        assertEquals(200, room.getHotelId());
        assertEquals("301", room.getRoomNumber());
        assertEquals("DELUXE", room.getRoomType());
        assertEquals(new BigDecimal("299.99"), room.getPrice());
        assertEquals("Deluxe room with view", room.getDescription());
        assertTrue(room.isAvailable());
    }
}
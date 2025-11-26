package com.hotelbooking.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class RoomTest {

    @Test
    public void testConstructorAndGetters() {
        Room room = new Room(1, "101", "Deluxe", 200.0, 2);

        assertEquals(1, room.getHotelId());
        assertEquals("101", room.getRoomNumber());
        assertEquals("Deluxe", room.getRoomType());
        assertEquals(200.0, room.getPricePerNight(), 0.001);
        assertEquals(2, room.getMaxOccupancy());
        assertTrue(room.isAvailable()); // 构造函数默认 true
    }

    @Test
    public void testSetters() {
        Room room = new Room();
        room.setHotelId(2);
        room.setRoomNumber("202");
        room.setRoomType("Standard");
        room.setPricePerNight(150.0);
        room.setMaxOccupancy(3);
        room.setAvailable(false);

        assertEquals(2, room.getHotelId());
        assertEquals("202", room.getRoomNumber());
        assertEquals("Standard", room.getRoomType());
        assertEquals(150.0, room.getPricePerNight(), 0.001);
        assertEquals(3, room.getMaxOccupancy());
        assertFalse(room.isAvailable());
    }
}

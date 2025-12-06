package com.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HotelTest {

    @Test
    void hotelCreation_WithAllFields_ShouldSetPropertiesCorrectly() {
        // Arrange & Act
        Hotel hotel = new Hotel("Grand Hotel", "New York", "Luxury hotel in downtown","gym", 50);
        
        // Assert
        assertEquals("Grand Hotel", hotel.getName());
        assertEquals("New York", hotel.getLocation());
        assertEquals("Luxury hotel in downtown", hotel.getDescription());
        assertEquals(50, hotel.getAvailableRooms());
        assertNull(hotel.getId()); // ID should be null until saved to database
    }

    @Test
    void hotelSetters_ShouldUpdateFieldsCorrectly() {
        // Arrange
        Hotel hotel = new Hotel();
        
        // Act
        hotel.setId(1);
        hotel.setName("Beach Resort");
        hotel.setLocation("Miami");
        hotel.setDescription("Beachfront property with ocean view");
        hotel.setAvailableRooms(25);
        
        // Assert
        assertEquals(1, hotel.getId());
        assertEquals("Beach Resort", hotel.getName());
        assertEquals("Miami", hotel.getLocation());
        assertEquals("Beachfront property with ocean view", hotel.getDescription());
        assertEquals(25, hotel.getAvailableRooms());
    }

    @Test
    void decreaseAvailableRooms_ShouldUpdateRoomCount() {
        // Arrange
        Hotel hotel = new Hotel("Test Hotel", "Location", "Desc","gym", 10);
        
        // Act
        hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
        
        // Assert
        assertEquals(9, hotel.getAvailableRooms());
    }
}
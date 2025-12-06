package com.hotelbooking.dao;

import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.util.DatabaseConnection;
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoomDAOTest {

    private RoomDAO roomDAO;
    private HotelDAO hotelDAO;
    private Integer testHotelId;
    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        // Clean and reinitialize database (required for Derby)
        DatabaseInitializer.resetDatabase();
        
        // Get database connection
        connection = DatabaseConnection.getConnection();
        roomDAO = new RoomDAO();
        hotelDAO = new HotelDAO();
        
        // Create a test hotel (Note: need to create hotel first due to foreign key constraint)
     Hotel hotel = new Hotel("Test Hotel", "Test City", "Test Description", "Pool,Gym", 10);
        Hotel createdHotel = hotelDAO.createHotel(hotel);
        
        if (createdHotel != null) {
            testHotelId = createdHotel.getId();
        } else {
            throw new RuntimeException("Failed to create test hotel");
        }
    }
    
    @AfterEach
    void tearDown() throws Exception {
        // Clean test data
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                // Derby requires disabling foreign key constraints first
                try {
                    stmt.execute("SET CONSTRAINTS ALL DEFERRED");
                } catch (Exception e) {
                    // Ignore error, some Derby versions don't need this
                }
                
                // Delete test data
                try {
                    stmt.execute("DELETE FROM rooms WHERE hotel_id = " + testHotelId);
                } catch (Exception e) {}
                
                try {
                    stmt.execute("DELETE FROM hotels WHERE id = " + testHotelId);
                } catch (Exception e) {}
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void createRoom_WithValidRoom_ShouldReturnRoomWithId() {
        // Arrange - Use constructor with 7 parameters
        Room room = new Room(testHotelId, "101", "STANDARD", 99.99, 2, true, "A comfortable standard room");
        
        // Act
        Room result = roomDAO.createRoom(room);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testHotelId, result.getHotelId());
        assertEquals("101", result.getRoomNumber());
        assertEquals("STANDARD", result.getRoomType());
        assertEquals(99.99, result.getPricePerNight(), 0.001);
        assertEquals(2, result.getMaxOccupancy());
        assertEquals("A comfortable standard room", result.getDescription());
        assertTrue(result.isAvailable());
    }
    
    @Test
    void getRoomById_WithExistingId_ShouldReturnRoom() {
        // First create a room
        Room room = new Room(testHotelId, "102", "DELUXE", 149.99, 4, true, "Double room with two beds");
        Room created = roomDAO.createRoom(room);
        assertNotNull(created);
        
        // Query this room
        Optional<Room> foundOpt = roomDAO.getRoomById(created.getId());
        
        // Assert
        assertTrue(foundOpt.isPresent());
        Room found = foundOpt.get();
        assertEquals(created.getId(), found.getId());
        assertEquals("102", found.getRoomNumber());
        assertEquals("DELUXE", found.getRoomType());
        assertEquals(149.99, found.getPricePerNight(), 0.001);
        assertEquals(4, found.getMaxOccupancy());
        assertEquals("Double room with two beds", found.getDescription());
        assertTrue(found.isAvailable());
    }
    
    @Test
    void getRoomById_WithNonExistingId_ShouldReturnEmpty() {
        // Query non-existing room
        Optional<Room> result = roomDAO.getRoomById(9999);
        
        // Assert
        assertFalse(result.isPresent());
    }
    
    @Test
    void updateRoom_ShouldUpdateRoomDetails() {
        // Create room
        Room room = new Room(testHotelId, "103", "SUITE", 299.99, 3, true, "Luxury suite");
        Room created = roomDAO.createRoom(room);
        assertNotNull(created);
        
        // Modify information
        created.setRoomNumber("103A");
        created.setPricePerNight(399.99);
        created.setDescription("Updated luxury suite with view");
        created.setAvailable(false);
        
        // Update
        boolean updated = roomDAO.updateRoom(created);
        
        // Assert update was successful
        assertTrue(updated);
        
        // Assert updated data
        Optional<Room> foundOpt = roomDAO.getRoomById(created.getId());
        assertTrue(foundOpt.isPresent());
        Room found = foundOpt.get();
        
        assertEquals("103A", found.getRoomNumber());
        assertEquals(399.99, found.getPricePerNight(), 0.001);
        assertEquals("Updated luxury suite with view", found.getDescription());
        assertFalse(found.isAvailable());
        // Other fields should remain unchanged
        assertEquals("SUITE", found.getRoomType());
        assertEquals(testHotelId, found.getHotelId());
    }
    
    @Test
    void getRoomsByHotelId_ShouldReturnAllRoomsForHotel() {
        // Create multiple rooms for the same hotel
        Room room1 = new Room(testHotelId, "201", "STANDARD", 99.99, 2, true, "Room 201");
        Room room2 = new Room(testHotelId, "202", "DELUXE", 149.99, 4, true, "Room 202");
        
        roomDAO.createRoom(room1);
        roomDAO.createRoom(room2);
        
        // Execute query
        List<Room> rooms = roomDAO.getRoomsByHotelId(testHotelId);
        
        // Assert
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().allMatch(r -> r.getHotelId().equals(testHotelId)));
        assertTrue(rooms.stream().anyMatch(r -> r.getRoomNumber().equals("201")));
        assertTrue(rooms.stream().anyMatch(r -> r.getRoomNumber().equals("202")));
    }
    
    @Test
    void getAvailableRoomsByHotelId_ShouldReturnOnlyAvailableRooms() {
        // Create rooms - one available, one unavailable
        Room availableRoom = new Room(testHotelId, "301", "STANDARD", 99.99, 2, true, "Available room");
        Room unavailableRoom = new Room(testHotelId, "302", "DELUXE", 149.99, 4, false, "Unavailable room");
        
        roomDAO.createRoom(availableRoom);
        roomDAO.createRoom(unavailableRoom);
        
        // Execute query
        List<Room> rooms = roomDAO.getAvailableRoomsByHotelId(testHotelId);
        
        // Assert
        assertNotNull(rooms);
        assertEquals(1, rooms.size());
        assertEquals("301", rooms.get(0).getRoomNumber());
        assertTrue(rooms.get(0).isAvailable());
    }
    
    @Test
    void getRoomsByType_ShouldReturnRoomsOfSpecifiedType() {
        // Create rooms of different types
        Room room1 = new Room(testHotelId, "401", "STANDARD", 99.99, 2, true, "Standard room 1");
        Room room2 = new Room(testHotelId, "402", "STANDARD", 109.99, 2, true, "Standard room 2");
        Room doubleRoom = new Room(testHotelId, "403", "DELUXE", 199.99, 4, true, "Deluxe room");
        
        roomDAO.createRoom(room1);
        roomDAO.createRoom(room2);
        roomDAO.createRoom(doubleRoom);
        
        // Execute query
        List<Room> standardRooms = roomDAO.getRoomsByType("STANDARD");
        
        // Assert
        assertNotNull(standardRooms);
        assertEquals(2, standardRooms.size());
        assertTrue(standardRooms.stream().allMatch(r -> "STANDARD".equals(r.getRoomType())));
        // Should be sorted by price ascending
        assertEquals(99.99, standardRooms.get(0).getPricePerNight(), 0.001);
        assertEquals(109.99, standardRooms.get(1).getPricePerNight(), 0.001);
    }
    
    @Test
    void getRoomsByPriceRange_ShouldReturnRoomsInPriceRange() {
        // Create rooms with different prices
        Room cheapRoom = new Room(testHotelId, "501", "STANDARD", 50.00, 2, true, "Cheap room");
        Room midRoom = new Room(testHotelId, "502", "DELUXE", 100.00, 4, true, "Mid room");
        Room expensiveRoom = new Room(testHotelId, "503", "SUITE", 200.00, 3, true, "Expensive room");
        
        roomDAO.createRoom(cheapRoom);
        roomDAO.createRoom(midRoom);
        roomDAO.createRoom(expensiveRoom);
        
        // Execute query (price range 75-150)
        List<Room> rooms = roomDAO.getRoomsByPriceRange(new BigDecimal("75"), new BigDecimal("150"));
        
        // Assert
        assertNotNull(rooms);
        assertEquals(1, rooms.size());  // Only midRoom is in range
        assertEquals("502", rooms.get(0).getRoomNumber());
        assertEquals(100.00, rooms.get(0).getPricePerNight(), 0.001);
    }
    
    @Test
    void updateRoomAvailability_ShouldUpdateAvailability() {
        // Create room
        Room room = new Room(testHotelId, "601", "STANDARD", 99.99, 2, true, "Test room");
        Room created = roomDAO.createRoom(room);
        
        // Update to unavailable (Derby uses 1/0 for boolean values)
        boolean updated = roomDAO.updateRoomAvailability(created.getId(), false);
        assertTrue(updated);
        
        // Assert
        Optional<Room> foundOpt = roomDAO.getRoomById(created.getId());
        assertTrue(foundOpt.isPresent());
        assertFalse(foundOpt.get().isAvailable());
        
        // Update to available again
        updated = roomDAO.updateRoomAvailability(created.getId(), true);
        assertTrue(updated);
        
        foundOpt = roomDAO.getRoomById(created.getId());
        assertTrue(foundOpt.isPresent());
        assertTrue(foundOpt.get().isAvailable());
    }
    
    @Test
    void deleteRoom_ShouldRemoveRoom() {
        // Create room
        Room room = new Room(testHotelId, "701", "STANDARD", 99.99, 2, true, "Room to delete");
        Room created = roomDAO.createRoom(room);
        
        // Delete room
        boolean deleted = roomDAO.deleteRoom(created.getId());
        assertTrue(deleted);
        
        // Assert room is deleted
        Optional<Room> foundOpt = roomDAO.getRoomById(created.getId());
        assertFalse(foundOpt.isPresent());
    }
    
    @Test
    void isRoomNumberExists_ShouldCheckRoomNumber() {
        // Create room
        Room room = new Room(testHotelId, "801", "STANDARD", 99.99, 2, true, "Test room");
        roomDAO.createRoom(room);
        
        // Assert existing room number exists
        boolean exists = roomDAO.isRoomNumberExists(testHotelId, "801");
        assertTrue(exists);
        
        // Assert non-existing room number doesn't exist
        exists = roomDAO.isRoomNumberExists(testHotelId, "999");
        assertFalse(exists);
    }
    
    @Test
    void getAllAvailableRooms_ShouldReturnAllAvailableRooms() {
        // Create another hotel
        Hotel hotel2 = new Hotel("Another Hotel", "Another City", "Description", "WiFi", 5);
        Hotel createdHotel2 = hotelDAO.createHotel(hotel2);
        
        // Create available and unavailable rooms
        Room available1 = new Room(testHotelId, "901", "STANDARD", 99.99, 2, true, "Available 1");
        Room available2 = new Room(createdHotel2.getId(), "902", "DELUXE", 149.99, 4, true, "Available 2");
        Room unavailable = new Room(testHotelId, "903", "SUITE", 299.99, 3, false, "Unavailable");
        
        roomDAO.createRoom(available1);
        roomDAO.createRoom(available2);
        roomDAO.createRoom(unavailable);
        
        // Execute query
        List<Room> allAvailable = roomDAO.getAllAvailableRooms();
        
        // Assert
        assertNotNull(allAvailable);
        assertEquals(2, allAvailable.size());
        assertTrue(allAvailable.stream().allMatch(Room::isAvailable));
    }
}
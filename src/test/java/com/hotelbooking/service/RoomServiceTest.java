package com.hotelbooking.service;

import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomDAO roomDAO;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom_WithValidData_ShouldCreateRoom() {
        // 准备
        Room expectedRoom = new Room(1, "101", "SINGLE", new BigDecimal("99.99"), true);
        expectedRoom.setId(1);
        
        when(roomDAO.createRoom(any(Room.class))).thenReturn(expectedRoom);
        
        // 执行
        Room result = roomService.createRoom(1, "101", "SINGLE", new BigDecimal("99.99"), true);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("101", result.getRoomNumber());
        assertEquals("SINGLE", result.getRoomType());
        verify(roomDAO, times(1)).createRoom(any(Room.class));
    }

    @Test
    void createRoom_WithInvalidHotelId_ShouldThrowException() {
        // 执行和验证
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> roomService.createRoom(null, "101", "SINGLE", new BigDecimal("100"), true)
        );
        
        assertEquals("Hotel ID cannot be null", exception.getMessage());
        verify(roomDAO, never()).createRoom(any(Room.class));
    }

    @Test
    void createRoom_WithEmptyRoomNumber_ShouldThrowException() {
        // 执行和验证
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> roomService.createRoom(1, "", "SINGLE", new BigDecimal("100"), true)
        );
        
        assertEquals("Room number cannot be empty", exception.getMessage());
        verify(roomDAO, never()).createRoom(any(Room.class));
    }

    @Test
    void createRoom_WithInvalidPrice_ShouldThrowException() {
        // 执行和验证
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> roomService.createRoom(1, "101", "SINGLE", new BigDecimal("-100"), true)
        );
        
        assertEquals("Price must be positive", exception.getMessage());
        verify(roomDAO, never()).createRoom(any(Room.class));
    }

    @Test
    void getRoomById_WithExistingId_ShouldReturnRoom() {
        // 准备
        Room room = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        room.setId(1);
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        
        // 执行
        Optional<Room> result = roomService.getRoomById(1);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(roomDAO, times(1)).getRoomById(1);
    }

    @Test
    void getRoomsByHotelId_ShouldReturnHotelRooms() {
        // 准备
        Room room1 = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        Room room2 = new Room(1, "102", "DOUBLE", new BigDecimal("150"), true);
        List<Room> expectedRooms = Arrays.asList(room1, room2);
        
        when(roomDAO.getRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = roomService.getRoomsByHotelId(1);
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(room -> room.getHotelId().equals(1)));
        verify(roomDAO, times(1)).getRoomsByHotelId(1);
    }

    @Test
    void getAvailableRoomsByHotelId_ShouldReturnAvailableRooms() {
        // 准备
        Room availableRoom = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        Room unavailableRoom = new Room(1, "102", "DOUBLE", new BigDecimal("150"), false);
        List<Room> expectedRooms = Arrays.asList(availableRoom);
        
        when(roomDAO.getAvailableRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = roomService.getAvailableRoomsByHotelId(1);
        
        // 验证
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
        verify(roomDAO, times(1)).getAvailableRoomsByHotelId(1);
    }

    @Test
    void getRoomsByType_ShouldReturnMatchingRooms() {
        // 准备
        Room singleRoom1 = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        Room singleRoom2 = new Room(2, "201", "SINGLE", new BigDecimal("120"), true);
        List<Room> expectedRooms = Arrays.asList(singleRoom1, singleRoom2);
        
        when(roomDAO.getRoomsByType("SINGLE")).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = roomService.getRoomsByType("SINGLE");
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(room -> room.getRoomType().equals("SINGLE")));
        verify(roomDAO, times(1)).getRoomsByType("SINGLE");
    }

    @Test
    void updateRoomAvailability_ShouldUpdateRoomStatus() {
        // 准备
        Room room = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        room.setId(1);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        when(roomDAO.updateRoom(any(Room.class))).thenReturn(true);
        
        // 执行
        boolean result = roomService.updateRoomAvailability(1, false);
        
        // 验证
        assertTrue(result);
        verify(roomDAO, times(1)).updateRoom(any(Room.class));
    }

    @Test
    void updateRoomAvailability_WithNonExistingRoom_ShouldReturnFalse() {
        // 准备
        when(roomDAO.getRoomById(999)).thenReturn(Optional.empty());
        
        // 执行
        boolean result = roomService.updateRoomAvailability(999, false);
        
        // 验证
        assertFalse(result);
        verify(roomDAO, never()).updateRoom(any(Room.class));
    }

    @Test
    void deleteRoom_WithExistingId_ShouldReturnTrue() {
        // 准备
        when(roomDAO.deleteRoom(1)).thenReturn(true);
        
        // 执行
        boolean result = roomService.deleteRoom(1);
        
        // 验证
        assertTrue(result);
        verify(roomDAO, times(1)).deleteRoom(1);
    }
}
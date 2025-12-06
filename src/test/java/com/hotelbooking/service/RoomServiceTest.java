package com.hotelbooking.service;

import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Room;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomDAO roomDAO;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom_WithValidData_ShouldCreateRoom() {
        // 准备 - 使用7个参数的构造函数
        Room expectedRoom = new Room(1, "101", "SINGLE", 99.99, 2, true, "Standard single room");
        expectedRoom.setId(1);
        
        when(roomDAO.createRoom(any(Room.class))).thenReturn(expectedRoom);
        
        // 执行
        Room result = roomService.createRoom(1, "101", "SINGLE", new BigDecimal("99.99"), true);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("101", result.getRoomNumber());
        assertEquals("SINGLE", result.getRoomType());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        assertTrue(result.isAvailable());
        verify(roomDAO, times(1)).createRoom(any(Room.class));
    }

    @Test
    void createRoom_WithInvalidHotelId_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.createRoom(null, "101", "SINGLE", new BigDecimal("100"), true)
        );
        
        assertEquals("Hotel ID cannot be empty", exception.getMessage());
        verify(roomDAO, never()).createRoom(any(Room.class));
    }

    @Test
    void createRoom_WithEmptyRoomNumber_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.createRoom(1, "", "SINGLE", new BigDecimal("100"), true)
        );
        
        assertEquals("Room number cannot be empty", exception.getMessage());
        verify(roomDAO, never()).createRoom(any(Room.class));
    }

    @Test
    void createRoom_WithInvalidPrice_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.createRoom(1, "101", "SINGLE", new BigDecimal("-100"), true)
        );
        
        assertEquals("Price must be positive", exception.getMessage());
        verify(roomDAO, never()).createRoom(any(Room.class));
    }

    @Test
    void createRoom_WithZeroPrice_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.createRoom(1, "101", "SINGLE", BigDecimal.ZERO, true)
        );
        
        assertEquals("Price must be positive", exception.getMessage());
        verify(roomDAO, never()).createRoom(any(Room.class));
    }

    @Test
    void createRoom_WithDuplicateRoomNumber_ShouldThrowBusinessException() {
        // 准备
        when(roomDAO.isRoomNumberExists(1, "101")).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> roomService.createRoom(1, "101", "SINGLE", new BigDecimal("100"), true)
        );
        
        assertEquals(ErrorType.ROOM_ALREADY_EXISTS, exception.getErrorType());
        verify(roomDAO, times(1)).isRoomNumberExists(1, "101");
        verify(roomDAO, never()).createRoom(any(Room.class));
    }

    @Test
    void getRoomById_WithExistingId_ShouldReturnRoom() {
        // 准备 - 使用7个参数的构造函数
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, true, "Single room with view");
        room.setId(1);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        
        // 执行
        Optional<Room> result = roomService.getRoomById(1);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("101", result.get().getRoomNumber());
        assertEquals("SINGLE", result.get().getRoomType());
        assertEquals(new BigDecimal("100.0"), result.get().getPrice());
        assertTrue(result.get().isAvailable());
        verify(roomDAO, times(1)).getRoomById(1);
    }

    @Test
    void getRoomById_WithNonExistingId_ShouldReturnEmpty() {
        // 准备
        when(roomDAO.getRoomById(999)).thenReturn(Optional.empty());
        
        // 执行
        Optional<Room> result = roomService.getRoomById(999);
        
        // 验证
        assertTrue(result.isEmpty());
        verify(roomDAO, times(1)).getRoomById(999);
    }

    @Test
    void getRoomsByHotelId_ShouldReturnHotelRooms() {
        // 准备 - 使用7个参数的构造函数
        Room room1 = new Room(1, "101", "SINGLE", 100.0, 2, true, "Standard single room");
        Room room2 = new Room(1, "102", "DOUBLE", 150.0, 4, true, "Deluxe double room");
        List<Room> expectedRooms = Arrays.asList(room1, room2);
        
        when(roomDAO.getRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = roomService.getRoomsByHotelId(1);
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(room -> room.getHotelId().equals(1)));
        assertEquals("101", result.get(0).getRoomNumber());
        assertEquals("102", result.get(1).getRoomNumber());
        verify(roomDAO, times(1)).getRoomsByHotelId(1);
    }

    @Test
    void getRoomsByHotelId_WithInvalidHotelId_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.getRoomsByHotelId(null)
        );
        
        assertEquals("Hotel ID cannot be empty", exception.getMessage());
        verify(roomDAO, never()).getRoomsByHotelId(anyInt());
    }

    @Test
    void getAvailableRoomsByHotelId_ShouldReturnAvailableRooms() {
        // 准备 - 使用7个参数的构造函数
        Room availableRoom = new Room(1, "101", "SINGLE", 100.0, 2, true, "Available single room");
        Room unavailableRoom = new Room(1, "102", "DOUBLE", 150.0, 4, false, "Unavailable double room");
        List<Room> expectedRooms = Arrays.asList(availableRoom);
        
        when(roomDAO.getAvailableRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = roomService.getAvailableRoomsByHotelId(1);
        
        // 验证
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
        assertEquals("101", result.get(0).getRoomNumber());
        verify(roomDAO, times(1)).getAvailableRoomsByHotelId(1);
    }

    @Test
    void getAvailableRoomsByHotelId_WithInvalidHotelId_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.getAvailableRoomsByHotelId(null)
        );
        
        assertEquals("Hotel ID cannot be empty", exception.getMessage());
        verify(roomDAO, never()).getAvailableRoomsByHotelId(anyInt());
    }

    @Test
    void getRoomsByType_ShouldReturnMatchingRooms() {
        // 准备 - 使用7个参数的构造函数
        Room singleRoom1 = new Room(1, "101", "SINGLE", 100.0, 2, true, "Single room 101");
        Room singleRoom2 = new Room(2, "201", "SINGLE", 120.0, 2, true, "Single room 201");
        List<Room> expectedRooms = Arrays.asList(singleRoom1, singleRoom2);
        
        when(roomDAO.getRoomsByType("SINGLE")).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = roomService.getRoomsByType("SINGLE");
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(room -> room.getRoomType().equals("SINGLE")));
        assertEquals("101", result.get(0).getRoomNumber());
        assertEquals("201", result.get(1).getRoomNumber());
        verify(roomDAO, times(1)).getRoomsByType("SINGLE");
    }

    @Test
    void getRoomsByType_WithEmptyType_ShouldReturnEmptyList() {
        // 执行
        List<Room> result = roomService.getRoomsByType("");
        
        // 验证
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roomDAO, never()).getRoomsByType(anyString());
    }

    @Test
    void getRoomsByType_WithNullType_ShouldReturnEmptyList() {
        // 执行
        List<Room> result = roomService.getRoomsByType(null);
        
        // 验证
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roomDAO, never()).getRoomsByType(anyString());
    }

    @Test
    void getRoomsByPriceRange_ShouldReturnRoomsInRange() {
        // 准备 - 使用7个参数的构造函数
        Room room1 = new Room(1, "101", "SINGLE", 100.0, 2, true, "Budget single room");
        Room room2 = new Room(1, "201", "DOUBLE", 150.0, 4, true, "Standard double room");
        List<Room> expectedRooms = Arrays.asList(room1, room2);
        
        when(roomDAO.getRoomsByPriceRange(new BigDecimal("80"), new BigDecimal("200")))
            .thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = roomService.getRoomsByPriceRange(new BigDecimal("80"), new BigDecimal("200"));
        
        // 验证
        assertEquals(2, result.size());
        verify(roomDAO, times(1)).getRoomsByPriceRange(new BigDecimal("80"), new BigDecimal("200"));
    }

    @Test
    void getRoomsByPriceRange_WithInvalidRange_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.getRoomsByPriceRange(new BigDecimal("200"), new BigDecimal("100"))
        );
        
        assertEquals("Minimum price cannot be greater than maximum price", exception.getMessage());
        verify(roomDAO, never()).getRoomsByPriceRange(any(), any());
    }

    @Test
    void updateRoomAvailability_ShouldUpdateRoomStatus() {
        // 准备 - 使用7个参数的构造函数
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, true, "Single room");
        room.setId(1);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        when(roomDAO.updateRoom(any(Room.class))).thenReturn(true);
        
        // 执行
        boolean result = roomService.updateRoomAvailability(1, false);
        
        // 验证
        assertTrue(result);
        verify(roomDAO, times(1)).getRoomById(1);
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
        verify(roomDAO, times(1)).getRoomById(999);
        verify(roomDAO, never()).updateRoom(any(Room.class));
    }

    @Test
    void updateRoomPrice_WithValidData_ShouldUpdatePrice() {
        // 准备
        when(roomDAO.updateRoomPrice(1, new BigDecimal("199.99"))).thenReturn(true);
        
        // 执行
        boolean result = roomService.updateRoomPrice(1, new BigDecimal("199.99"));
        
        // 验证
        assertTrue(result);
        verify(roomDAO, times(1)).updateRoomPrice(1, new BigDecimal("199.99"));
    }

    @Test
    void updateRoomPrice_WithInvalidRoomId_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.updateRoomPrice(null, new BigDecimal("199.99"))
        );
        
        assertEquals("Room ID cannot be empty", exception.getMessage());
        verify(roomDAO, never()).updateRoomPrice(anyInt(), any());
    }

    @Test
    void updateRoomPrice_WithInvalidPrice_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.updateRoomPrice(1, new BigDecimal("-100"))
        );
        
        assertEquals("Price must be positive", exception.getMessage());
        verify(roomDAO, never()).updateRoomPrice(anyInt(), any());
    }

    @Test
    void isRoomNumberExists_ShouldReturnCorrectResult() {
        // 准备
        when(roomDAO.isRoomNumberExists(1, "101")).thenReturn(true);
        when(roomDAO.isRoomNumberExists(1, "102")).thenReturn(false);
        
        // 执行和验证
        assertTrue(roomService.isRoomNumberExists(1, "101"));
        assertFalse(roomService.isRoomNumberExists(1, "102"));
        verify(roomDAO, times(2)).isRoomNumberExists(anyInt(), anyString());
    }

    @Test
    void isRoomNumberExists_WithInvalidHotelId_ShouldThrowException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> roomService.isRoomNumberExists(null, "101")
        );
        
        assertEquals("Hotel ID cannot be empty", exception.getMessage());
        verify(roomDAO, never()).isRoomNumberExists(anyInt(), anyString());
    }

    @Test
    void getAllAvailableRooms_ShouldReturnAllAvailableRooms() {
        // 准备 - 使用7个参数的构造函数
        Room room1 = new Room(1, "101", "SINGLE", 100.0, 2, true, "Available room 101");
        Room room2 = new Room(2, "201", "DOUBLE", 150.0, 4, true, "Available room 201");
        List<Room> expectedRooms = Arrays.asList(room1, room2);
        
        when(roomDAO.getAllAvailableRooms()).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = roomService.getAllAvailableRooms();
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Room::isAvailable));
        verify(roomDAO, times(1)).getAllAvailableRooms();
    }

    @Test
    void getRoomCountByHotel_ShouldReturnCount() {
        // 准备
        when(roomDAO.getRoomCountByHotel(1)).thenReturn(5);
        
        // 执行
        int result = roomService.getRoomCountByHotel(1);
        
        // 验证
        assertEquals(5, result);
        verify(roomDAO, times(1)).getRoomCountByHotel(1);
    }

    @Test
    void getAvailableRoomCountByHotel_ShouldReturnAvailableCount() {
        // 准备
        when(roomDAO.getAvailableRoomCountByHotel(1)).thenReturn(3);
        
        // 执行
        int result = roomService.getAvailableRoomCountByHotel(1);
        
        // 验证
        assertEquals(3, result);
        verify(roomDAO, times(1)).getAvailableRoomCountByHotel(1);
    }

    @Test
    void updateRoom_WithValidRoom_ShouldReturnTrue() {
        // 准备 - 使用7个参数的构造函数
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, true, "Updated room description");
        room.setId(1);
        
        when(roomDAO.updateRoom(room)).thenReturn(true);
        
        // 执行
        boolean result = roomService.updateRoom(room);
        
        // 验证
        assertTrue(result);
        verify(roomDAO, times(1)).updateRoom(room);
    }

    @Test
    void updateRoom_WithNullRoom_ShouldReturnFalse() {
        // 执行
        boolean result = roomService.updateRoom(null);
        
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

    @Test
    void deleteRoom_WithNonExistingId_ShouldReturnFalse() {
        // 准备
        when(roomDAO.deleteRoom(999)).thenReturn(false);
        
        // 执行
        boolean result = roomService.deleteRoom(999);
        
        // 验证
        assertFalse(result);
        verify(roomDAO, times(1)).deleteRoom(999);
    }

    @Test
    void isRoomAvailable_WithAvailableRoom_ShouldReturnTrue() {
        // 准备 - 使用7个参数的构造函数
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, true, "Available room");
        room.setId(1);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        
        // 执行
        boolean result = roomService.isRoomAvailable(1);
        
        // 验证
        assertTrue(result);
        verify(roomDAO, times(1)).getRoomById(1);
    }

    @Test
    void isRoomAvailable_WithUnavailableRoom_ShouldReturnFalse() {
        // 准备 - 使用7个参数的构造函数
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, false, "Unavailable room");
        room.setId(1);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        
        // 执行
        boolean result = roomService.isRoomAvailable(1);
        
        // 验证
        assertFalse(result);
        verify(roomDAO, times(1)).getRoomById(1);
    }

    @Test
    void isRoomAvailable_WithNonExistingRoom_ShouldThrowException() {
        // 准备
        when(roomDAO.getRoomById(999)).thenReturn(Optional.empty());
        
        // 执行和验证
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> roomService.isRoomAvailable(999)
        );
        
        assertTrue(exception.getMessage().contains("Failed to check room availability"));
        verify(roomDAO, times(1)).getRoomById(999);
    }
}
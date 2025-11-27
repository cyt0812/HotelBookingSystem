package com.hotelbooking.controller;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @Mock
    private RoomService roomService;

    private HotelController hotelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hotelController = new HotelController(hotelService, roomService);
    }

    @Test
    void createHotel_WithValidData_ShouldReturnHotel() {
        // 准备
        Hotel expectedHotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", 10);
        expectedHotel.setId(1);
        
        when(hotelService.createHotel("Grand Hotel", "New York", "Luxury hotel", 10))
                .thenReturn(expectedHotel);
        
        // 执行
        Hotel result = hotelController.createHotel("Grand Hotel", "New York", "Luxury hotel", 10);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Grand Hotel", result.getName());
        verify(hotelService, times(1)).createHotel("Grand Hotel", "New York", "Luxury hotel", 10);
    }

    @Test
    void getHotelById_WithExistingHotel_ShouldReturnHotel() {
        // 准备
        Hotel expectedHotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", 10);
        expectedHotel.setId(1);
        
        when(hotelService.getHotelById(1)).thenReturn(Optional.of(expectedHotel));
        
        // 执行
        Hotel result = hotelController.getHotelById(1);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Grand Hotel", result.getName());
        verify(hotelService, times(1)).getHotelById(1);
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", 10);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelService.getAllHotels()).thenReturn(expectedHotels);
        
        // 执行
        List<Hotel> result = hotelController.getAllHotels();
        
        // 验证
        assertEquals(2, result.size());
        verify(hotelService, times(1)).getAllHotels();
    }

    @Test
    void getHotelsByLocation_ShouldReturnMatchingHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel NY", "New York", "Desc", 5);
        Hotel hotel2 = new Hotel("Another NY", "New York", "Desc", 8);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelService.getHotelsByLocation("New York")).thenReturn(expectedHotels);
        
        // 执行
        List<Hotel> result = hotelController.getHotelsByLocation("New York");
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(h -> h.getLocation().equals("New York")));
        verify(hotelService, times(1)).getHotelsByLocation("New York");
    }

    @Test
    void updateHotel_WithValidHotel_ShouldReturnTrue() {
        // 准备
        Hotel hotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", 10);
        hotel.setId(1);
        
        when(hotelService.updateHotel(hotel)).thenReturn(true);
        
        // 执行
        boolean result = hotelController.updateHotel(hotel);
        
        // 验证
        assertTrue(result);
        verify(hotelService, times(1)).updateHotel(hotel);
    }

    @Test
    void deleteHotel_WithExistingHotel_ShouldReturnTrue() {
        // 准备
        when(hotelService.deleteHotel(1)).thenReturn(true);
        
        // 执行
        boolean result = hotelController.deleteHotel(1);
        
        // 验证
        assertTrue(result);
        verify(hotelService, times(1)).deleteHotel(1);
    }

    @Test
    void createRoom_WithValidData_ShouldReturnRoom() {
        // 准备
        Room expectedRoom = new Room(1, "101", "SINGLE", new BigDecimal("99.99"), true);
        expectedRoom.setId(1);
        
        when(roomService.createRoom(1, "101", "SINGLE", new BigDecimal("99.99"), true))
                .thenReturn(expectedRoom);
        
        // 执行
        Room result = hotelController.createRoom(1, "101", "SINGLE", new BigDecimal("99.99"), true);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("101", result.getRoomNumber());
        verify(roomService, times(1)).createRoom(1, "101", "SINGLE", new BigDecimal("99.99"), true);
    }

    @Test
    void getRoomsByHotel_ShouldReturnHotelRooms() {
        // 准备
        Room room1 = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        Room room2 = new Room(1, "102", "DOUBLE", new BigDecimal("150"), true);
        List<Room> expectedRooms = Arrays.asList(room1, room2);
        
        when(roomService.getRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = hotelController.getRoomsByHotel(1);
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(room -> room.getHotelId().equals(1)));
        verify(roomService, times(1)).getRoomsByHotelId(1);
    }

    @Test
    void getAvailableRoomsByHotel_ShouldReturnAvailableRooms() {
        // 准备
        Room availableRoom = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        Room unavailableRoom = new Room(1, "102", "DOUBLE", new BigDecimal("150"), false);
        List<Room> expectedRooms = Arrays.asList(availableRoom);
        
        when(roomService.getAvailableRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        List<Room> result = hotelController.getAvailableRoomsByHotel(1);
        
        // 验证
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
        verify(roomService, times(1)).getAvailableRoomsByHotelId(1);
    }
}
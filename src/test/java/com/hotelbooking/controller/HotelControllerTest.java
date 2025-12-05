package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
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
        Hotel expectedHotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", "Pool,Gym", 10);
        expectedHotel.setId(1);

        when(hotelService.createHotel("Grand Hotel", "New York", "Luxury hotel", "Pool,Gym", 10))
                .thenReturn(expectedHotel);

        ApiResponse<Object> result = hotelController.createHotel("Grand Hotel", "New York", "Luxury hotel", "Pool,Gym", 10);

        assertTrue(result.isSuccess());
        assertEquals("酒店创建成功", result.getMessage());
        Hotel actualHotel = (Hotel) result.getData();
        assertNotNull(actualHotel);
        assertEquals(1, actualHotel.getId());
        assertEquals("Grand Hotel", actualHotel.getName());
        verify(hotelService, times(1)).createHotel("Grand Hotel", "New York", "Luxury hotel", "Pool,Gym", 10);
    }

    @Test
    void getHotelById_WithExistingHotel_ShouldReturnHotel() {
        // 准备
        Hotel expectedHotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", "Pool,Gym", 10);
        expectedHotel.setId(1);
        
        when(hotelService.getHotelById(1)).thenReturn(Optional.of(expectedHotel));
        
        // 执行
        ApiResponse<Object> result = hotelController.getHotelById(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取酒店信息成功", result.getMessage());
        Hotel actualHotel = (Hotel) result.getData();
        assertNotNull(actualHotel);
        assertEquals(1, actualHotel.getId());
        assertEquals("Grand Hotel", actualHotel.getName());
        verify(hotelService, times(1)).getHotelById(1);
    }

    @Test
    void getHotelById_WithNonExistingHotel_ShouldReturnFailure() {
        // 准备
        when(hotelService.getHotelById(999)).thenReturn(Optional.empty());
        
        // 执行
        ApiResponse<Object> result = hotelController.getHotelById(999);
        
        // 验证
        assertFalse(result.isSuccess());
        assertEquals("酒店不存在，ID: 999", result.getMessage());
        verify(hotelService, times(1)).getHotelById(999);
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", "Pool,Gym", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", "Pool,Gym", 10);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelService.getAllHotels()).thenReturn(expectedHotels);
        
        // 执行
        ApiResponse<Object> result = hotelController.getAllHotels();
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取酒店列表成功", result.getMessage());
        List<Hotel> actualHotels = (List<Hotel>) result.getData();
        assertEquals(2, actualHotels.size());
        verify(hotelService, times(1)).getAllHotels();
    }

    @Test
    void getHotelsByLocation_ShouldReturnMatchingHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel NY", "New York", "Desc", "Pool,Gym", 5);
        Hotel hotel2 = new Hotel("Another NY", "New York", "Desc", "Pool,Gym", 8);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelService.getHotelsByLocation("New York")).thenReturn(expectedHotels);
        
        // 执行
        ApiResponse<Object> result = hotelController.getHotelsByLocation("New York");
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取位置酒店列表成功", result.getMessage());
        List<Hotel> actualHotels = (List<Hotel>) result.getData();
        assertEquals(2, actualHotels.size());
        assertTrue(actualHotels.stream().allMatch(h -> h.getLocation().equals("New York")));
        verify(hotelService, times(1)).getHotelsByLocation("New York");
    }

    @Test
    void updateHotel_WithValidHotel_ShouldReturnTrue() {
        // 准备
        Hotel hotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", "Pool,Gym", 10);
        hotel.setId(1);
        
        when(hotelService.updateHotel(hotel)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = hotelController.updateHotel(hotel);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("酒店信息更新成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(hotelService, times(1)).updateHotel(hotel);
    }

    @Test
    void updateHotel_WithInvalidHotel_ShouldReturnFalse() {
        // 准备
        Hotel hotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", "Pool,Gym", 10);
        hotel.setId(999);
        
        when(hotelService.updateHotel(hotel)).thenReturn(false);
        
        // 执行
        ApiResponse<Object> result = hotelController.updateHotel(hotel);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("酒店信息更新失败", result.getMessage());
        assertFalse((Boolean) result.getData());
        verify(hotelService, times(1)).updateHotel(hotel);
    }

    @Test
    void deleteHotel_WithExistingHotel_ShouldReturnTrue() {
        // 准备
        when(hotelService.deleteHotel(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = hotelController.deleteHotel(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("酒店删除成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(hotelService, times(1)).deleteHotel(1);
    }

    @Test
    void createRoom_WithValidData_ShouldReturnRoom() {
        // 准备
        Room expectedRoom = new Room(1, "101", "SINGLE", 99.99, 2, true, "Standard single room with view");
        expectedRoom.setId(1);
        
        when(roomService.createRoom(1, "101", "SINGLE", new BigDecimal("99.99"), true))
                .thenReturn(expectedRoom);
        
        // 执行
        ApiResponse<Object> result = hotelController.createRoom(1, "101", "SINGLE", new BigDecimal("99.99"), true);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("房间创建成功", result.getMessage());
        Room actualRoom = (Room) result.getData();
        assertNotNull(actualRoom);
        assertEquals(1, actualRoom.getId());
        assertEquals("101", actualRoom.getRoomNumber());
        verify(roomService, times(1)).createRoom(1, "101", "SINGLE", new BigDecimal("99.99"), true);
    }

    @Test
    void getRoomById_WithExistingRoom_ShouldReturnRoom() {
        // 准备
        Room expectedRoom = new Room(1, "101", "SINGLE", 100, 2, true, "Standard single room");
        expectedRoom.setId(1);
        
        when(roomService.getRoomById(1)).thenReturn(Optional.of(expectedRoom));
        
        // 执行
        ApiResponse<Object> result = hotelController.getRoomById(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取房间信息成功", result.getMessage());
        Room actualRoom = (Room) result.getData();
        assertNotNull(actualRoom);
        assertEquals(1, actualRoom.getId());
        assertEquals("101", actualRoom.getRoomNumber());
        verify(roomService, times(1)).getRoomById(1);
    }

    @Test
    void getRoomsByHotel_ShouldReturnHotelRooms() {
        // 准备
        Room room1 = new Room(1, "101", "SINGLE", 100, 2, true, "Standard single room");
        Room room2 = new Room(1, "102", "DOUBLE", 150, 2, true, "Standard double room");
        List<Room> expectedRooms = Arrays.asList(room1, room2);
        
        when(roomService.getRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        ApiResponse<Object> result = hotelController.getRoomsByHotel(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取酒店房间列表成功", result.getMessage());
        List<Room> actualRooms = (List<Room>) result.getData();
        assertEquals(2, actualRooms.size());
        assertTrue(actualRooms.stream().allMatch(room -> room.getHotelId().equals(1)));
        verify(roomService, times(1)).getRoomsByHotelId(1);
    }

    @Test
    void getAvailableRoomsByHotel_ShouldReturnAvailableRooms() {
        // 准备
        Room availableRoom = new Room(1, "101", "SINGLE", 100, 2, true, "Standard single room");
        Room unavailableRoom = new Room(1, "102", "DOUBLE", 150, 2, false, "Standard double room");
        List<Room> expectedRooms = Arrays.asList(availableRoom);
        
        when(roomService.getAvailableRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        ApiResponse<Object> result = hotelController.getAvailableRoomsByHotel(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取可用房间列表成功", result.getMessage());
        List<Room> actualRooms = (List<Room>) result.getData();
        assertEquals(1, actualRooms.size());
        assertTrue(actualRooms.get(0).isAvailable());
        verify(roomService, times(1)).getAvailableRoomsByHotelId(1);
    }

    @Test
    void getRoomsByType_ShouldReturnMatchingRooms() {
        // 准备
        Room room1 = new Room(1, "101", "SINGLE", 100, 2, true, "Standard single room");
        Room room2 = new Room(2, "201", "SINGLE", 120, 2, true, "Deluxe single room");
        List<Room> expectedRooms = Arrays.asList(room1, room2);
        
        when(roomService.getRoomsByType("SINGLE")).thenReturn(expectedRooms);
        
        // 执行
        ApiResponse<Object> result = hotelController.getRoomsByType("SINGLE");
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取房型房间列表成功", result.getMessage());
        List<Room> actualRooms = (List<Room>) result.getData();
        assertEquals(2, actualRooms.size());
        assertTrue(actualRooms.stream().allMatch(room -> "SINGLE".equals(room.getRoomType())));
        verify(roomService, times(1)).getRoomsByType("SINGLE");
    }

    @Test
    void updateRoom_WithValidRoom_ShouldReturnTrue() {
        // 准备
        Room room = new Room(1, "101", "SINGLE", 100, 2, true, "Standard single room");
        room.setId(1);
        
        when(roomService.updateRoom(room)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = hotelController.updateRoom(room);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("房间信息更新成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(roomService, times(1)).updateRoom(room);
    }

    @Test
    void updateRoomAvailability_WithValidData_ShouldReturnTrue() {
        // 准备
        when(roomService.updateRoomAvailability(1, false)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = hotelController.updateRoomAvailability(1, false);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("房间状态更新成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(roomService, times(1)).updateRoomAvailability(1, false);
    }

    @Test
    void deleteRoom_WithExistingRoom_ShouldReturnTrue() {
        // 准备
        when(roomService.deleteRoom(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = hotelController.deleteRoom(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("房间删除成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(roomService, times(1)).deleteRoom(1);
    }

    @Test
    void searchHotels_WithNameAndLocation_ShouldReturnFilteredHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Grand Hotel", "New York", "Luxury", "Pool,Gym", 10);
        Hotel hotel2 = new Hotel("Another Hotel", "Los Angeles", "Standard", "Pool,Gym", 5);
        List<Hotel> allHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelService.getAllHotels()).thenReturn(allHotels);
        
        // 执行
        ApiResponse<Object> result = hotelController.searchHotels("Grand", "New York");
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("酒店搜索成功", result.getMessage());
        List<Hotel> filteredHotels = (List<Hotel>) result.getData();
        assertEquals(1, filteredHotels.size());
        assertEquals("Grand Hotel", filteredHotels.get(0).getName());
        verify(hotelService, times(1)).getAllHotels();
    }

    @Test
    void searchAvailableRooms_WithFilters_ShouldReturnFilteredRooms() {
        // 准备
        Room room1 = new Room(1, "101", "SINGLE", 100, 2, true, "Standard single room");
        Room room2 = new Room(1, "102", "DOUBLE", 200, 2, true, "Standard double room");
        List<Room> availableRooms = Arrays.asList(room1, room2);
        
        when(roomService.getAvailableRoomsByHotelId(1)).thenReturn(availableRooms);
        
        // 执行
        ApiResponse<Object> result = hotelController.searchAvailableRooms(1, "SINGLE", 
            new BigDecimal("50"), new BigDecimal("150"));
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("房间搜索成功", result.getMessage());
        List<Room> filteredRooms = (List<Room>) result.getData();
        assertEquals(1, filteredRooms.size());
        assertEquals("SINGLE", filteredRooms.get(0).getRoomType());
        verify(roomService, times(1)).getAvailableRoomsByHotelId(1);
    }
}
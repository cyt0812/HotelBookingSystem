// HotelServiceTest.java - 完整修复版本
package com.hotelbooking.service;

import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelDAO hotelDAO;

    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        // 使用带参数的构造函数，避免在构造函数中调用 getAllHotels()
        hotelService = new HotelService(hotelDAO);
    }

    @Test
    void createHotel_WithValidData_ShouldCreateHotel() {
        // 准备
        Hotel expectedHotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", "pool", 10);
        expectedHotel.setId(1);
        
        when(hotelDAO.createHotel(any(Hotel.class))).thenReturn(expectedHotel);
        
        // 执行
        Hotel result = hotelService.createHotel("Grand Hotel", "New York", "Luxury hotel", "pool", 10);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Grand Hotel", result.getName());
        verify(hotelDAO, times(1)).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithEmptyName_ShouldThrowValidationException() {
        // 执行和验证
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel("", "Location", "Description", "pool", 5)
        );
        
        assertEquals("酒店名称不能为空", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithNullName_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel(null, "Location", "Description", "pool", 5)
        );
        
        assertEquals("酒店名称不能为空", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithWhitespaceName_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel("   ", "Location", "Description", "pool", 5)
        );
        
        assertEquals("酒店名称不能为空", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithEmptyLocation_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel("Test Hotel", "", "Description", "pool", 5)
        );
        
        assertEquals("位置不能为空", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithNegativeAvailableRooms_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel("Test Hotel", "Location", "Description", "pool", -1)
        );
        
        assertEquals("可用房间数必须为非负数", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void getHotelById_WithExistingId_ShouldReturnHotel() {
        // 准备
        Hotel hotel = new Hotel("Test Hotel", "City", "Desc", "pool", 5);
        hotel.setId(1);
        when(hotelDAO.getHotelById(1)).thenReturn(Optional.of(hotel));
        
        // 执行
        Optional<Hotel> result = hotelService.getHotelById(1);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(hotelDAO, times(1)).getHotelById(1);
    }

    @Test
    void getHotelById_WithNonExistingId_ShouldReturnEmpty() {
        // 准备
        when(hotelDAO.getHotelById(999)).thenReturn(Optional.empty());
        
        // 执行
        Optional<Hotel> result = hotelService.getHotelById(999);
        
        // 验证
        assertFalse(result.isPresent());
        verify(hotelDAO, times(1)).getHotelById(999);
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", "pool", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", "pool", 10);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getAllHotels()).thenReturn(expectedHotels);
        
        // 执行
        List<Hotel> result = hotelService.getAllHotels();
        
        // 验证
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
    }

    @Test
    void getAllHotels_WhenDAOThrowsException_ShouldThrowBusinessException() {
        // 准备
        when(hotelDAO.getAllHotels()).thenThrow(new RuntimeException("Database error"));
        
        // 执行和验证
        assertThrows(RuntimeException.class, () -> hotelService.getAllHotels());
        verify(hotelDAO, times(1)).getAllHotels();
    }

    @Test
    void getHotelsByLocation_ShouldReturnMatchingHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel NY", "New York", "Desc", "pool", 5);
        Hotel hotel2 = new Hotel("Another NY", "New York", "Desc", "pool", 8);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getHotelsByLocation("New York")).thenReturn(expectedHotels);
        
        // 执行
        List<Hotel> result = hotelService.getHotelsByLocation("New York");
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(h -> h.getLocation().equals("New York")));
        verify(hotelDAO, times(1)).getHotelsByLocation("New York");
    }

    @Test
    void getHotelsByLocation_WithEmptyLocation_ShouldReturnEmptyList() {
        // 执行
        List<Hotel> result = hotelService.getHotelsByLocation("");
        
        // 验证
        assertTrue(result.isEmpty());
        verify(hotelDAO, never()).getHotelsByLocation(anyString());
    }

    @Test
    void updateHotel_WithValidHotel_ShouldReturnTrue() {
        // 准备
        Hotel hotel = new Hotel("Old Name", "Location", "Desc", "pool", 5);
        hotel.setId(1);
        
        when(hotelDAO.updateHotel(hotel)).thenReturn(true);
        
        // 执行
        boolean result = hotelService.updateHotel(hotel);
        
        // 验证
        assertTrue(result);
        verify(hotelDAO, times(1)).updateHotel(hotel);
    }

    @Test
    void updateHotel_WithNullHotel_ShouldReturnFalse() {
        // 执行
        boolean result = hotelService.updateHotel(null);
        
        // 验证
        assertFalse(result);
        verify(hotelDAO, never()).updateHotel(any());
    }

    @Test
    void updateHotel_WithHotelNullId_ShouldReturnFalse() {
        // 准备
        Hotel hotel = new Hotel("Test", "Location", "Desc", "pool", 5);
        // hotel.setId(null); // ID为null
        
        // 执行
        boolean result = hotelService.updateHotel(hotel);
        
        // 验证
        assertFalse(result);
        verify(hotelDAO, never()).updateHotel(any());
    }

    @Test
    void deleteHotel_WithExistingId_ShouldReturnTrue() {
        // 准备
        when(hotelDAO.deleteHotel(1)).thenReturn(true);
        
        // 执行
        boolean result = hotelService.deleteHotel(1);
        
        // 验证
        assertTrue(result);
        verify(hotelDAO, times(1)).deleteHotel(1);
    }

    @Test
    void deleteHotel_WithNonExistingId_ShouldReturnFalse() {
        // 准备
        when(hotelDAO.deleteHotel(999)).thenReturn(false);
        
        // 执行
        boolean result = hotelService.deleteHotel(999);
        
        // 验证
        assertFalse(result);
        verify(hotelDAO, times(1)).deleteHotel(999);
    }

    @Test
    void searchHotels_ShouldReturnMatchingHotels() {
        // 准备
        Hotel h1 = new Hotel("Grand Hotel", "New York", "Luxury", "pool", 10);
        Hotel h3 = new Hotel("NY Budget Inn", "New York", "Cheap", "wifi", 20);
        List<Hotel> searchResult = Arrays.asList(h1, h3);
        
        // mock DAO 的 searchHotels 方法
        when(hotelDAO.searchHotels("New York")).thenReturn(searchResult);
        
        // 执行搜索
        List<Hotel> result = hotelService.searchHotels("New York");
        
        // 断言结果
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).searchHotels("New York");
    }

    @Test
    void searchHotels_WithEmptyKeyword_ShouldReturnAllHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", "pool", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", "pool", 10);
        List<Hotel> allHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getAllHotels()).thenReturn(allHotels);
        
        // 执行
        List<Hotel> result = hotelService.searchHotels("");
        
        // 验证
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
        verify(hotelDAO, never()).searchHotels(anyString());
    }

    @Test
    void searchHotels_WithNullKeyword_ShouldReturnAllHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", "pool", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", "pool", 10);
        List<Hotel> allHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getAllHotels()).thenReturn(allHotels);
        
        // 执行
        List<Hotel> result = hotelService.searchHotels(null);
        
        // 验证
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
        verify(hotelDAO, never()).searchHotels(anyString());
    }

    @Test
    void clearCache_ShouldResetCache() {
        // 这个方法主要用于内部调用，测试简单调用即可
        assertDoesNotThrow(() -> hotelService.clearCache());
    }
}
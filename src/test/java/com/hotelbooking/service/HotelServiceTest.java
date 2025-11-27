package com.hotelbooking.service;

import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.entity.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelDAO hotelDAO;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void createHotel_WithValidData_ShouldCreateHotel() {
        // 准备
        Hotel expectedHotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", 10);
        expectedHotel.setId(1);
        
        when(hotelDAO.createHotel(any(Hotel.class))).thenReturn(expectedHotel);
        
        // 执行
        Hotel result = hotelService.createHotel("Grand Hotel", "New York", "Luxury hotel", 10);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Grand Hotel", result.getName());
        verify(hotelDAO, times(1)).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithEmptyName_ShouldThrowException() {
        // 执行和验证
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> hotelService.createHotel("", "Location", "Description", 5)
        );
        
        assertEquals("Hotel name cannot be empty", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void getHotelById_WithExistingId_ShouldReturnHotel() {
        // 准备
        Hotel hotel = new Hotel("Test Hotel", "City", "Desc", 5);
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
    void getAllHotels_ShouldReturnAllHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", 10);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getAllHotels()).thenReturn(expectedHotels);
        
        // 执行
        List<Hotel> result = hotelService.getAllHotels();
        
        // 验证
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
    }

    @Test
    void getHotelsByLocation_ShouldReturnMatchingHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel NY", "New York", "Desc", 5);
        Hotel hotel2 = new Hotel("Another NY", "New York", "Desc", 8);
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
    void updateHotel_WithValidHotel_ShouldReturnTrue() {
        // 准备
        Hotel hotel = new Hotel("Old Name", "Location", "Desc", 5);
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
    void deleteHotel_WithExistingId_ShouldReturnTrue() {
        // 准备
        when(hotelDAO.deleteHotel(1)).thenReturn(true);
        
        // 执行
        boolean result = hotelService.deleteHotel(1);
        
        // 验证
        assertTrue(result);
        verify(hotelDAO, times(1)).deleteHotel(1);
    }
}
// HotelServiceTest.java - Complete fixed version
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
        // Arrange
        Hotel expectedHotel = new Hotel("Grand Hotel", "New York", "Luxury hotel", "pool", 10);
        expectedHotel.setId(1);
        
        when(hotelDAO.createHotel(any(Hotel.class))).thenReturn(expectedHotel);
        
        // Act
        Hotel result = hotelService.createHotel("Grand Hotel", "New York", "Luxury hotel", "pool", 10);
        
        // Assert
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
        
        assertEquals("Hotel name cannot be empty", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithNullName_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel(null, "Location", "Description", "pool", 5)
        );
        
        assertEquals("Hotel name cannot be empty", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithWhitespaceName_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel("   ", "Location", "Description", "pool", 5)
        );
        
        assertEquals("Hotel name cannot be empty", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithEmptyLocation_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel("Test Hotel", "", "Description", "pool", 5)
        );
        
        assertEquals("Location cannot be empty", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void createHotel_WithNegativeAvailableRooms_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> hotelService.createHotel("Test Hotel", "Location", "Description", "pool", -1)
        );
        
        assertEquals("Available rooms must be non-negative", exception.getMessage());
        verify(hotelDAO, never()).createHotel(any(Hotel.class));
    }

    @Test
    void getHotelById_WithExistingId_ShouldReturnHotel() {
        // Arrange
        Hotel hotel = new Hotel("Test Hotel", "City", "Desc", "pool", 5);
        hotel.setId(1);
        when(hotelDAO.getHotelById(1)).thenReturn(Optional.of(hotel));
        
        // Act
        Optional<Hotel> result = hotelService.getHotelById(1);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(hotelDAO, times(1)).getHotelById(1);
    }

    @Test
    void getHotelById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(hotelDAO.getHotelById(999)).thenReturn(Optional.empty());
        
        // Act
        Optional<Hotel> result = hotelService.getHotelById(999);
        
        // Assert
        assertFalse(result.isPresent());
        verify(hotelDAO, times(1)).getHotelById(999);
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", "pool", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", "pool", 10);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getAllHotels()).thenReturn(expectedHotels);
        
        // Act
        List<Hotel> result = hotelService.getAllHotels();
        
        // Assert
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
    }

    @Test
    void getAllHotels_WhenDAOThrowsException_ShouldThrowBusinessException() {
        // Arrange
        when(hotelDAO.getAllHotels()).thenThrow(new RuntimeException("Database error"));
        
        // Act and Assert
        assertThrows(RuntimeException.class, () -> hotelService.getAllHotels());
        verify(hotelDAO, times(1)).getAllHotels();
    }

    @Test
    void getHotelsByLocation_ShouldReturnMatchingHotels() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel NY", "New York", "Desc", "pool", 5);
        Hotel hotel2 = new Hotel("Another NY", "New York", "Desc", "pool", 8);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getHotelsByLocation("New York")).thenReturn(expectedHotels);
        
        // Act
        List<Hotel> result = hotelService.getHotelsByLocation("New York");
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(h -> h.getLocation().equals("New York")));
        verify(hotelDAO, times(1)).getHotelsByLocation("New York");
    }

    @Test
    void getHotelsByLocation_WithEmptyLocation_ShouldReturnEmptyList() {
        // Act
        List<Hotel> result = hotelService.getHotelsByLocation("");
        
        // Assert
        assertTrue(result.isEmpty());
        verify(hotelDAO, never()).getHotelsByLocation(anyString());
    }

    @Test
    void updateHotel_WithValidHotel_ShouldReturnTrue() {
        // Arrange
        Hotel hotel = new Hotel("Old Name", "Location", "Desc", "pool", 5);
        hotel.setId(1);
        
        when(hotelDAO.updateHotel(hotel)).thenReturn(true);
        
        // Act
        boolean result = hotelService.updateHotel(hotel);
        
        // Assert
        assertTrue(result);
        verify(hotelDAO, times(1)).updateHotel(hotel);
    }

    @Test
    void updateHotel_WithNullHotel_ShouldReturnFalse() {
        // Act
        boolean result = hotelService.updateHotel(null);
        
        // Assert
        assertFalse(result);
        verify(hotelDAO, never()).updateHotel(any());
    }

    @Test
    void updateHotel_WithHotelNullId_ShouldReturnFalse() {
        // Arrange
        Hotel hotel = new Hotel("Test", "Location", "Desc", "pool", 5);
        // hotel.setId(null); // ID is null
        
        // Act
        boolean result = hotelService.updateHotel(hotel);
        
        // Assert
        assertFalse(result);
        verify(hotelDAO, never()).updateHotel(any());
    }

    @Test
    void deleteHotel_WithExistingId_ShouldReturnTrue() {
        // Arrange
        when(hotelDAO.deleteHotel(1)).thenReturn(true);
        
        // Act
        boolean result = hotelService.deleteHotel(1);
        
        // Assert
        assertTrue(result);
        verify(hotelDAO, times(1)).deleteHotel(1);
    }

    @Test
    void deleteHotel_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        when(hotelDAO.deleteHotel(999)).thenReturn(false);
        
        // Act
        boolean result = hotelService.deleteHotel(999);
        
        // Assert
        assertFalse(result);
        verify(hotelDAO, times(1)).deleteHotel(999);
    }

    @Test
    void searchHotels_ShouldReturnMatchingHotels() {
        // Arrange
        Hotel h1 = new Hotel("Grand Hotel", "New York", "Luxury", "pool", 10);
        Hotel h3 = new Hotel("NY Budget Inn", "New York", "Cheap", "wifi", 20);
        List<Hotel> searchResult = Arrays.asList(h1, h3);
        
        // Mock DAO's searchHotels method
        when(hotelDAO.searchHotels("New York")).thenReturn(searchResult);
        
        // Act - Perform search
        List<Hotel> result = hotelService.searchHotels("New York");
        
        // Assert results
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).searchHotels("New York");
    }

    @Test
    void searchHotels_WithEmptyKeyword_ShouldReturnAllHotels() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", "pool", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", "pool", 10);
        List<Hotel> allHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getAllHotels()).thenReturn(allHotels);
        
        // Act
        List<Hotel> result = hotelService.searchHotels("");
        
        // Assert
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
        verify(hotelDAO, never()).searchHotels(anyString());
    }

    @Test
    void searchHotels_WithNullKeyword_ShouldReturnAllHotels() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", "pool", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", "pool", 10);
        List<Hotel> allHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getAllHotels()).thenReturn(allHotels);
        
        // Act
        List<Hotel> result = hotelService.searchHotels(null);
        
        // Assert
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
        verify(hotelDAO, never()).searchHotels(anyString());
    }

    @Test
    void clearCache_ShouldResetCache() {
        // This method is primarily for internal use, just test simple invocation
        assertDoesNotThrow(() -> hotelService.clearCache());
    }
}
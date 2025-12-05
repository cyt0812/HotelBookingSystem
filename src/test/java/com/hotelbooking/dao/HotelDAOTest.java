package com.hotelbooking.dao;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class HotelDAOTest {

    private HotelDAO hotelDAO;

    @BeforeEach
    void setUp() {
        DatabaseInitializer.initializeDatabase();
        hotelDAO = new HotelDAO();
    }

    @Test
    void createHotel_WithValidHotel_ShouldReturnHotelWithId() {
        // 准备
        Hotel hotel = new Hotel("Test Hotel", "Test City", "Test Description","gym pool", 10);
        
        // 执行
        Hotel result = hotelDAO.createHotel(hotel);
        
        // 验证
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test Hotel", result.getName());
        assertEquals("Test City", result.getLocation());
        assertEquals(10, result.getAvailableRooms());
    }

    @Test
    void getHotelById_WithExistingId_ShouldReturnHotel() {
        // 先创建酒店
        Hotel hotel = hotelDAO.createHotel(new Hotel("Existing Hotel", "City", "Desc","gym pool", 5));
        
        // 执行
        Optional<Hotel> result = hotelDAO.getHotelById(hotel.getId());
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(hotel.getId(), result.get().getId());
        assertEquals("Existing Hotel", result.get().getName());
    }

    @Test
    void getHotelById_WithNonExistingId_ShouldReturnEmpty() {
        // 执行
        Optional<Hotel> result = hotelDAO.getHotelById(999);
        
        // 验证
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        // 准备 - 创建几个测试酒店
        hotelDAO.createHotel(new Hotel("Hotel A", "City A", "Desc A","gym bar", 5));
        hotelDAO.createHotel(new Hotel("Hotel B", "City B", "Desc B","pool", 10));
        
        // 执行
        List<Hotel> hotels = hotelDAO.getAllHotels();
        
        // 验证
        assertNotNull(hotels);
        assertTrue(hotels.size() >= 2);
    }

    @Test
    void getHotelsByLocation_ShouldReturnMatchingHotels() {
        // 准备
        hotelDAO.createHotel(new Hotel("Hotel NY", "New York", "NY Hotel","gym bar", 5));
        hotelDAO.createHotel(new Hotel("Hotel LA", "Los Angeles", "LA Hotel","gym bar", 10));
        hotelDAO.createHotel(new Hotel("Another NY Hotel", "New York", "Another NY","gym bar", 8));
        
        // 执行
        List<Hotel> nyHotels = hotelDAO.getHotelsByLocation("New York");
        
        // 验证
        assertNotNull(nyHotels);
        assertEquals(2, nyHotels.size());
        assertTrue(nyHotels.stream().allMatch(h -> h.getLocation().equals("New York")));
    }

    @Test
    void updateHotel_ShouldUpdateHotelData() {
        // 准备
        Hotel hotel = hotelDAO.createHotel(new Hotel("Old Name", "Old Location", "Old Desc","gym bar", 5));
        
        // 执行 - 更新酒店信息
        hotel.setName("New Name");
        hotel.setLocation("New Location");
        hotel.setAvailableRooms(10);
        boolean updated = hotelDAO.updateHotel(hotel);
        
        // 验证
        assertTrue(updated);
        Optional<Hotel> retrievedHotel = hotelDAO.getHotelById(hotel.getId());
        assertTrue(retrievedHotel.isPresent());
        assertEquals("New Name", retrievedHotel.get().getName());
        assertEquals("New Location", retrievedHotel.get().getLocation());
        assertEquals(10, retrievedHotel.get().getAvailableRooms());
    }

    @Test
    void deleteHotel_ShouldRemoveHotel() {
        // 准备
        Hotel hotel = hotelDAO.createHotel(new Hotel("To Delete", "Location", "Desc","gym bar", 5));
        
        // 执行
        boolean deleted = hotelDAO.deleteHotel(hotel.getId());
        
        // 验证
        assertTrue(deleted);
        Optional<Hotel> retrievedHotel = hotelDAO.getHotelById(hotel.getId());
        assertTrue(retrievedHotel.isEmpty());
    }
}
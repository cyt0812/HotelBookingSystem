package com.hotelbooking.controller;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.User;
import com.hotelbooking.service.AdminService;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private UserService userService;

    @Mock
    private HotelService hotelService;

    @Mock
    private BookingService bookingService;

    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(adminService, userService, hotelService, bookingService);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // 准备
        User user1 = new User("user1", "user1@test.com", "pass", "CUSTOMER");
        User user2 = new User("admin", "admin@test.com", "pass", "ADMIN");
        List<User> expectedUsers = Arrays.asList(user1, user2);
        
        when(adminService.getAllUsers()).thenReturn(expectedUsers);
        
        // 执行
        List<User> result = adminController.getAllUsers();
        
        // 验证
        assertEquals(2, result.size());
        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        // 准备
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B", 10);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(adminService.getAllHotels()).thenReturn(expectedHotels);
        
        // 执行
        List<Hotel> result = adminController.getAllHotels();
        
        // 验证
        assertEquals(2, result.size());
        verify(adminService, times(1)).getAllHotels();
    }

    @Test
    void getAllBookings_ShouldReturnAllBookings() {
        // 准备
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CONFIRMED");
        Booking booking2 = new Booking(2, 1, 1, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), 
                                     new BigDecimal("150"), "PAID");
        List<Booking> expectedBookings = Arrays.asList(booking1, booking2);
        
        when(adminService.getAllBookings()).thenReturn(expectedBookings);
        
        // 执行
        List<Booking> result = adminController.getAllBookings();
        
        // 验证
        assertEquals(2, result.size());
        verify(adminService, times(1)).getAllBookings();
    }

    @Test
    void getBookingStatistics_ShouldReturnStatistics() {
        // 准备
        String expectedStats = "=== Booking Statistics ===\nTotal Hotels: 5\nTotal Bookings: 20";
        when(adminService.getBookingStatistics()).thenReturn(expectedStats);
        
        // 执行
        String result = adminController.getBookingStatistics();
        
        // 验证
        assertEquals(expectedStats, result);
        verify(adminService, times(1)).getBookingStatistics();
    }

    @Test
    void updateUserRole_WithValidData_ShouldReturnSuccess() {
        // 准备
        when(adminService.updateUserRole(1, "ADMIN")).thenReturn(true);
        
        // 执行
        boolean result = adminController.updateUserRole(1, "ADMIN");
        
        // 验证
        assertTrue(result);
        verify(adminService, times(1)).updateUserRole(1, "ADMIN");
    }

    @Test
    void deleteUser_WithExistingUser_ShouldReturnTrue() {
        // 准备
        when(userService.deleteUser(1)).thenReturn(true);
        
        // 执行
        boolean result = adminController.deleteUser(1);
        
        // 验证
        assertTrue(result);
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void deleteHotel_WithExistingHotel_ShouldReturnTrue() {
        // 准备
        when(adminService.deleteHotel(1)).thenReturn(true);
        
        // 执行
        boolean result = adminController.deleteHotel(1);
        
        // 验证
        assertTrue(result);
        verify(adminService, times(1)).deleteHotel(1);
    }

    @Test
    void cancelBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        when(adminService.cancelUserBooking(1)).thenReturn(true);
        
        // 执行
        boolean result = adminController.cancelBooking(1);
        
        // 验证
        assertTrue(result);
        verify(adminService, times(1)).cancelUserBooking(1);
    }

    @Test
    void getTotalRevenue_ShouldReturnRevenue() {
        // 准备
        BigDecimal expectedRevenue = new BigDecimal("5000.00");
        when(adminService.getTotalRevenue()).thenReturn(expectedRevenue);
        
        // 执行
        BigDecimal result = adminController.getTotalRevenue();
        
        // 验证
        assertEquals(expectedRevenue, result);
        verify(adminService, times(1)).getTotalRevenue();
    }

    @Test
    void getMostPopularHotel_ShouldReturnHotelInfo() {
        // 准备
        String expectedInfo = "Grand Hotel (25 bookings)";
        when(adminService.getMostPopularHotel()).thenReturn(expectedInfo);
        
        // 执行
        String result = adminController.getMostPopularHotel();
        
        // 验证
        assertEquals(expectedInfo, result);
        verify(adminService, times(1)).getMostPopularHotel();
    }
}
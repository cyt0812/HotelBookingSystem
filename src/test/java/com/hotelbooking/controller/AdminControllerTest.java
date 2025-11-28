package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
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
        ApiResponse<Object> result = adminController.getAllUsers();
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取用户列表成功", result.getMessage());
        List<User> actualUsers = (List<User>) result.getData();
        assertEquals(2, actualUsers.size());
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
        ApiResponse<Object> result = adminController.getAllHotels();
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取酒店列表成功", result.getMessage());
        List<Hotel> actualHotels = (List<Hotel>) result.getData();
        assertEquals(2, actualHotels.size());
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
        ApiResponse<Object> result = adminController.getAllBookings();
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取预订列表成功", result.getMessage());
        List<Booking> actualBookings = (List<Booking>) result.getData();
        assertEquals(2, actualBookings.size());
        verify(adminService, times(1)).getAllBookings();
    }

    @Test
    void getBookingStatistics_ShouldReturnStatistics() {
        // 准备
        String expectedStats = "=== Booking Statistics ===\nTotal Hotels: 5\nTotal Bookings: 20";
        when(adminService.getBookingStatistics()).thenReturn(expectedStats);
        
        // 执行
        ApiResponse<Object> result = adminController.getBookingStatistics();
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取统计信息成功", result.getMessage());
        assertEquals(expectedStats, result.getData());
        verify(adminService, times(1)).getBookingStatistics();
    }

    @Test
    void updateUserRole_WithValidData_ShouldReturnSuccess() {
        // 准备
        when(adminService.updateUserRole(1, "ADMIN")).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = adminController.updateUserRole(1, "ADMIN");
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("用户角色更新成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).updateUserRole(1, "ADMIN");
    }

    @Test
    void updateUserRole_WithInvalidData_ShouldReturnFailure() {
        // 准备
        when(adminService.updateUserRole(999, "ADMIN")).thenReturn(false);
        
        // 执行
        ApiResponse<Object> result = adminController.updateUserRole(999, "ADMIN");
        
        // 验证
        assertTrue(result.isSuccess()); // 注意：即使操作失败，ApiResponse仍然返回成功，但数据为false
        assertEquals("用户角色更新失败", result.getMessage());
        assertFalse((Boolean) result.getData());
        verify(adminService, times(1)).updateUserRole(999, "ADMIN");
    }

    @Test
    void deleteUser_WithExistingUser_ShouldReturnTrue() {
        // 准备
        when(userService.deleteUser(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = adminController.deleteUser(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("用户删除成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void deleteHotel_WithExistingHotel_ShouldReturnTrue() {
        // 准备
        when(adminService.deleteHotel(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = adminController.deleteHotel(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("酒店删除成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).deleteHotel(1);
    }

    @Test
    void cancelBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        when(adminService.cancelUserBooking(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = adminController.cancelBooking(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("预订取消成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(adminService, times(1)).cancelUserBooking(1);
    }

    @Test
    void getTotalRevenue_ShouldReturnRevenue() {
        // 准备
        BigDecimal expectedRevenue = new BigDecimal("5000.00");
        when(adminService.getTotalRevenue()).thenReturn(expectedRevenue);
        
        // 执行
        ApiResponse<Object> result = adminController.getTotalRevenue();
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取总收入成功", result.getMessage());
        assertEquals(expectedRevenue, result.getData());
        verify(adminService, times(1)).getTotalRevenue();
    }

    @Test
    void getMostPopularHotel_ShouldReturnHotelInfo() {
        // 准备
        String expectedInfo = "Grand Hotel (25 bookings)";
        when(adminService.getMostPopularHotel()).thenReturn(expectedInfo);
        
        // 执行
        ApiResponse<Object> result = adminController.getMostPopularHotel();
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取热门酒店成功", result.getMessage());
        assertEquals(expectedInfo, result.getData());
        verify(adminService, times(1)).getMostPopularHotel();
    }

    @Test
    void getUserById_WithExistingUser_ShouldReturnUser() {
        // 准备
        User expectedUser = new User("testuser", "test@test.com", "password", "CUSTOMER");
        expectedUser.setId(1);
        
        when(userService.getUserById(1)).thenReturn(Optional.of(expectedUser));
        
        // 执行
        ApiResponse<Object> result = adminController.getUserById(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取用户信息成功", result.getMessage());
        User actualUser = (User) result.getData();
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void getHotelById_WithExistingHotel_ShouldReturnHotel() {
        // 准备
        Hotel expectedHotel = new Hotel("Test Hotel", "Test City", "Test Description", 10);
        expectedHotel.setId(1);
        
        when(hotelService.getHotelById(1)).thenReturn(Optional.of(expectedHotel));
        
        // 执行
        ApiResponse<Object> result = adminController.getHotelById(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取酒店信息成功", result.getMessage());
        Hotel actualHotel = (Hotel) result.getData();
        assertEquals(expectedHotel.getId(), actualHotel.getId());
        assertEquals(expectedHotel.getName(), actualHotel.getName());
        verify(hotelService, times(1)).getHotelById(1);
    }

    @Test
    void createHotel_WithValidData_ShouldReturnHotel() {
        // 准备
        Hotel expectedHotel = new Hotel("New Hotel", "New City", "New Description", 15);
        expectedHotel.setId(1);
        
        when(hotelService.createHotel("New Hotel", "New City", "New Description", 15))
            .thenReturn(expectedHotel);
        
        // 执行
        ApiResponse<Object> result = adminController.createHotel("New Hotel", "New City", "New Description", 15);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("酒店创建成功", result.getMessage());
        Hotel actualHotel = (Hotel) result.getData();
        assertEquals(expectedHotel.getId(), actualHotel.getId());
        assertEquals(expectedHotel.getName(), actualHotel.getName());
        verify(hotelService, times(1)).createHotel("New Hotel", "New City", "New Description", 15);
    }

    @Test
    void updateHotel_WithValidHotel_ShouldReturnSuccess() {
        // 准备
        Hotel hotel = new Hotel("Updated Hotel", "Updated City", "Updated Description", 20);
        hotel.setId(1);
        
        when(hotelService.updateHotel(hotel)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = adminController.updateHotel(hotel);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("酒店信息更新成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(hotelService, times(1)).updateHotel(hotel);
    }
}
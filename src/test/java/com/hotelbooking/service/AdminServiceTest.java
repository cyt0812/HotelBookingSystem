package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private HotelDAO hotelDAO;

    @Mock
    private BookingDAO bookingDAO;

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(userDAO, hotelDAO, bookingDAO);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User("user1", "user1@test.com", "pass", "CUSTOMER");
        User user2 = new User("admin", "admin@test.com", "pass", "ADMIN");
        List<User> expectedUsers = Arrays.asList(user1, user2);
        
        when(userDAO.getAllUsers()).thenReturn(expectedUsers);
        
        // Act
        List<User> result = adminService.getAllUsers();
        
        // Assert
        assertEquals(2, result.size());
        verify(userDAO, times(1)).getAllUsers();
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A","pool", 5);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B","pool", 10);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        
        when(hotelDAO.getAllHotels()).thenReturn(expectedHotels);
        
        // Act
        List<Hotel> result = adminService.getAllHotels();
        
        // Assert
        assertEquals(2, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
    }

    @Test
    void getAllBookings_ShouldReturnAllBookings() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A","pool", 5);
        hotel1.setId(1);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B","pool", 10);
        hotel2.setId(2);
        List<Hotel> hotels = Arrays.asList(hotel1, hotel2);
        
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CONFIRMED");
        List<Booking> hotel1Bookings = Arrays.asList(booking1);
        List<Booking> hotel2Bookings = Arrays.asList();
        
        when(hotelDAO.getAllHotels()).thenReturn(hotels);
        when(bookingDAO.getBookingsByHotelId(1)).thenReturn(hotel1Bookings);
        when(bookingDAO.getBookingsByHotelId(2)).thenReturn(hotel2Bookings);
        
        // Act
        List<Booking> result = adminService.getAllBookings();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(hotelDAO, times(1)).getAllHotels();
        verify(bookingDAO, times(1)).getBookingsByHotelId(1);
        verify(bookingDAO, times(1)).getBookingsByHotelId(2);
    }

    @Test
    void getBookingStatistics_ShouldReturnStatistics() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A","pool", 5);
        hotel1.setId(1);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B","pool", 10);
        hotel2.setId(2);
        List<Hotel> hotels = Arrays.asList(hotel1, hotel2);
        
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CONFIRMED");
        Booking booking2 = new Booking(2, 1, 1, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), 
                                     new BigDecimal("150"), "PAID");
        List<Booking> hotel1Bookings = Arrays.asList(booking1, booking2);
        List<Booking> hotel2Bookings = Arrays.asList();
        
        when(hotelDAO.getAllHotels()).thenReturn(hotels);
        when(bookingDAO.getBookingsByHotelId(1)).thenReturn(hotel1Bookings);
        when(bookingDAO.getBookingsByHotelId(2)).thenReturn(hotel2Bookings);
        
        // Act
        String statistics = adminService.getBookingStatistics();
        
        // Assert
        assertNotNull(statistics);
        assertTrue(statistics.contains("Total Hotels: 2"));
        assertTrue(statistics.contains("Total Bookings: 2"));
        assertTrue(statistics.contains("Total Revenue: $150.00"));
        verify(hotelDAO, times(1)).getAllHotels();
    }

    @Test
    void updateUserRole_WithValidUser_ShouldUpdateRole() {
        // Arrange
        User user = new User("testuser", "test@test.com", "pass", "CUSTOMER");
        user.setId(1);
        
        when(userDAO.getUserById(1)).thenReturn(Optional.of(user));
        when(userDAO.updateUser(any(User.class))).thenReturn(true);
        
        // Act
        boolean result = adminService.updateUserRole(1, "ADMIN");
        
        // Assert
        assertTrue(result);
        assertEquals("ADMIN", user.getRole());
        verify(userDAO, times(1)).updateUser(user);
    }

    @Test
    void updateUserRole_WithNonExistingUser_ShouldReturnFalse() {
        // Arrange
        when(userDAO.getUserById(999)).thenReturn(Optional.empty());
        
        // Act
        boolean result = adminService.updateUserRole(999, "ADMIN");
        
        // Assert
        assertFalse(result);
        verify(userDAO, never()).updateUser(any(User.class));
    }

    @Test
    void deleteHotel_WithExistingHotel_ShouldReturnTrue() {
        // Arrange
        when(hotelDAO.deleteHotel(1)).thenReturn(true);
        
        // Act
        boolean result = adminService.deleteHotel(1);
        
        // Assert
        assertTrue(result);
        verify(hotelDAO, times(1)).deleteHotel(1);
    }

    @Test
    void cancelUserBooking_WithValidBooking_ShouldCancelBooking() {
        // Arrange
        Booking booking = new Booking(1, 1, 1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 
                                    new BigDecimal("200"), "CONFIRMED");
        booking.setId(1);
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        when(bookingDAO.updateBookingStatus(1, "CANCELLED")).thenReturn(true);
        
        // Act
        boolean result = adminService.cancelUserBooking(1);
        
        // Assert
        assertTrue(result);
        verify(bookingDAO, times(1)).updateBookingStatus(1, "CANCELLED");
    }

    @Test
    void getTotalRevenue_ShouldReturnCorrectAmount() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A","pool", 5);
        hotel1.setId(1);
        List<Hotel> hotels = Arrays.asList(hotel1);
        
        Booking paidBooking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                        new BigDecimal("100"), "PAID");
        Booking confirmedBooking = new Booking(2, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                             new BigDecimal("50"), "CONFIRMED");
        List<Booking> hotelBookings = Arrays.asList(paidBooking, confirmedBooking);
        
        when(hotelDAO.getAllHotels()).thenReturn(hotels);
        when(bookingDAO.getBookingsByHotelId(1)).thenReturn(hotelBookings);
        
        // Act
        BigDecimal revenue = adminService.getTotalRevenue();
        
        // Assert
        assertEquals(new BigDecimal("100"), revenue);
        verify(hotelDAO, times(1)).getAllHotels();
        verify(bookingDAO, times(1)).getBookingsByHotelId(1);
    }

    @Test
    void getMostPopularHotel_ShouldReturnHotelInfo() {
        // Arrange
        Hotel hotel1 = new Hotel("Hotel A", "City A", "Desc A","pool", 5);
        hotel1.setId(1);
        Hotel hotel2 = new Hotel("Hotel B", "City B", "Desc B","pool", 10);
        hotel2.setId(2);
        List<Hotel> hotels = Arrays.asList(hotel1, hotel2);
        
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "PAID");
        Booking booking2 = new Booking(2, 1, 1, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), 
                                     new BigDecimal("150"), "PAID");
        List<Booking> hotel1Bookings = Arrays.asList(booking1, booking2);
        List<Booking> hotel2Bookings = Arrays.asList();
        
        when(hotelDAO.getAllHotels()).thenReturn(hotels);
        when(bookingDAO.getBookingsByHotelId(1)).thenReturn(hotel1Bookings);
        when(bookingDAO.getBookingsByHotelId(2)).thenReturn(hotel2Bookings);
        
        // Act
        String result = adminService.getMostPopularHotel();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Hotel A"));
        assertTrue(result.contains("2 bookings"));
        verify(hotelDAO, times(1)).getAllHotels();
    }

    @Test
    void deleteUser_WithExistingUser_ShouldReturnTrue() {
        // Arrange
        when(userDAO.deleteUser(1)).thenReturn(true);
        
        // Act
        boolean result = adminService.deleteUser(1);
        
        // Assert
        assertTrue(result);
        verify(userDAO, times(1)).deleteUser(1);
    }
}
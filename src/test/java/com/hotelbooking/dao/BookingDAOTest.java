package com.hotelbooking.dao;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.User;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class BookingDAOTest {

    private BookingDAO bookingDAO;
    private UserDAO userDAO;
    private HotelDAO hotelDAO;
    private RoomDAO roomDAO;
    
    private Integer testUserId;
    private Integer testHotelId;
    private Integer testRoomId;

    @BeforeEach
    void setUp() {
        DatabaseInitializer.initializeDatabase();
        bookingDAO = new BookingDAO();
        userDAO = new UserDAO();
        hotelDAO = new HotelDAO();
        roomDAO = new RoomDAO();
        
        // 创建测试数据
        User user = userDAO.createUser(new User("testuser", "test@example.com", "password", "CUSTOMER"));
        testUserId = user.getId();
        
        Hotel hotel = hotelDAO.createHotel(new Hotel("Test Hotel", "Test City", "Test Desc", 10));
        testHotelId = hotel.getId();
        
        Room room = roomDAO.createRoom(new Room(testHotelId, "101", "SINGLE", new BigDecimal("99.99"), true));
        testRoomId = room.getId();
    }

    @Test
    void createBooking_WithValidBooking_ShouldReturnBookingWithId() {
        // 准备
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking booking = new Booking(testUserId, testHotelId, testRoomId, checkIn, checkOut, 
                                    new BigDecimal("199.98"), "CONFIRMED");
        
        // 执行
        Booking result = bookingDAO.createBooking(booking);
        
        // 验证
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUserId, result.getUserId());
        assertEquals(testHotelId, result.getHotelId());
        assertEquals(testRoomId, result.getRoomId());
        assertEquals(checkIn, result.getCheckInDate());
        assertEquals(checkOut, result.getCheckOutDate());
        assertEquals(new BigDecimal("199.98"), result.getTotalPrice());
        assertEquals("CONFIRMED", result.getStatus());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void getBookingById_WithExistingId_ShouldReturnBooking() {
        // 先创建预订
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(2);
        Booking booking = bookingDAO.createBooking(
            new Booking(testUserId, testHotelId, testRoomId, checkIn, checkOut, 
                       new BigDecimal("99.99"), "PENDING"));
        
        // 执行
        Optional<Booking> result = bookingDAO.getBookingById(booking.getId());
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(booking.getId(), result.get().getId());
        assertEquals(testUserId, result.get().getUserId());
        assertEquals("PENDING", result.get().getStatus());
    }

    @Test
    void getBookingById_WithNonExistingId_ShouldReturnEmpty() {
        // 执行
        Optional<Booking> result = bookingDAO.getBookingById(999);
        
        // 验证
        assertTrue(result.isEmpty());
    }

    @Test
    void getBookingsByUserId_ShouldReturnUserBookings() {
        // 准备 - 为同一个用户创建多个预订
        bookingDAO.createBooking(new Booking(testUserId, testHotelId, testRoomId, 
                                           LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                                           new BigDecimal("99.99"), "CONFIRMED"));
        bookingDAO.createBooking(new Booking(testUserId, testHotelId, testRoomId, 
                                           LocalDate.now().plusDays(5), LocalDate.now().plusDays(7),
                                           new BigDecimal("199.98"), "PENDING"));
        
        // 执行
        List<Booking> bookings = bookingDAO.getBookingsByUserId(testUserId);
        
        // 验证
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertTrue(bookings.stream().allMatch(booking -> booking.getUserId().equals(testUserId)));
    }

    @Test
    void getBookingsByHotelId_ShouldReturnHotelBookings() {
        // 准备 - 为同一个酒店创建多个预订
        bookingDAO.createBooking(new Booking(testUserId, testHotelId, testRoomId, 
                                           LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                                           new BigDecimal("99.99"), "CONFIRMED"));
        
        // 创建另一个用户和预订
        User user2 = userDAO.createUser(new User("user2", "user2@test.com", "pass", "CUSTOMER"));
        bookingDAO.createBooking(new Booking(user2.getId(), testHotelId, testRoomId, 
                                           LocalDate.now().plusDays(3), LocalDate.now().plusDays(4),
                                           new BigDecimal("99.99"), "CONFIRMED"));
        
        // 执行
        List<Booking> bookings = bookingDAO.getBookingsByHotelId(testHotelId);
        
        // 验证
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertTrue(bookings.stream().allMatch(booking -> booking.getHotelId().equals(testHotelId)));
    }

    @Test
    void getBookingsByStatus_ShouldReturnMatchingBookings() {
        // 准备
        bookingDAO.createBooking(new Booking(testUserId, testHotelId, testRoomId, 
                                           LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                                           new BigDecimal("99.99"), "CONFIRMED"));
        bookingDAO.createBooking(new Booking(testUserId, testHotelId, testRoomId, 
                                           LocalDate.now().plusDays(5), LocalDate.now().plusDays(7),
                                           new BigDecimal("199.98"), "PENDING"));
        
        // 执行
        List<Booking> confirmedBookings = bookingDAO.getBookingsByStatus("CONFIRMED");
        
        // 验证
        assertNotNull(confirmedBookings);
        assertEquals(1, confirmedBookings.size());
        assertTrue(confirmedBookings.stream().allMatch(booking -> booking.getStatus().equals("CONFIRMED")));
    }

    @Test
    void updateBookingStatus_ShouldUpdateStatus() {
        // 准备
        Booking booking = bookingDAO.createBooking(new Booking(testUserId, testHotelId, testRoomId, 
                                           LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                                           new BigDecimal("99.99"), "PENDING"));
        
        // 执行
        boolean updated = bookingDAO.updateBookingStatus(booking.getId(), "CONFIRMED");
        
        // 验证
        assertTrue(updated);
        Optional<Booking> retrievedBooking = bookingDAO.getBookingById(booking.getId());
        assertTrue(retrievedBooking.isPresent());
        assertEquals("CONFIRMED", retrievedBooking.get().getStatus());
    }

    @Test
    void deleteBooking_ShouldRemoveBooking() {
        // 准备
        Booking booking = bookingDAO.createBooking(new Booking(testUserId, testHotelId, testRoomId, 
                                           LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                                           new BigDecimal("99.99"), "CONFIRMED"));
        
        // 执行
        boolean deleted = bookingDAO.deleteBooking(booking.getId());
        
        // 验证
        assertTrue(deleted);
        Optional<Booking> retrievedBooking = bookingDAO.getBookingById(booking.getId());
        assertTrue(retrievedBooking.isEmpty());
    }
}
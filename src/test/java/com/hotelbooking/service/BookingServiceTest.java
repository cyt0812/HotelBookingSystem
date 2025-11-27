package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingDAO bookingDAO;

    @Mock
    private RoomDAO roomDAO;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking_WithValidData_ShouldCreateBooking() {
        // 准备
        Room room = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        room.setId(1);
        
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking expectedBooking = new Booking(1, 1, 1, checkIn, checkOut, new BigDecimal("200"), "CONFIRMED");
        expectedBooking.setId(1);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        when(bookingDAO.createBooking(any(Booking.class))).thenReturn(expectedBooking);
        
        // 执行
        Optional<Booking> result = bookingService.createBooking(1, 1, 1, checkIn, checkOut);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("CONFIRMED", result.get().getStatus());
        verify(bookingDAO, times(1)).createBooking(any(Booking.class));
        verify(roomDAO, times(1)).updateRoom(any(Room.class)); // 应该更新房间状态
    }

    @Test
    void createBooking_WithUnavailableRoom_ShouldReturnEmpty() {
        // 准备
        Room room = new Room(1, "101", "SINGLE", new BigDecimal("100"), false); // 不可用
        room.setId(1);
        
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        
        // 执行
        Optional<Booking> result = bookingService.createBooking(1, 1, 1, checkIn, checkOut);
        
        // 验证
        assertTrue(result.isEmpty());
        verify(bookingDAO, never()).createBooking(any(Booking.class));
    }

    @Test
    void createBooking_WithInvalidDates_ShouldReturnEmpty() {
        // 准备
        Room room = new Room(1, "101", "SINGLE", new BigDecimal("100"), true);
        room.setId(1);
        
        LocalDate checkIn = LocalDate.now().plusDays(3);
        LocalDate checkOut = LocalDate.now().plusDays(1); // 结束日期在开始日期之前
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        
        // 执行
        Optional<Booking> result = bookingService.createBooking(1, 1, 1, checkIn, checkOut);
        
        // 验证
        assertTrue(result.isEmpty());
        verify(bookingDAO, never()).createBooking(any(Booking.class));
    }

    @Test
    void getBookingById_WithExistingId_ShouldReturnBooking() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                    new BigDecimal("100"), "CONFIRMED");
        booking.setId(1);
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        
        // 执行
        Optional<Booking> result = bookingService.getBookingById(1);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(bookingDAO, times(1)).getBookingById(1);
    }

    @Test
    void getBookingsByUserId_ShouldReturnUserBookings() {
        // 准备
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CONFIRMED");
        Booking booking2 = new Booking(1, 1, 1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), 
                                     new BigDecimal("200"), "PENDING");
        List<Booking> expectedBookings = Arrays.asList(booking1, booking2);
        
        when(bookingDAO.getBookingsByUserId(1)).thenReturn(expectedBookings);
        
        // 执行
        List<Booking> result = bookingService.getBookingsByUserId(1);
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(booking -> booking.getUserId().equals(1)));
        verify(bookingDAO, times(1)).getBookingsByUserId(1);
    }

    @Test
    void cancelBooking_WithExistingBooking_ShouldUpdateStatus() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 
                                    new BigDecimal("200"), "CONFIRMED");
        booking.setId(1);
        
        Room room = new Room(1, "101", "SINGLE", new BigDecimal("100"), false);
        room.setId(1);
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        when(bookingDAO.updateBookingStatus(1, "CANCELLED")).thenReturn(true);
        
        // 执行
        boolean result = bookingService.cancelBooking(1);
        
        // 验证
        assertTrue(result);
        verify(bookingDAO, times(1)).updateBookingStatus(1, "CANCELLED");
        verify(roomDAO, times(1)).updateRoom(any(Room.class)); // 应该恢复房间可用性
    }

    @Test
    void updateBookingStatus_WithValidStatus_ShouldReturnTrue() {
        // 准备
        when(bookingDAO.updateBookingStatus(1, "COMPLETED")).thenReturn(true);
        
        // 执行
        boolean result = bookingService.updateBookingStatus(1, "COMPLETED");
        
        // 验证
        assertTrue(result);
        verify(bookingDAO, times(1)).updateBookingStatus(1, "COMPLETED");
    }

    @Test
    void deleteBooking_WithExistingId_ShouldReturnTrue() {
        // 准备
        when(bookingDAO.deleteBooking(1)).thenReturn(true);
        
        // 执行
        boolean result = bookingService.deleteBooking(1);
        
        // 验证
        assertTrue(result);
        verify(bookingDAO, times(1)).deleteBooking(1);
    }
}
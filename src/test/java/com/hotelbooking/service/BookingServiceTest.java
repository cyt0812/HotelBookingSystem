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
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, true, "Standard single room");
        room.setId(1);
        
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(4);
        Booking expectedBooking = new Booking(1, 1, 1, checkIn, checkOut, 
                                            new BigDecimal("200"), "CONFIRMED");
        expectedBooking.setId(1);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        when(bookingDAO.createBooking(any(Booking.class))).thenReturn(expectedBooking);
        when(roomDAO.updateRoom(any(Room.class))).thenReturn(true);
        
        // 执行
        Optional<Booking> result = bookingService.createBooking(1, 1, 1, checkIn, checkOut);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("CONFIRMED", result.get().getStatus());
        verify(bookingDAO, times(1)).createBooking(any(Booking.class));
        verify(roomDAO, times(1)).updateRoom(any(Room.class));
    }

    @Test
    void createBooking_WithUnavailableRoom_ShouldReturnEmpty() {
        // 准备
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, false, "Standard single room");
        room.setId(1);
        
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(4);
        
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        
        // 执行
        Optional<Booking> result = bookingService.createBooking(1, 1, 1, checkIn, checkOut);
        
        // 验证
        assertTrue(result.isEmpty());
        verify(bookingDAO, never()).createBooking(any(Booking.class));
    }

    @Test
void createBooking_WithInvalidDates_ShouldReturnEmpty() {
    // 准备 - 不需要设置roomDAO的stubbing，因为不会调用它
    LocalDate checkIn = LocalDate.now().plusDays(3);
    LocalDate checkOut = LocalDate.now().plusDays(1); // 结束日期在开始日期之前
    
    // 执行 - 直接调用，不需要任何stubbing
    Optional<Booking> result = bookingService.createBooking(1, 1, 1, checkIn, checkOut);
    
    // 验证
    assertTrue(result.isEmpty());
    verify(roomDAO, never()).getRoomById(anyInt()); // 验证确实没有调用
    verify(bookingDAO, never()).createBooking(any(Booking.class));
}

    @Test
    void createBooking_WithPastCheckInDate_ShouldReturnEmpty() {
        // 准备
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, true, "Standard single room");
        room.setId(1);
        
        LocalDate checkIn = LocalDate.now().minusDays(1); // 过去的日期
        LocalDate checkOut = LocalDate.now().plusDays(2);
       
        
        // 执行
        Optional<Booking> result = bookingService.createBooking(1, 1, 1, checkIn, checkOut);
        
        // 验证
        assertTrue(result.isEmpty());
        verify(roomDAO, never()).getRoomById(anyInt());
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
    void getBookingById_WithNonExistingId_ShouldReturnEmpty() {
        // 准备
        when(bookingDAO.getBookingById(999)).thenReturn(Optional.empty());
        
        // 执行
        Optional<Booking> result = bookingService.getBookingById(999);
        
        // 验证
        assertTrue(result.isEmpty());
        verify(bookingDAO, times(1)).getBookingById(999);
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
        verify(bookingDAO, times(1)).getBookingsByUserId(1);
    }

    @Test
    void cancelBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 
                                    new BigDecimal("200"), "CONFIRMED");
        booking.setId(1);
        
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, false, "Standard single room");
        room.setId(1);
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        when(bookingDAO.updateBookingStatus(1, "CANCELLED")).thenReturn(true);
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        when(roomDAO.updateRoom(any(Room.class))).thenReturn(true);
        
        // 执行
        boolean result = bookingService.cancelBooking(1);
        
        // 验证
        assertTrue(result);
        verify(bookingDAO, times(1)).updateBookingStatus(1, "CANCELLED");
        verify(roomDAO, times(1)).updateRoom(any(Room.class));
    }

    @Test
    void cancelBooking_WithNonExistingBooking_ShouldReturnFalse() {
        // 准备
        when(bookingDAO.getBookingById(999)).thenReturn(Optional.empty());
        
        // 执行
        boolean result = bookingService.cancelBooking(999);
        
        // 验证
        assertFalse(result);
        verify(bookingDAO, never()).updateBookingStatus(anyInt(), anyString());
    }

    @Test
    void completeBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now().minusDays(1), LocalDate.now(), 
                                    new BigDecimal("100"), "CONFIRMED");
        booking.setId(1);
        
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, false, "Standard single room");
        room.setId(1);
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        when(bookingDAO.updateBookingStatus(1, "COMPLETED")).thenReturn(true);
        when(roomDAO.getRoomById(1)).thenReturn(Optional.of(room));
        when(roomDAO.updateRoom(any(Room.class))).thenReturn(true);
        
        // 执行
        boolean result = bookingService.completeBooking(1);
        
        // 验证
        assertTrue(result);
        verify(bookingDAO, times(1)).updateBookingStatus(1, "COMPLETED");
        verify(roomDAO, times(1)).updateRoom(any(Room.class));
    }

    @Test
    void completeBooking_WithNonExistingBooking_ShouldReturnFalse() {
        // 准备
        when(bookingDAO.getBookingById(999)).thenReturn(Optional.empty());
        
        // 执行
        boolean result = bookingService.completeBooking(999);
        
        // 验证
        assertFalse(result);
        verify(bookingDAO, never()).updateBookingStatus(anyInt(), anyString());
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
    void updateBookingStatus_WithInvalidBookingId_ShouldReturnFalse() {
        // 准备
        when(bookingDAO.updateBookingStatus(999, "COMPLETED")).thenReturn(false);
        
        // 执行
        boolean result = bookingService.updateBookingStatus(999, "COMPLETED");
        
        // 验证
        assertFalse(result);
        verify(bookingDAO, times(1)).updateBookingStatus(999, "COMPLETED");
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

    @Test
    void deleteBooking_WithNonExistingId_ShouldReturnFalse() {
        // 准备
        when(bookingDAO.deleteBooking(999)).thenReturn(false);
        
        // 执行
        boolean result = bookingService.deleteBooking(999);
        
        // 验证
        assertFalse(result);
        verify(bookingDAO, times(1)).deleteBooking(999);
    }
}
package com.hotelbooking.controller;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.PaymentService;
import com.hotelbooking.service.RoomService;
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

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RoomService roomService;

    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingController = new BookingController(bookingService, paymentService, roomService);
    }

    @Test
    void createBooking_WithValidData_ShouldReturnBooking() {
        // 准备
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking expectedBooking = new Booking(1, 1, 1, checkIn, checkOut, new BigDecimal("200"), "CONFIRMED");
        expectedBooking.setId(1);
        
        when(bookingService.createBooking(1, 1, 1, checkIn, checkOut))
                .thenReturn(Optional.of(expectedBooking));
        
        // 执行
        Booking result = bookingController.createBooking(1, 1, 1, checkIn, checkOut);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("CONFIRMED", result.getStatus());
        verify(bookingService, times(1)).createBooking(1, 1, 1, checkIn, checkOut);
    }

    @Test
    void createBooking_WithInvalidData_ShouldThrowException() {
        // 准备
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        when(bookingService.createBooking(1, 1, 1, checkIn, checkOut))
                .thenReturn(Optional.empty());
        
        // 执行和验证
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> bookingController.createBooking(1, 1, 1, checkIn, checkOut)
        );
        
        assertEquals("Failed to create booking", exception.getMessage());
        verify(bookingService, times(1)).createBooking(1, 1, 1, checkIn, checkOut);
    }

    @Test
    void getBookingById_WithExistingBooking_ShouldReturnBooking() {
        // 准备
        Booking expectedBooking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                            new BigDecimal("100"), "CONFIRMED");
        expectedBooking.setId(1);
        
        when(bookingService.getBookingById(1)).thenReturn(Optional.of(expectedBooking));
        
        // 执行
        Booking result = bookingController.getBookingById(1);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(bookingService, times(1)).getBookingById(1);
    }

    @Test
    void getBookingsByUserId_ShouldReturnUserBookings() {
        // 准备
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CONFIRMED");
        Booking booking2 = new Booking(1, 1, 1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), 
                                     new BigDecimal("200"), "PAID");
        List<Booking> expectedBookings = Arrays.asList(booking1, booking2);
        
        when(bookingService.getBookingsByUserId(1)).thenReturn(expectedBookings);
        
        // 执行
        List<Booking> result = bookingController.getBookingsByUserId(1);
        
        // 验证
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(booking -> booking.getUserId().equals(1)));
        verify(bookingService, times(1)).getBookingsByUserId(1);
    }

    @Test
    void cancelBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        when(bookingService.cancelBooking(1)).thenReturn(true);
        
        // 执行
        boolean result = bookingController.cancelBooking(1);
        
        // 验证
        assertTrue(result);
        verify(bookingService, times(1)).cancelBooking(1);
    }

    @Test
    void processPayment_WithValidData_ShouldReturnTrue() {
        // 准备
        when(paymentService.processPayment(1, "1234567890123456", "12/25", "123"))
                .thenReturn(true);
        
        // 执行
        boolean result = bookingController.processPayment(1, "1234567890123456", "12/25", "123");
        
        // 验证
        assertTrue(result);
        verify(paymentService, times(1)).processPayment(1, "1234567890123456", "12/25", "123");
    }

    @Test
    void getBookingAmount_WithExistingBooking_ShouldReturnAmount() {
        // 准备
        BigDecimal expectedAmount = new BigDecimal("200.00");
        when(paymentService.getBookingAmount(1)).thenReturn(Optional.of(expectedAmount));
        
        // 执行
        BigDecimal result = bookingController.getBookingAmount(1);
        
        // 验证
        assertEquals(expectedAmount, result);
        verify(paymentService, times(1)).getBookingAmount(1);
    }

    @Test
    void checkRoomAvailability_WithAvailableRoom_ShouldReturnTrue() {
        // 准备
        when(roomService.isRoomAvailable(1)).thenReturn(true);
        
        // 执行
        boolean result = bookingController.checkRoomAvailability(1);
        
        // 验证
        assertTrue(result);
        verify(roomService, times(1)).isRoomAvailable(1);
    }

    @Test
    void completeBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        when(bookingService.completeBooking(1)).thenReturn(true);
        
        // 执行
        boolean result = bookingController.completeBooking(1);
        
        // 验证
        assertTrue(result);
        verify(bookingService, times(1)).completeBooking(1);
    }
}
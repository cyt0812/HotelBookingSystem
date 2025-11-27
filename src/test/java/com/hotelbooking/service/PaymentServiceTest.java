package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.entity.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private BookingDAO bookingDAO;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void processPayment_WithValidBooking_ShouldProcessPayment() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                    new BigDecimal("200"), "CONFIRMED");
        booking.setId(1);
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        when(bookingDAO.updateBookingStatus(1, "PAID")).thenReturn(true);
        
        // 执行
        boolean result = paymentService.processPayment(1, "1234567890123456", "12/25", "123");
        
        // 验证
        assertTrue(result);
        verify(bookingDAO, times(1)).updateBookingStatus(1, "PAID");
    }

    @Test
    void processPayment_WithNonExistingBooking_ShouldReturnFalse() {
        // 准备
        when(bookingDAO.getBookingById(999)).thenReturn(Optional.empty());
        
        // 执行
        boolean result = paymentService.processPayment(999, "1234567890123456", "12/25", "123");
        
        // 验证
        assertFalse(result);
        verify(bookingDAO, never()).updateBookingStatus(anyInt(), anyString());
    }

    @Test
    void processPayment_WithAlreadyPaidBooking_ShouldReturnFalse() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                    new BigDecimal("200"), "PAID"); // 已经是已支付状态
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        
        // 执行
        boolean result = paymentService.processPayment(1, "1234567890123456", "12/25", "123");
        
        // 验证
        assertFalse(result);
        verify(bookingDAO, never()).updateBookingStatus(anyInt(), anyString());
    }

    @Test
    void processPayment_WithInvalidCardNumber_ShouldReturnFalse() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                    new BigDecimal("200"), "CONFIRMED");
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        
        // 执行 - 卡号太短
        boolean result = paymentService.processPayment(1, "1234", "12/25", "123");
        
        // 验证
        assertFalse(result);
        verify(bookingDAO, never()).updateBookingStatus(anyInt(), anyString());
    }

    @Test
    void processPayment_WithExpiredCard_ShouldReturnFalse() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                    new BigDecimal("200"), "CONFIRMED");
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        
        // 执行 - 过期卡片
        boolean result = paymentService.processPayment(1, "1234567890123456", "01/20", "123");
        
        // 验证
        assertFalse(result);
        verify(bookingDAO, never()).updateBookingStatus(anyInt(), anyString());
    }

    @Test
    void refundPayment_WithValidBooking_ShouldProcessRefund() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                    new BigDecimal("200"), "PAID");
        booking.setId(1);
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        when(bookingDAO.updateBookingStatus(1, "REFUNDED")).thenReturn(true);
        
        // 执行
        boolean result = paymentService.refundPayment(1);
        
        // 验证
        assertTrue(result);
        verify(bookingDAO, times(1)).updateBookingStatus(1, "REFUNDED");
    }

    @Test
    void getBookingAmount_WithValidBooking_ShouldReturnAmount() {
        // 准备
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                    new BigDecimal("200"), "CONFIRMED");
        booking.setId(1);
        
        when(bookingDAO.getBookingById(1)).thenReturn(Optional.of(booking));
        
        // 执行
        Optional<BigDecimal> result = paymentService.getBookingAmount(1);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("200"), result.get());
    }
}
package com.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void bookingCreation_WithAllFields_ShouldSetPropertiesCorrectly() {
        // 准备 & 执行
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking booking = new Booking(1, 1, 1, checkIn, checkOut, 
                                    new BigDecimal("199.98"), "CONFIRMED");
        
        // 验证
        assertEquals(1, booking.getUserId());
        assertEquals(1, booking.getHotelId());
        assertEquals(1, booking.getRoomId());
        assertEquals(checkIn, booking.getCheckInDate());
        assertEquals(checkOut, booking.getCheckOutDate());
        assertEquals(new BigDecimal("199.98"), booking.getTotalPrice());
        assertEquals("CONFIRMED", booking.getStatus());
        assertNull(booking.getId());
        assertNotNull(booking.getCreatedAt());
    }

    @Test
    void bookingSetters_ShouldUpdateFieldsCorrectly() {
        // 准备
        Booking booking = new Booking();
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        
        // 执行
        booking.setId(1);
        booking.setUserId(2);
        booking.setHotelId(3);
        booking.setRoomId(4);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setTotalPrice(new BigDecimal("299.99"));
        booking.setStatus("CANCELLED");
        booking.setCreatedAt(createdAt);
        
        // 验证
        assertEquals(1, booking.getId());
        assertEquals(2, booking.getUserId());
        assertEquals(3, booking.getHotelId());
        assertEquals(4, booking.getRoomId());
        assertEquals(checkIn, booking.getCheckInDate());
        assertEquals(checkOut, booking.getCheckOutDate());
        assertEquals(new BigDecimal("299.99"), booking.getTotalPrice());
        assertEquals("CANCELLED", booking.getStatus());
        assertEquals(createdAt, booking.getCreatedAt());
    }

    @Test
    void bookingStatuses_ShouldHaveValidValues() {
        // 测试预订状态常量
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "PENDING");
        Booking booking2 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CONFIRMED");
        Booking booking3 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CANCELLED");
        Booking booking4 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "COMPLETED");
        
        assertEquals("PENDING", booking1.getStatus());
        assertEquals("CONFIRMED", booking2.getStatus());
        assertEquals("CANCELLED", booking3.getStatus());
        assertEquals("COMPLETED", booking4.getStatus());
    }

    @Test
    void bookingDuration_ShouldCalculateCorrectNumberOfNights() {
        // 准备
        LocalDate checkIn = LocalDate.of(2024, 1, 1);
        LocalDate checkOut = LocalDate.of(2024, 1, 5);
        Booking booking = new Booking(1, 1, 1, checkIn, checkOut, new BigDecimal("400"), "CONFIRMED");
        
        // 验证 - 4晚住宿
        assertEquals(4, checkIn.until(checkOut).getDays());
    }
}
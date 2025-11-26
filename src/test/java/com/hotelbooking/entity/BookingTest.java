package com.hotelbooking.entity;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingTest {

    @Test
    public void testConstructorAndGetters() {
        LocalDate checkIn = LocalDate.of(2025, 1, 10);
        LocalDate checkOut = LocalDate.of(2025, 1, 12);

        Booking booking = new Booking(1, 2, checkIn, checkOut, 500.0);

        assertEquals(1, booking.getUserId());
        assertEquals(2, booking.getRoomId());
        assertEquals(checkIn, booking.getCheckInDate());
        assertEquals(checkOut, booking.getCheckOutDate());
        assertEquals(500.0, booking.getTotalPrice(), 0.0001);
        assertEquals("CONFIRMED", booking.getStatus());

        // createdAt 不能精确比，但可以检查是否在合理时间范围内
        assertNotNull(booking.getCreatedAt());
        assertTrue(booking.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    public void testSetters() {
        Booking booking = new Booking();

        LocalDate checkIn = LocalDate.of(2025, 2, 1);
        LocalDate checkOut = LocalDate.of(2025, 2, 5);
        LocalDateTime created = LocalDateTime.of(2024, 12, 1, 10, 20);

        booking.setBookingId(10);
        booking.setUserId(100);
        booking.setRoomId(200);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setTotalPrice(999.99);
        booking.setStatus("CANCELLED");
        booking.setCreatedAt(created);

        assertEquals(10, booking.getBookingId());
        assertEquals(100, booking.getUserId());
        assertEquals(200, booking.getRoomId());
        assertEquals(checkIn, booking.getCheckInDate());
        assertEquals(checkOut, booking.getCheckOutDate());
        assertEquals(999.99, booking.getTotalPrice(), 0.0001);
        assertEquals("CANCELLED", booking.getStatus());
        assertEquals(created, booking.getCreatedAt());
    }
}

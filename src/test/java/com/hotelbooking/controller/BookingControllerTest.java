package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.Payment;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.PaymentService;
import com.hotelbooking.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
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

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // -------------------------
    // CREATE BOOKING
    // -------------------------
    @Test
    void createBooking_ShouldReturnSuccess() {
        LocalDate in = LocalDate.now().plusDays(1);
        LocalDate out = LocalDate.now().plusDays(3);
        Booking booking = new Booking(1, 1, 1, in, out, new BigDecimal("300"), "CONFIRMED");

        when(bookingService.createBooking(1, 1, 1, in, out))
                .thenReturn(Optional.of(booking));

        ApiResponse<Object> res = bookingController.createBooking(1, 1, 1, in, out);

        assertTrue(res.isSuccess());
        assertEquals("预订创建成功", res.getMessage());
        assertEquals("CONFIRMED", ((Booking) res.getData()).getStatus());
    }


    // -------------------------
    // GET BOOKING BY ID
    // -------------------------
    @Test
    void getBookingById_ShouldReturnBooking() {
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                     new BigDecimal("200"), "CONFIRMED");
        when(bookingService.getBookingById(10)).thenReturn(Optional.of(booking));

        ApiResponse<Object> res = bookingController.getBookingById(10);

        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
    }


    // -------------------------
    // GET BOOKINGS BY USER ID
    // -------------------------
    @Test
    void getBookingsByUserId_ShouldReturnList() {
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                     new BigDecimal("200"), "CONFIRMED");
        when(bookingService.getBookingsByUserId(5))
                .thenReturn(List.of(booking));

        ApiResponse<Object> res = bookingController.getBookingsByUserId(5);

        assertTrue(res.isSuccess());
        assertEquals(1, ((List<?>) res.getData()).size());
    }


    // -------------------------
    // GET BOOKINGS BY HOTEL ID
    // -------------------------
    @Test
    void getBookingsByHotelId_ShouldReturnList() {
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                     new BigDecimal("200"), "CONFIRMED");
        when(bookingService.getBookingsByHotelId(7))
                .thenReturn(List.of(booking));

        ApiResponse<Object> res = bookingController.getBookingsByHotelId(7);

        assertTrue(res.isSuccess());
    }


    // -------------------------
    // CANCEL BOOKING
    // -------------------------
    @Test
    void cancelBooking_ShouldReturnSuccess() {
        when(bookingService.cancelBooking(11)).thenReturn(true);

        ApiResponse<Object> res = bookingController.cancelBooking(11);

        assertTrue(res.isSuccess());
    }


    // -------------------------
    // COMPLETE BOOKING
    // -------------------------
    @Test
    void completeBooking_ShouldReturnSuccess() {
        when(bookingService.completeBooking(11)).thenReturn(true);

        ApiResponse<Object> res = bookingController.completeBooking(11);

        assertTrue(res.isSuccess());
    }


    // -------------------------
    // PAYMENT
    // -------------------------
    @Test
    void processPayment_ShouldReturnSuccess() {
        when(paymentService.processPayment("B123", new BigDecimal("100"), "CARD"))
                .thenReturn(true);

        ApiResponse<Object> res =
                bookingController.processPayment("B123", new BigDecimal("100"), "CARD");

        assertTrue(res.isSuccess());
    }


    // -------------------------
    // REFUND
    // -------------------------
    @Test
    void processRefund_ShouldReturnSuccess() {
        when(paymentService.processRefund("B123")).thenReturn(true);

        ApiResponse<Object> res = bookingController.processRefund("B123");

        assertTrue(res.isSuccess());
    }


    // -------------------------
    // PAYMENT STATUS
    // -------------------------
    @Test
    void getPaymentStatus_ShouldReturnStatus() {
        Payment p = new Payment();
        p.setPaymentStatus("SUCCESS");

        when(paymentService.getPaymentStatus("B123"))
                .thenReturn(Optional.of(p));

        ApiResponse<Object> res = bookingController.getPaymentStatus("B123");

        assertEquals("SUCCESS", res.getData());
    }


    // -------------------------
    // GET ROOM BY ID
    // -------------------------
    @Test
    void getRoomById_ShouldReturnRoom() {
        Room room = new Room(1, "101", "SINGLE", 100.0, 2, true, "Standard single room");
        room.setId(3);
        when(roomService.getRoomById(3)).thenReturn(Optional.of(room));

        ApiResponse<Object> res = bookingController.getRoomById(3);

        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
    }


    // -------------------------
    // GET AVAILABLE ROOMS BY HOTEL
    // -------------------------
    @Test
    void getAvailableRoomsByHotel_ShouldReturnList() {
        Room room = new Room(9, "101", "SINGLE", 100.0, 2, true, "Standard single room");
        when(roomService.getAvailableRoomsByHotelId(9))
                .thenReturn(List.of(room));

        ApiResponse<Object> res = bookingController.getAvailableRoomsByHotel(9);

        assertTrue(res.isSuccess());
    }


    // -------------------------
    // GET ROOMS BY PRICE RANGE
    // -------------------------
    @Test
    void getRoomsByPriceRange_ShouldFilterByHotelId() {
        Room r1 = new Room(1, "101", "SINGLE", 150.0, 2, true, "Standard single room");
        r1.setId(1);
        
        Room r2 = new Room(2, "201", "DOUBLE", 250.0, 2, true, "Standard double room");
        r2.setId(2); // should be filtered out

        when(roomService.getRoomsByPriceRange(
                new BigDecimal("100"),
                new BigDecimal("300")))
                .thenReturn(List.of(r1, r2));

        ApiResponse<Object> res =
                bookingController.getAvailableRoomsByPriceRange(
                        1, new BigDecimal("100"), new BigDecimal("300"));

        List<?> list = (List<?>) res.getData();

        assertEquals(1, list.size());     // only r1
    }


    // -------------------------
    // GET BOOKINGS BY STATUS
    // -------------------------
    @Test
    void getBookingsByStatus_ShouldReturnList() {
        Booking booking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), 
                                     new BigDecimal("200"), "CONFIRMED");
        when(bookingService.getBookingsByStatus("CONFIRMED"))
                .thenReturn(List.of(booking));

        ApiResponse<Object> res = bookingController.getBookingsByStatus("CONFIRMED");

        assertTrue(res.isSuccess());
    }


    // -------------------------
    // UPDATE STATUS
    // -------------------------
    @Test
    void updateBookingStatus_ShouldReturnSuccess() {
        when(bookingService.updateBookingStatus(10, "CANCELLED"))
                .thenReturn(true);

        ApiResponse<Object> res =
                bookingController.updateBookingStatus(10, "CANCELLED");

        assertTrue(res.isSuccess());
    }


    // -------------------------
    // DELETE BOOKING
    // -------------------------
    @Test
    void deleteBooking_ShouldReturnSuccess() {
        when(bookingService.deleteBooking(10)).thenReturn(true);

        ApiResponse<Object> res = bookingController.deleteBooking(10);

        assertTrue(res.isSuccess());
    }
}
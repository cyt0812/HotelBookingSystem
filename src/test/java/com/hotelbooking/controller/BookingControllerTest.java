package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Payment;
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
        
        when(bookingService.createBooking(1, 1, 1, checkIn, checkOut))
                .thenReturn(expectedBooking);
        
        // 执行
        ApiResponse<Object> result = bookingController.createBooking(1, 1, 1, checkIn, checkOut);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("预订创建成功", result.getMessage());
        Booking actualBooking = (Booking) result.getData();
        assertNotNull(actualBooking);
        assertEquals("CONFIRMED", actualBooking.getStatus());
        verify(bookingService, times(1)).createBooking(1, 1, 1, checkIn, checkOut);
    }

    @Test
    void createBookingWithPayment_WithValidData_ShouldReturnBooking() {
        // 准备
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking expectedBooking = new Booking(1, 1, 1, checkIn, checkOut, new BigDecimal("200"), "CONFIRMED");
        
        when(bookingService.createBookingWithPayment(1, 1, 1, checkIn, checkOut, "CREDIT_CARD"))
                .thenReturn(expectedBooking);
        
        // 执行
        ApiResponse<Object> result = bookingController.createBookingWithPayment(1, 1, 1, checkIn, checkOut, "CREDIT_CARD");
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("带支付预订创建成功", result.getMessage());
        Booking actualBooking = (Booking) result.getData();
        assertNotNull(actualBooking);
        assertEquals("CONFIRMED", actualBooking.getStatus());
        verify(bookingService, times(1)).createBookingWithPayment(1, 1, 1, checkIn, checkOut, "CREDIT_CARD");
    }

    @Test
    void getBookingById_WithExistingBooking_ShouldReturnBooking() {
        // 准备
        Booking expectedBooking = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                            new BigDecimal("100"), "CONFIRMED");
        
        when(bookingService.getBookingById(1)).thenReturn(Optional.of(expectedBooking));
        
        // 执行
        ApiResponse<Object> result = bookingController.getBookingById(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取预订信息成功", result.getMessage());
        Booking actualBooking = (Booking) result.getData();
        assertNotNull(actualBooking);
        verify(bookingService, times(1)).getBookingById(1);
    }

    @Test
    void getBookingById_WithNonExistingBooking_ShouldReturnFailure() {
        // 准备
        when(bookingService.getBookingById(999)).thenReturn(Optional.empty());
        
        // 执行
        ApiResponse<Object> result = bookingController.getBookingById(999);
        
        // 验证
        assertFalse(result.isSuccess());
        assertEquals("预订不存在，ID: 999", result.getMessage());
        verify(bookingService, times(1)).getBookingById(999);
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
        ApiResponse<Object> result = bookingController.getBookingsByUserId(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取用户预订列表成功", result.getMessage());
        List<Booking> actualBookings = (List<Booking>) result.getData();
        assertEquals(2, actualBookings.size());
        assertTrue(actualBookings.stream().allMatch(booking -> booking.getUserId().equals(1)));
        verify(bookingService, times(1)).getBookingsByUserId(1);
    }

    @Test
    void getBookingsByHotelId_ShouldReturnHotelBookings() {
        // 准备
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CONFIRMED");
        Booking booking2 = new Booking(2, 1, 1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), 
                                     new BigDecimal("200"), "PAID");
        List<Booking> expectedBookings = Arrays.asList(booking1, booking2);
        
        when(bookingService.getBookingsByHotelId(1)).thenReturn(expectedBookings);
        
        // 执行
        ApiResponse<Object> result = bookingController.getBookingsByHotelId(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取酒店预订列表成功", result.getMessage());
        List<Booking> actualBookings = (List<Booking>) result.getData();
        assertEquals(2, actualBookings.size());
        verify(bookingService, times(1)).getBookingsByHotelId(1);
    }

    @Test
    void cancelBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        when(bookingService.cancelBooking(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.cancelBooking(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("预订取消成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(bookingService, times(1)).cancelBooking(1);
    }

    @Test
    void cancelBooking_WithNonExistingBooking_ShouldReturnFalse() {
        // 准备
        when(bookingService.cancelBooking(999)).thenReturn(false);
        
        // 执行
        ApiResponse<Object> result = bookingController.cancelBooking(999);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("预订取消失败", result.getMessage());
        assertFalse((Boolean) result.getData());
        verify(bookingService, times(1)).cancelBooking(999);
    }

    @Test
    void cancelBookingWithRefund_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        when(bookingService.cancelBookingWithRefund(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.cancelBookingWithRefund(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("取消并退款成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(bookingService, times(1)).cancelBookingWithRefund(1);
    }

    @Test
    void completeBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        when(bookingService.completeBooking(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.completeBooking(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("预订完成成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(bookingService, times(1)).completeBooking(1);
    }

    @Test
    void processPayment_WithValidData_ShouldReturnTrue() {
        // 准备
        String bookingId = "BOOK_123";
        BigDecimal amount = new BigDecimal("200.00");
        String paymentMethod = "CREDIT_CARD";
        
        when(paymentService.processPayment(bookingId, amount, paymentMethod))
                .thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.processPayment(bookingId, amount, paymentMethod);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("支付处理成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(paymentService, times(1)).processPayment(bookingId, amount, paymentMethod);
    }

    @Test
    void processRefund_WithValidData_ShouldReturnTrue() {
        // 准备
        String bookingId = "BOOK_123";
        
        when(paymentService.processRefund(bookingId)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.processRefund(bookingId);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("退款处理成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(paymentService, times(1)).processRefund(bookingId);
    }

    @Test
    void getPaymentStatus_WithExistingPayment_ShouldReturnStatus() {
        // 准备
        String bookingId = "BOOK_123";
        Payment payment = new Payment(bookingId, new BigDecimal("200.00"), "CREDIT_CARD");
        payment.setPaymentStatus("COMPLETED");
        
        when(paymentService.getPaymentStatus(bookingId)).thenReturn(Optional.of(payment));
        
        // 执行
        ApiResponse<Object> result = bookingController.getPaymentStatus(bookingId);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取支付状态成功", result.getMessage());
        assertEquals("COMPLETED", result.getData());
        verify(paymentService, times(1)).getPaymentStatus(bookingId);
    }

    @Test
    void getPaymentStatus_WithNonExistingPayment_ShouldReturnNotFound() {
        // 准备
        String bookingId = "BOOK_123";
        
        when(paymentService.getPaymentStatus(bookingId)).thenReturn(Optional.empty());
        
        // 执行
        ApiResponse<Object> result = bookingController.getPaymentStatus(bookingId);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取支付状态成功", result.getMessage());
        assertEquals("NOT_FOUND", result.getData());
        verify(paymentService, times(1)).getPaymentStatus(bookingId);
    }

    @Test
    void calculateRefundAmount_WithValidData_ShouldReturnAmount() {
        // 准备
        String bookingId = "BOOK_123";
        LocalDate checkInDate = LocalDate.now().plusDays(10);
        BigDecimal expectedRefund = new BigDecimal("100.00");
        
        when(paymentService.calculateRefundAmount(bookingId, checkInDate))
                .thenReturn(expectedRefund);
        
        // 执行
        ApiResponse<Object> result = bookingController.calculateRefundAmount(bookingId, checkInDate);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("计算退款金额成功", result.getMessage());
        assertEquals(expectedRefund, result.getData());
        verify(paymentService, times(1)).calculateRefundAmount(bookingId, checkInDate);
    }

    @Test
    void checkRoomAvailability_WithAvailableRoom_ShouldReturnTrue() {
        // 准备
        when(roomService.isRoomAvailable(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.checkRoomAvailability(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("房间可用", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(roomService, times(1)).isRoomAvailable(1);
    }

    @Test
    void checkRoomAvailabilityWithDates_WithAvailableRoom_ShouldReturnTrue() {
        // 准备
        Integer roomId = 1;
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        when(bookingService.isRoomAvailable(roomId, checkIn, checkOut)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.checkRoomAvailability(roomId, checkIn, checkOut);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("房间在指定时间段可用", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(bookingService, times(1)).isRoomAvailable(roomId, checkIn, checkOut);
    }

    @Test
    void getRoomById_WithExistingRoom_ShouldReturnRoom() {
        // 准备
        Room expectedRoom = new Room(1, "101", "STANDARD", new BigDecimal("100.00"), true);
        
        when(roomService.getRoomById(1)).thenReturn(Optional.of(expectedRoom));
        
        // 执行
        ApiResponse<Object> result = bookingController.getRoomById(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取房间信息成功", result.getMessage());
        Room actualRoom = (Room) result.getData();
        assertNotNull(actualRoom);
        assertEquals("101", actualRoom.getRoomNumber());
        verify(roomService, times(1)).getRoomById(1);
    }

    @Test
    void getAvailableRoomsByHotel_ShouldReturnAvailableRooms() {
        // 准备
        Room room1 = new Room(1, "101", "STANDARD", new BigDecimal("100.00"), true);
        Room room2 = new Room(1, "102", "DELUXE", new BigDecimal("150.00"), true);
        List<Room> expectedRooms = Arrays.asList(room1, room2);
        
        when(roomService.getAvailableRoomsByHotelId(1)).thenReturn(expectedRooms);
        
        // 执行
        ApiResponse<Object> result = bookingController.getAvailableRoomsByHotel(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取可用房间列表成功", result.getMessage());
        List<Room> actualRooms = (List<Room>) result.getData();
        assertEquals(2, actualRooms.size());
        verify(roomService, times(1)).getAvailableRoomsByHotelId(1);
    }

    @Test
    void getBookingsByStatus_ShouldReturnBookings() {
        // 准备
        Booking booking1 = new Booking(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), 
                                     new BigDecimal("100"), "CONFIRMED");
        Booking booking2 = new Booking(2, 1, 1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), 
                                     new BigDecimal("200"), "CONFIRMED");
        List<Booking> expectedBookings = Arrays.asList(booking1, booking2);
        
        when(bookingService.getBookingsByStatus("CONFIRMED")).thenReturn(expectedBookings);
        
        // 执行
        ApiResponse<Object> result = bookingController.getBookingsByStatus("CONFIRMED");
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("获取状态预订列表成功", result.getMessage());
        List<Booking> actualBookings = (List<Booking>) result.getData();
        assertEquals(2, actualBookings.size());
        assertTrue(actualBookings.stream().allMatch(booking -> "CONFIRMED".equals(booking.getStatus())));
        verify(bookingService, times(1)).getBookingsByStatus("CONFIRMED");
    }

    @Test
    void updateBookingStatus_WithValidData_ShouldReturnTrue() {
        // 准备
        when(bookingService.updateBookingStatus(1, "COMPLETED")).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.updateBookingStatus(1, "COMPLETED");
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("预订状态更新成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(bookingService, times(1)).updateBookingStatus(1, "COMPLETED");
    }

    @Test
    void validateBookingDates_WithValidDates_ShouldReturnTrue() {
        // 准备
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(4);
        
        // 执行
        ApiResponse<Object> result = bookingController.validateBookingDates(checkIn, checkOut);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("日期验证成功", result.getMessage());
        assertTrue((Boolean) result.getData());
    }

    @Test
    void validateBookingDates_WithInvalidDates_ShouldReturnFalse() {
        // 准备
        LocalDate checkIn = LocalDate.now().minusDays(1); // 过去日期
        LocalDate checkOut = LocalDate.now().plusDays(1);
        
        // 执行
        ApiResponse<Object> result = bookingController.validateBookingDates(checkIn, checkOut);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("日期验证失败", result.getMessage());
        assertFalse((Boolean) result.getData());
    }

    @Test
    void deleteBooking_WithExistingBooking_ShouldReturnTrue() {
        // 准备
        when(bookingService.deleteBooking(1)).thenReturn(true);
        
        // 执行
        ApiResponse<Object> result = bookingController.deleteBooking(1);
        
        // 验证
        assertTrue(result.isSuccess());
        assertEquals("预订删除成功", result.getMessage());
        assertTrue((Boolean) result.getData());
        verify(bookingService, times(1)).deleteBooking(1);
    }
}
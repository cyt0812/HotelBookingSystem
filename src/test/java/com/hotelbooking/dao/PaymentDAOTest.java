package com.hotelbooking.dao;

import com.hotelbooking.entity.Payment;
import com.hotelbooking.entity.User;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PaymentDAOTest {

    private PaymentDAO paymentDAO;
    private BookingDAO bookingDAO;
    private UserDAO userDAO;
    private HotelDAO hotelDAO;
    private RoomDAO roomDAO;
    
    private Integer testUserId;
    private Integer testHotelId;
    private Integer testRoomId;
    private String testBookingId;

    @BeforeEach
    void setUp() {
        DatabaseInitializer.initializeDatabase();
        paymentDAO = new PaymentDAO();
        bookingDAO = new BookingDAO();
        userDAO = new UserDAO();
        hotelDAO = new HotelDAO();
        roomDAO = new RoomDAO();
        
        // 创建测试数据
        User user = userDAO.createUser(new User("testuser", "test@example.com", "password", "CUSTOMER"));
        testUserId = user.getId();
        
        Hotel hotel = hotelDAO.createHotel(new Hotel("Test Hotel", "Test City", "Test Desc", "gym pool", 10));
        testHotelId = hotel.getId();
        
        Room room = roomDAO.createRoom(new Room(
            testHotelId,           // hotelId
            "101",                 // roomNumber
            "SINGLE",              // roomType
            99.99,                 // pricePerNight
            2,                     // maxOccupancy
            true,                  // isAvailable
            "Standard room"        // description
        ));
        testRoomId = room.getId();
        
        // 创建预订
        Booking booking = bookingDAO.createBooking(new Booking(
            testUserId, testHotelId, testRoomId,
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(3),
            new BigDecimal("199.98"), "CONFIRMED"
        ));
        testBookingId = booking.getBookingId();
    }

    @Test
    void createPayment_WithValidPayment_ShouldCreateSuccessfully() {
        // 准备
        Payment payment = new Payment();
        payment.setPaymentId("PAY" + System.currentTimeMillis());
        payment.setBookingId(testBookingId.toString());
        payment.setAmount(new BigDecimal("199.98"));
        payment.setPaymentMethod("CREDIT_CARD");
        payment.setPaymentStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        
        // 执行
        boolean result = paymentDAO.createPayment(payment);
        
        // 验证
        assertTrue(result);
    }

    @Test
    void getPaymentById_WithExistingId_ShouldReturnPayment() {
        // 准备 - 先创建支付记录
        String paymentId = "PAY" + System.currentTimeMillis();
        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setBookingId(testBookingId.toString());
        payment.setAmount(new BigDecimal("199.98"));
        payment.setPaymentMethod("CREDIT_CARD");
        payment.setPaymentStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        
        paymentDAO.createPayment(payment);
        
        // 执行
        Payment retrievedPayment = paymentDAO.getPaymentById(paymentId);
        
        // 验证
        assertNotNull(retrievedPayment);
        assertEquals(paymentId, retrievedPayment.getPaymentId());
        assertEquals(testBookingId.toString(), retrievedPayment.getBookingId());
        assertEquals(new BigDecimal("199.98"), retrievedPayment.getAmount());
        assertEquals("CREDIT_CARD", retrievedPayment.getPaymentMethod());
        assertEquals("COMPLETED", retrievedPayment.getPaymentStatus());
    }

    @Test
    void getPaymentById_WithNonExistingId_ShouldReturnNull() {
        // 执行
        Payment retrievedPayment = paymentDAO.getPaymentById("NON_EXISTING_PAY_ID");
        
        // 验证
        assertNull(retrievedPayment);
    }

    @Test
    void getPaymentByBookingId_WithExistingBookingId_ShouldReturnPayment() {
        // 准备 - 先创建支付记录
        String paymentId = "PAY" + System.currentTimeMillis();
        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setBookingId(testBookingId.toString());
        payment.setAmount(new BigDecimal("199.98"));
        payment.setPaymentMethod("CREDIT_CARD");
        payment.setPaymentStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        
        paymentDAO.createPayment(payment);
        
        // 执行
        Payment retrievedPayment = paymentDAO.getPaymentByBookingId(testBookingId.toString());
        
        // 验证
        assertNotNull(retrievedPayment);
        assertEquals(testBookingId.toString(), retrievedPayment.getBookingId());
        assertEquals(new BigDecimal("199.98"), retrievedPayment.getAmount());
    }

    @Test
    void updatePaymentStatus_ShouldUpdateStatusAndTransactionId() {
        // 准备 - 先创建支付记录
        String paymentId = "PAY" + System.currentTimeMillis();
        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setBookingId(testBookingId.toString());
        payment.setAmount(new BigDecimal("199.98"));
        payment.setPaymentMethod("CREDIT_CARD");
        payment.setPaymentStatus("PENDING");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId("OLD_TXN" + System.currentTimeMillis());
        
        paymentDAO.createPayment(payment);
        
        // 执行 - 更新状态和交易ID
        String newTransactionId = "NEW_TXN" + System.currentTimeMillis();
        boolean result = paymentDAO.updatePaymentStatus(paymentId, "COMPLETED", newTransactionId);
        
        // 验证
        assertTrue(result);
        Payment updatedPayment = paymentDAO.getPaymentById(paymentId);
        assertNotNull(updatedPayment);
        assertEquals("COMPLETED", updatedPayment.getPaymentStatus());
        assertEquals(newTransactionId, updatedPayment.getTransactionId());
    }

    @Test
    void getAllPayments_ShouldReturnAllPayments() {
        // 先获取现有支付记录数量
        List<Payment> existingPayments = paymentDAO.getAllPayments();
        int existingCount = existingPayments.size();
        
        // 创建两个新的支付记录
        for (int i = 0; i < 2; i++) {
            String paymentId = "PAY_ALL" + System.currentTimeMillis() + i;
            Payment payment = new Payment();
            payment.setPaymentId(paymentId);
            payment.setBookingId(testBookingId.toString());
            payment.setAmount(new BigDecimal("199.98"));
            payment.setPaymentMethod("CREDIT_CARD");
            payment.setPaymentStatus("COMPLETED");
            payment.setPaymentDate(LocalDateTime.now());
            payment.setTransactionId("TXN_ALL" + System.currentTimeMillis() + i);
            
            paymentDAO.createPayment(payment);
        }
        
        // 执行查询
        List<Payment> allPayments = paymentDAO.getAllPayments();
        
        // 验证
        assertNotNull(allPayments);
        assertEquals(existingCount + 2, allPayments.size());
    }

    @Test
    void getPaymentsByStatus_ShouldReturnMatchingPayments() {
        // 先获取现有COMPLETED支付记录数量
        List<Payment> existingCompleted = paymentDAO.getPaymentsByStatus("COMPLETED");
        int existingCompletedCount = existingCompleted.size();
        
        // 获取现有PENDING支付记录数量
        List<Payment> existingPending = paymentDAO.getPaymentsByStatus("PENDING");
        int existingPendingCount = existingPending.size();
        
        // 创建1个COMPLETED支付
        String completedPaymentId = "PAY_COMPLETED" + System.currentTimeMillis();
        Payment completedPayment = new Payment();
        completedPayment.setPaymentId(completedPaymentId);
        completedPayment.setBookingId(testBookingId.toString());
        completedPayment.setAmount(new BigDecimal("199.98"));
        completedPayment.setPaymentMethod("CREDIT_CARD");
        completedPayment.setPaymentStatus("COMPLETED");
        completedPayment.setPaymentDate(LocalDateTime.now());
        completedPayment.setTransactionId("TXN_COMPLETED" + System.currentTimeMillis());
        
        paymentDAO.createPayment(completedPayment);
        
        // 创建1个PENDING支付
        String pendingPaymentId = "PAY_PENDING" + System.currentTimeMillis();
        Payment pendingPayment = new Payment();
        pendingPayment.setPaymentId(pendingPaymentId);
        pendingPayment.setBookingId(testBookingId.toString());
        pendingPayment.setAmount(new BigDecimal("299.97"));
        pendingPayment.setPaymentMethod("PAYPAL");
        pendingPayment.setPaymentStatus("PENDING");
        pendingPayment.setPaymentDate(LocalDateTime.now());
        pendingPayment.setTransactionId("TXN_PENDING" + System.currentTimeMillis());
        
        paymentDAO.createPayment(pendingPayment);
        
        // 执行查询
        List<Payment> completedPayments = paymentDAO.getPaymentsByStatus("COMPLETED");
        List<Payment> pendingPayments = paymentDAO.getPaymentsByStatus("PENDING");
        
        // 验证：数量应该增加
        assertEquals(existingCompletedCount + 1, completedPayments.size());
        assertEquals(existingPendingCount + 1, pendingPayments.size());
        
        // 验证状态是否正确
        assertTrue(completedPayments.stream().anyMatch(p -> p.getPaymentId().equals(completedPaymentId)));
        assertTrue(pendingPayments.stream().anyMatch(p -> p.getPaymentId().equals(pendingPaymentId)));
    }
}

package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.entity.Booking;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PaymentService {
    private final BookingDAO bookingDAO;
    
    public PaymentService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }
    
    /**
     * 处理支付
     */
    public boolean processPayment(Integer bookingId, String cardNumber, String expiryDate, String cvv) {
    // 验证支付信息
    if (!isValidCardNumber(cardNumber) || !isValidExpiryDate(expiryDate) || !isValidCVV(cvv)) {
        return false;
    }
    
    // 获取预订信息
    Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
    if (bookingOpt.isEmpty()) {
        return false;
    }
    
    Booking booking = bookingOpt.get();
    
    // 检查预订状态是否允许支付
    if (!"CONFIRMED".equals(booking.getStatus()) && !"PENDING".equals(booking.getStatus())) {
        return false;
    }
    
    // 模拟支付处理
    System.out.println("Processing payment for booking " + bookingId);
    System.out.println("Amount: $" + booking.getTotalPrice());
    System.out.println("Card: **** **** **** " + cardNumber.substring(cardNumber.length() - 4));
    
    // 这里总是返回 true 来模拟成功的支付
    // 在实际应用中，这里会调用支付网关并返回实际结果
    boolean paymentSuccess = true;
    
    if (paymentSuccess) {
        // 更新预订状态为已支付
        return bookingDAO.updateBookingStatus(bookingId, "PAID");
    }
    
    return false;
}
    
    /**
     * 处理退款
     */
    public boolean refundPayment(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isEmpty()) {
            return false;
        }
        
        Booking booking = bookingOpt.get();
        
        // 只有已支付的预订才能退款
        if (!"PAID".equals(booking.getStatus())) {
            return false;
        }
        
        // 模拟退款处理
        System.out.println("Processing refund for booking " + bookingId);
        System.out.println("Refund amount: $" + booking.getTotalPrice());
        
        // 更新预订状态为已退款
        return bookingDAO.updateBookingStatus(bookingId, "REFUNDED");
    }
    
    /**
     * 获取预订金额
     */
    public Optional<BigDecimal> getBookingAmount(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        return bookingOpt.map(Booking::getTotalPrice);
    }
    
    /**
     * 验证信用卡号
     */
    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }
        
        // 简单的Luhn算法验证
        return luhnCheck(cardNumber.replaceAll("\\s+", ""));
    }
    
    /**
     * 验证有效期
     */
    private boolean isValidExpiryDate(String expiryDate) {
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        
        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000; // 假设是20xx年
            
            if (month < 1 || month > 12) {
                return false;
            }
            
            LocalDate expiry = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
            return !expiry.isBefore(LocalDate.now());
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 验证CVV
     */
    private boolean isValidCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3,4}");
    }
    
    /**
     * Luhn算法验证信用卡号
     */
    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return (sum % 10) == 0;
    }
    
    /**
     * 计算应退金额（根据取消政策）
     */
    public Optional<BigDecimal> calculateRefundAmount(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Booking booking = bookingOpt.get();
        LocalDate checkIn = booking.getCheckInDate();
        LocalDate today = LocalDate.now();
        
        long daysUntilCheckIn = java.time.temporal.ChronoUnit.DAYS.between(today, checkIn);
        
        // 取消政策：
        // - 提前7天以上取消：全额退款
        // - 提前3-7天取消：退款50%
        // - 3天内取消：不退款
        BigDecimal refundAmount;
        if (daysUntilCheckIn > 7) {
            refundAmount = booking.getTotalPrice();
        } else if (daysUntilCheckIn > 3) {
            refundAmount = booking.getTotalPrice().multiply(new BigDecimal("0.5"));
        } else {
            refundAmount = BigDecimal.ZERO;
        }
        
        return Optional.of(refundAmount);
    }
}
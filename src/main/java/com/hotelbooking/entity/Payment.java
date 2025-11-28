// Payment.java - 支付实体类
package com.hotelbooking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private String bookingId;
    private BigDecimal amount;
    private String paymentMethod; // CREDIT_CARD, PAYPAL, WECHAT, ALIPAY
    private String paymentStatus; // PENDING, COMPLETED, FAILED, REFUNDED
    private LocalDateTime paymentDate;
    private String transactionId;
    
    // 构造函数
    public Payment() {}
    
    public Payment(String bookingId, BigDecimal amount, String paymentMethod) {
        this.paymentId = generatePaymentId();
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "PENDING";
        this.paymentDate = LocalDateTime.now();
    }
    
    private String generatePaymentId() {
        return "PAY_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    // Getter和Setter方法
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
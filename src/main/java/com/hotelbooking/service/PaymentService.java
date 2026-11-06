package com.hotelbooking.service;

import com.hotelbooking.dao.PaymentDAO;
import com.hotelbooking.entity.Payment;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class PaymentService {
    private PaymentDAO paymentDAO;
    
    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }
    
    // 便捷构造函数
    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }
    
    /**
     * 处理支付
     */
    public boolean processPayment(String bookingId, BigDecimal amount, String paymentMethod) {
        try {
            // 创建支付记录
            Payment payment = new Payment(bookingId, amount, paymentMethod);
            boolean created = paymentDAO.createPayment(payment);
            
            if (!created) {
                throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to create payment record");
            }
            
            // 模拟支付处理（在实际项目中这里会调用第三方支付API）
            boolean paymentSuccess = simulatePaymentProcessing(paymentMethod);
            
            if (paymentSuccess) {
                // 支付成功，更新支付状态
                String transactionId = "TXN_" + System.currentTimeMillis();
                return paymentDAO.updatePaymentStatus(payment.getPaymentId(), "COMPLETED", transactionId);
            } else {
                // 支付失败
                paymentDAO.updatePaymentStatus(payment.getPaymentId(), "FAILED", null);
                throw new BusinessException(ErrorType.PAYMENT_FAILED, "Payment processing failed. Please try again or use a different payment method");
            }
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Payment system error: " + e.getMessage(), e);
        }
    }
    
    /**
     * 处理退款
     */
    public boolean processRefund(String bookingId) {
        try {
            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
            if (payment == null) {
                throw new BusinessException(ErrorType.PAYMENT_NOT_FOUND, "No corresponding payment record found");
            }
            
            if (!"COMPLETED".equals(payment.getPaymentStatus())) {
                throw new BusinessException(ErrorType.REFUND_FAILED, "Only completed payments can be refunded");
            }
            
            // 模拟退款处理
            boolean refundSuccess = simulateRefundProcessing();
            
            if (refundSuccess) {
                return paymentDAO.updatePaymentStatus(payment.getPaymentId(), "REFUNDED", 
                    payment.getTransactionId() + "_REFUND");
            } else {
                throw new BusinessException(ErrorType.REFUND_FAILED, "Refund processing failed");
            }
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Refund system error: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取支付状态
     */
    public Optional<Payment> getPaymentStatus(String bookingId) {
        try {
            return Optional.ofNullable(paymentDAO.getPaymentByBookingId(bookingId));
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to query payment status: " + e.getMessage(), e);
        }
    }
    
    /**
     * 模拟支付处理（实际项目中替换为真实的支付网关调用）
     */
    private boolean simulatePaymentProcessing(String paymentMethod) {
        // 模拟支付成功率：90%
        System.out.println("Simulating " + paymentMethod + " payment processing...");
        return Math.random() > 0.1;
    }
    
    /**
     * 模拟退款处理
     */
    private boolean simulateRefundProcessing() {
        // 模拟退款成功率：95%
        System.out.println("Simulating refund processing...");
        return Math.random() > 0.05;
    }
    
    /**
     * 计算应退金额（根据取消政策）
     */
    public BigDecimal calculateRefundAmount(String bookingId, LocalDate checkInDate) {
        try {
            LocalDate today = LocalDate.now();
            long daysUntilCheckIn = java.time.temporal.ChronoUnit.DAYS.between(today, checkInDate);
            
            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
            if (payment == null) {
                return BigDecimal.ZERO;
            }
            
            BigDecimal amount = payment.getAmount();
            
            // 取消政策：
            // - 提前7天以上取消：全额退款
            // - 提前3-7天取消：退款50%
            // - 3天内取消：不退款
            if (daysUntilCheckIn > 7) {
                return amount;
            } else if (daysUntilCheckIn > 3) {
               return amount.multiply(new BigDecimal("0.5")).setScale(2, java.math.RoundingMode.HALF_UP);
            } else {
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to calculate refund amount: " + e.getMessage(), e);
        }
    }
}
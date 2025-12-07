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
<<<<<<< HEAD
                throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to create payment record");
=======
                throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "创建支付记录失败");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
<<<<<<< HEAD
                throw new BusinessException(ErrorType.PAYMENT_FAILED, "Payment processing failed. Please try again or use a different payment method");
=======
                throw new BusinessException(ErrorType.PAYMENT_FAILED, "支付处理失败，请重试或更换支付方式");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            }
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Payment system error: " + e.getMessage(), e);
=======
                "支付系统错误: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
     * 处理退款
     */
    public boolean processRefund(String bookingId) {
        try {
            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
            if (payment == null) {
<<<<<<< HEAD
                throw new BusinessException(ErrorType.PAYMENT_NOT_FOUND, "No corresponding payment record found");
            }
            
            if (!"COMPLETED".equals(payment.getPaymentStatus())) {
                throw new BusinessException(ErrorType.REFUND_FAILED, "Only completed payments can be refunded");
=======
                throw new BusinessException(ErrorType.PAYMENT_NOT_FOUND, "未找到对应的支付记录");
            }
            
            if (!"COMPLETED".equals(payment.getPaymentStatus())) {
                throw new BusinessException(ErrorType.REFUND_FAILED, "只有已完成的支付才能退款");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            }
            
            // 模拟退款处理
            boolean refundSuccess = simulateRefundProcessing();
            
            if (refundSuccess) {
                return paymentDAO.updatePaymentStatus(payment.getPaymentId(), "REFUNDED", 
                    payment.getTransactionId() + "_REFUND");
            } else {
<<<<<<< HEAD
                throw new BusinessException(ErrorType.REFUND_FAILED, "Refund processing failed");
=======
                throw new BusinessException(ErrorType.REFUND_FAILED, "退款处理失败");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            }
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Refund system error: " + e.getMessage(), e);
=======
                "退款系统错误: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
<<<<<<< HEAD
                "Failed to query payment status: " + e.getMessage(), e);
=======
                "查询支付状态失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
    
    /**
     * 模拟支付处理（实际项目中替换为真实的支付网关调用）
     */
    private boolean simulatePaymentProcessing(String paymentMethod) {
        // 模拟支付成功率：90%
<<<<<<< HEAD
        System.out.println("Simulating " + paymentMethod + " payment processing...");
=======
        System.out.println("模拟" + paymentMethod + "支付处理...");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        return Math.random() > 0.1;
    }
    
    /**
     * 模拟退款处理
     */
    private boolean simulateRefundProcessing() {
        // 模拟退款成功率：95%
<<<<<<< HEAD
        System.out.println("Simulating refund processing...");
=======
        System.out.println("模拟退款处理...");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
<<<<<<< HEAD
                "Failed to calculate refund amount: " + e.getMessage(), e);
=======
                "计算退款金额失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
}
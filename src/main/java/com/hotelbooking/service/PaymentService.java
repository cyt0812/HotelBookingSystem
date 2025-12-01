package com.hotelbooking.service;

public class PaymentService {
    
    public boolean processPayment(double amount, String cardNumber) {
        // 模拟支付处理
        System.out.println("Processing payment: Amount " + amount);
        
        // 简单验证逻辑
        return cardNumber != null && cardNumber.startsWith("4") && cardNumber.length() == 16;
    }
}
// PaymentDAO.java - 支付数据访问层
package com.hotelbooking.dao;

import com.hotelbooking.entity.Payment;
import com.hotelbooking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    public PaymentDAO() {
        // 空构造函数，不使用类级别connection
    }
    
    // 创建支付记录
    public boolean createPayment(Payment payment) {
        String sql = "INSERT INTO payments (payment_id, booking_id, amount, payment_method, payment_status, payment_date, transaction_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, payment.getPaymentId());
            stmt.setString(2, payment.getBookingId());
            stmt.setBigDecimal(3, payment.getAmount());
            stmt.setString(4, payment.getPaymentMethod());
            stmt.setString(5, payment.getPaymentStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setString(7, payment.getTransactionId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating payment", e);
        }
    }
    
    // 根据支付ID查询支付记录
    public Payment getPaymentById(String paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting payment by id: " + paymentId, e);
        }
    }
    
    // 根据预订ID查询支付记录
    public Payment getPaymentByBookingId(String bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting payment by booking id: " + bookingId, e);
        }
    }
    
    // 更新支付状态
    public boolean updatePaymentStatus(String paymentId, String status, String transactionId) {
        String sql = "UPDATE payments SET payment_status = ?, transaction_id = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setString(2, transactionId);
            stmt.setString(3, paymentId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating payment status: " + paymentId, e);
        }
    }
    
    // 获取所有支付记录
    public List<Payment> getAllPayments() {
        String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            return payments;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all payments", e);
        }
    }
    
    // 根据状态获取支付记录
    public List<Payment> getPaymentsByStatus(String status) {
        String sql = "SELECT * FROM payments WHERE payment_status = ? ORDER BY payment_date DESC";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            return payments;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting payments by status: " + status, e);
        }
    }
    
    // 映射ResultSet到Payment对象
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getString("payment_id"));
        payment.setBookingId(rs.getString("booking_id"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
        payment.setTransactionId(rs.getString("transaction_id"));
        return payment;
    }
}
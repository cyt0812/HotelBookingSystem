package com.hotelbooking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private int userId;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;

    public Booking() {}

    public Booking(int userId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, double totalPrice) {
        this.userId = userId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.status = "CONFIRMED";
        this.createdAt = LocalDateTime.now();
    }

    // Getter 和 Setter
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Booking{bookingId=" + bookingId + ", userId=" + userId + ", roomId=" + roomId + 
               ", checkIn=" + checkInDate + ", checkOut=" + checkOutDate + ", totalPrice=" + totalPrice + "}";
    }
}
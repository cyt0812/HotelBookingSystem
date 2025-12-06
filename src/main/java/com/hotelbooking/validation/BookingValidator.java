// BookingValidator.java - Booking data validation
package com.hotelbooking.validation;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.exception.ValidationException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingValidator {
    
    public static void validateBooking(Booking booking) {
        // Basic data validation
        validateBasicData(booking);
        
        // Date validation
        validateDates(booking.getCheckInDate(), booking.getCheckOutDate());
        
        // Business rules validation
        validateBusinessRules(booking);
    }
    
    private static void validateBasicData(Booking booking) {
        if (booking.getUserId() <= 0) {
            throw new ValidationException("Invalid user ID");
        }
        
        if (booking.getHotelId() <= 0) {
            throw new ValidationException("Invalid hotel ID");
        }
        
        if (booking.getRoomId() <= 0) {
            throw new ValidationException("Invalid room ID");
        }
        
        if (booking.getTotalPrice() == null || booking.getTotalPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be greater than 0");
        }
    }
    
    private static void validateDates(LocalDate checkIn, LocalDate checkOut) {
        LocalDate today = LocalDate.now();
        
        // Check-in date cannot be before today
        if (checkIn.isBefore(today)) {
            throw new ValidationException("Check-in date cannot be a past date");
        }
        
        // Must book at least 1 day in advance
        if (!checkIn.isAfter(today.plusDays(1))) {
            throw new ValidationException("Booking must be made at least 1 day in advance");
        }
        
        // Check-out date must be after check-in date
        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException("Check-out date must be after check-in date");
        }
        
        // Maximum stay limit (30 days)
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights > 30) {
            throw new ValidationException("Maximum stay is 30 days");
        }
    }
    
    private static void validateBusinessRules(Booking booking) {
        // More business rules can be added here
        // For example: special date restrictions, membership level restrictions, etc.
    }
}
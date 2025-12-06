package com.hotelbooking.validation;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.exception.ValidationException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class BookingValidatorTest {

    @Test
    public void testValidateBooking_ValidBooking() {
        // Arrange - Create a valid booking
        LocalDate tomorrow = LocalDate.now().plusDays(2);
        LocalDate afterTomorrow = tomorrow.plusDays(3);
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(tomorrow);
        booking.setCheckOutDate(afterTomorrow);
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Valid booking should not throw exception
        assertDoesNotThrow(() -> BookingValidator.validateBooking(booking));
    }

    // Test basic data validation
    @Test
    public void testValidateBooking_InvalidUserId() {
        // Arrange - Create booking with invalid user ID
        Booking booking = new Booking();
        booking.setUserId(0); // Invalid user ID
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(LocalDate.now().plusDays(2));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Invalid user ID", exception.getMessage());
    }

    @Test
    public void testValidateBooking_InvalidHotelId() {
        // Arrange - Create booking with invalid hotel ID
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(-1); // Invalid hotel ID
        booking.setRoomId(1);
        booking.setCheckInDate(LocalDate.now().plusDays(2));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Invalid hotel ID", exception.getMessage());
    }

    @Test
    public void testValidateBooking_InvalidRoomId() {
        // Arrange - Create booking with invalid room ID
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(0); // Invalid room ID
        booking.setCheckInDate(LocalDate.now().plusDays(2));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Invalid room ID", exception.getMessage());
    }

    @Test
    public void testValidateBooking_NullPrice() {
        // Arrange - Create booking with null price
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(LocalDate.now().plusDays(2));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setTotalPrice(null); // Null price
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Price must be greater than 0", exception.getMessage());
    }

    @Test
    public void testValidateBooking_NegativePrice() {
        // Arrange - Create booking with negative price
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(LocalDate.now().plusDays(2));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setTotalPrice(new BigDecimal(-100)); // Negative price
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Price must be greater than 0", exception.getMessage());
    }

    @Test
    public void testValidateBooking_ZeroPrice() {
        // Arrange - Create booking with zero price
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(LocalDate.now().plusDays(2));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setTotalPrice(new BigDecimal(0)); // Zero price
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Price must be greater than 0", exception.getMessage());
    }

    // Test date validation
    @Test
    public void testValidateBooking_CheckInBeforeToday() {
        // Arrange - Create booking with check-in date before today
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(LocalDate.now().minusDays(1)); // Check-in date before today
        booking.setCheckOutDate(LocalDate.now().plusDays(1));
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Check-in date cannot be a past date", exception.getMessage());
    }

    @Test
    public void testValidateBooking_NotEnoughAdvanceBooking() {
        // Arrange - Create booking with check-in date not at least 1 day in advance
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(LocalDate.now().plusDays(1)); // Check-in date is tomorrow
        booking.setCheckOutDate(LocalDate.now().plusDays(2));
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Booking must be made at least 1 day in advance", exception.getMessage());
    }

    @Test
    public void testValidateBooking_CheckOutBeforeCheckIn() {
        // Arrange - Create booking with check-out date before check-in date
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(LocalDate.now().plusDays(3));
        booking.setCheckOutDate(LocalDate.now().plusDays(2)); // Check-out date before check-in date
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Check-out date must be after check-in date", exception.getMessage());
    }

    @Test
    public void testValidateBooking_CheckOutSameAsCheckIn() {
        // Arrange - Create booking with check-out date same as check-in date
        LocalDate futureDate = LocalDate.now().plusDays(5);
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(futureDate);
        booking.setCheckOutDate(futureDate); // Check-out date same as check-in date
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Check-out date must be after check-in date", exception.getMessage());
    }

    @Test
    public void testValidateBooking_TooLongStay() {
        // Arrange - Create booking with stay longer than 30 days
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = checkIn.plusDays(31); // 31 days stay
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setTotalPrice(new BigDecimal(10000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Maximum stay is 30 days", exception.getMessage());
    }

    @Test
    public void testValidateBooking_StayLessThanOneNight() {
        // Arrange - Create booking with stay less than one night
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = checkIn; // Same day check-out
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setRoomId(1);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setTotalPrice(new BigDecimal(1000));
        booking.setStatus("CONFIRMED");

        // Act & Assert - Should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> BookingValidator.validateBooking(booking));
        assertEquals("Check-out date must be after check-in date", exception.getMessage());
    }
}

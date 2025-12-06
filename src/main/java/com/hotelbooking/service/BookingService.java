package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    
    public BookingService(BookingDAO bookingDAO, RoomDAO roomDAO) {
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
    }
    
    
    
    /**
     * Create booking
     */
    public Optional<Booking> createBooking(Integer userId, Integer hotelId, Integer roomId, 
                                         LocalDate checkInDate, LocalDate checkOutDate) {
        // Validate dates
        if (checkInDate == null || checkOutDate == null || 
            checkInDate.isBefore(LocalDate.now()) || 
            !checkOutDate.isAfter(checkInDate)) {
            return Optional.empty();
        }
        
        // Check if room exists and is available
        Optional<Room> roomOpt = roomDAO.getRoomById(roomId);
        if (roomOpt.isEmpty() || !roomOpt.get().isAvailable()) {
            return Optional.empty();
        }
        
        Room room = roomOpt.get();
        
        // Calculate total price
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal roomPrice = BigDecimal.valueOf(room.getPricePerNight());
        BigDecimal totalPrice = roomPrice.multiply(BigDecimal.valueOf(numberOfNights));
        
        // Create booking
        Booking booking = new Booking(userId, hotelId, roomId, checkInDate, checkOutDate, 
                                    totalPrice, "CONFIRMED");
        
        Booking createdBooking = bookingDAO.createBooking(booking);
        
        // Update room status to unavailable
        room.setIsAvailable(false);
        roomDAO.updateRoom(room);
        
        return Optional.of(createdBooking);
    }
    
    /**
     * Get booking by ID
     */
    public Optional<Booking> getBookingById(Integer id) {
        return bookingDAO.getBookingById(id);
    }
    
    /**
     * Get all bookings by user ID
     */
    public List<Booking> getBookingsByUserId(Integer userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }
    
    /**
     * Get all bookings by hotel ID
     */
    public List<Booking> getBookingsByHotelId(Integer hotelId) {
        return bookingDAO.getBookingsByHotelId(hotelId);
    }
    
    /**
     * Get bookings by status
     */
    public List<Booking> getBookingsByStatus(String status) {
        return bookingDAO.getBookingsByStatus(status);
    }
    
    /**
     * Cancel booking
     */
    public boolean cancelBooking(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
            // Update booking status
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
            
            if (updated) {
                // Restore room availability
                Optional<Room> roomOpt = roomDAO.getRoomById(booking.getRoomId());
                if (roomOpt.isPresent()) {
                    Room room = roomOpt.get();
                    room.setIsAvailable(true);
                    roomDAO.updateRoom(room);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Update booking status
     */
    public boolean updateBookingStatus(Integer bookingId, String status) {
        return bookingDAO.updateBookingStatus(bookingId, status);
    }
    
    /**
     * Delete booking
     */
    public boolean deleteBooking(Integer bookingId) {
        return bookingDAO.deleteBooking(bookingId);
    }
    
    /**
     * Complete booking (checkout)
     */
    public boolean completeBooking(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
            // Update booking status
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "COMPLETED");
            
            if (updated) {
                // Restore room availability
                Optional<Room> roomOpt = roomDAO.getRoomById(booking.getRoomId());
                if (roomOpt.isPresent()) {
                    Room room = roomOpt.get();
                    room.setIsAvailable(true);
                    roomDAO.updateRoom(room);
                }
                return true;
            }
        }
        return false;
    }
}
package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.User;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminService {
    private UserDAO userDAO;
    private HotelDAO hotelDAO;
    private BookingDAO bookingDAO;
    
    public AdminService(UserDAO userDAO, HotelDAO hotelDAO, BookingDAO bookingDAO) {
        this.userDAO = userDAO;
        this.hotelDAO = hotelDAO;
        this.bookingDAO = bookingDAO;
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get user list: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all hotels
     */
    public List<Hotel> getAllHotels() {
        try {
            return hotelDAO.getAllHotels();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get hotel list: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        try {
            List<Booking> allBookings = new ArrayList<>();
            List<Hotel> allHotels = hotelDAO.getAllHotels();
            
            for (Hotel hotel : allHotels) {
                List<Booking> hotelBookings = bookingDAO.getBookingsByHotelId(hotel.getId());
                allBookings.addAll(hotelBookings);
            }
            
            return allBookings;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get booking list: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get booking statistics
     */
    public String getBookingStatistics() {
        try {
            List<Hotel> hotels = hotelDAO.getAllHotels();
            int totalBookings = 0;
            int confirmedBookings = 0;
            int paidBookings = 0;
            int cancelledBookings = 0;
            BigDecimal totalRevenue = BigDecimal.ZERO;
            
            for (Hotel hotel : hotels) {
                List<Booking> hotelBookings = bookingDAO.getBookingsByHotelId(hotel.getId());
                totalBookings += hotelBookings.size();
                
                for (Booking booking : hotelBookings) {
                    switch (booking.getStatus()) {
                        case "CONFIRMED":
                            confirmedBookings++;
                            break;
                        case "PAID":
                            paidBookings++;
                            totalRevenue = totalRevenue.add(booking.getTotalPrice());
                            break;
                        case "CANCELLED":
                            cancelledBookings++;
                            break;
                    }
                }
            }
            
            return String.format(
                "=== Booking Statistics ===\n" +
                "Total Hotels: %d\n" +
                "Total Bookings: %d\n" +
                "Confirmed: %d\n" +
                "Paid: %d\n" +
                "Cancelled: %d\n" +
                "Total Revenue: $%.2f\n" +
                "==========================",
                hotels.size(), totalBookings, confirmedBookings, paidBookings, cancelledBookings, totalRevenue
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get statistics: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update user role
     */
    public boolean updateUserRole(Integer userId, String newRole) {
        try {
            Optional<User> userOpt = userDAO.getUserById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setRole(newRole);
                return userDAO.updateUser(user);
            }
            return false;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to update user role: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete user
     */
    public boolean deleteUser(Integer userId) {
        try {
            return userDAO.deleteUser(userId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to delete user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete hotel
     */
    public boolean deleteHotel(Integer hotelId) {
        try {
            return hotelDAO.deleteHotel(hotelId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to delete hotel: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cancel user booking (admin permission)
     */
    public boolean cancelUserBooking(Integer bookingId) {
        try {
            Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
            if (bookingOpt.isPresent()) {
                return bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
            }
            return false;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to cancel user booking: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get system total revenue
     */
    public BigDecimal getTotalRevenue() {
        try {
            List<Booking> allBookings = getAllBookings();
            return allBookings.stream()
                    .filter(booking -> "PAID".equals(booking.getStatus()))
                    .map(Booking::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get total revenue: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get most popular hotels
     */
    public String getMostPopularHotel() {
        try {
            List<Hotel> hotels = hotelDAO.getAllHotels();
            Hotel mostPopular = null;
            int maxBookings = 0;
            
            for (Hotel hotel : hotels) {
                List<Booking> hotelBookings = bookingDAO.getBookingsByHotelId(hotel.getId());
                int paidBookings = (int) hotelBookings.stream()
                        .filter(booking -> "PAID".equals(booking.getStatus()))
                        .count();
                
                if (paidBookings > maxBookings) {
                    maxBookings = paidBookings;
                    mostPopular = hotel;
                }
            }
            
            return mostPopular != null ? 
                String.format("%s (%d bookings)", mostPopular.getName(), maxBookings) : 
                "No bookings yet";
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get popular hotels: " + e.getMessage(), e);
        }
    }
}
package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminService {
    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;
    private final BookingDAO bookingDAO;
    
    public AdminService(UserDAO userDAO, HotelDAO hotelDAO, BookingDAO bookingDAO) {
        this.userDAO = userDAO;
        this.hotelDAO = hotelDAO;
        this.bookingDAO = bookingDAO;
    }
    
    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * 获取所有酒店
     */
    public List<Hotel> getAllHotels() {
        return hotelDAO.getAllHotels();
    }
    
    /**
     * 获取所有预订
     */
    public List<Booking> getAllBookings() {
        List<Booking> allBookings = new ArrayList<>();
        List<Hotel> allHotels = hotelDAO.getAllHotels();
        
        for (Hotel hotel : allHotels) {
            List<Booking> hotelBookings = bookingDAO.getBookingsByHotelId(hotel.getId());
            allBookings.addAll(hotelBookings);
        }
        
        return allBookings;
    }
    
    /**
     * 获取预订统计信息
     */
    public String getBookingStatistics() {
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
    }
    
    /**
     * 更新用户角色
     */
    public boolean updateUserRole(Integer userId, String newRole) {
        Optional<User> userOpt = userDAO.getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(newRole);
            return userDAO.updateUser(user);
        }
        return false;
    }
    
    /**
     * 删除用户
     */
    public boolean deleteUser(Integer userId) {
        return userDAO.deleteUser(userId);
    }
    
    /**
     * 删除酒店
     */
    public boolean deleteHotel(Integer hotelId) {
        return hotelDAO.deleteHotel(hotelId);
    }
    
    /**
     * 取消用户预订（管理员权限）
     */
    public boolean cancelUserBooking(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            return bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
        }
        return false;
    }
    
    /**
     * 获取系统总收入
     */
    public BigDecimal getTotalRevenue() {
        List<Booking> allBookings = getAllBookings();
        return allBookings.stream()
                .filter(booking -> "PAID".equals(booking.getStatus()))
                .map(Booking::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * 获取最受欢迎的酒店
     */
    public String getMostPopularHotel() {
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
    }
}
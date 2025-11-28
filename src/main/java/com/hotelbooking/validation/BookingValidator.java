// BookingValidator.java - 预订数据验证
package com.hotelbooking.validation;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.exception.ValidationException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingValidator {
    
    public static void validateBooking(Booking booking) {
        // 基本数据验证
        validateBasicData(booking);
        
        // 日期验证
        validateDates(booking.getCheckInDate(), booking.getCheckOutDate());
        
        // 业务规则验证
        validateBusinessRules(booking);
    }
    
    private static void validateBasicData(Booking booking) {
        if (booking.getUserId() <= 0) {
            throw new ValidationException("用户ID无效");
        }
        
        if (booking.getHotelId() <= 0) {
            throw new ValidationException("酒店ID无效");
        }
        
        if (booking.getRoomId() <= 0) {
            throw new ValidationException("房间ID无效");
        }
        
        if (booking.getTotalPrice() == null || booking.getTotalPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("价格必须大于0");
        }
    }
    
    private static void validateDates(LocalDate checkIn, LocalDate checkOut) {
        LocalDate today = LocalDate.now();
        
        // 入住日期不能早于今天
        if (checkIn.isBefore(today)) {
            throw new ValidationException("入住日期不能是过去日期");
        }
        
        // 至少提前1天预订
        if (checkIn.isBefore(today.plusDays(1))) {
            throw new ValidationException("请至少提前1天预订");
        }
        
        // 离店日期必须晚于入住日期
        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException("离店日期必须晚于入住日期");
        }
        
        // 最长入住天数限制（30天）
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights > 30) {
            throw new ValidationException("单次入住最多30天");
        }
        
        if (nights < 1) {
            throw new ValidationException("至少需要入住1晚");
        }
    }
    
    private static void validateBusinessRules(Booking booking) {
        // 这里可以添加更多的业务规则验证
        // 例如：特殊日期限制、会员等级限制等
    }
}
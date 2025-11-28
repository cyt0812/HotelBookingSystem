package com.hotelbooking.service;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import com.hotelbooking.validation.BookingValidator;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;
    private final PaymentService paymentService;
    
    public BookingService(BookingDAO bookingDAO, RoomDAO roomDAO, PaymentService paymentService) {
        this.bookingDAO = bookingDAO;
        this.roomDAO = roomDAO;
        this.paymentService = paymentService;
    }
    
    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.roomDAO = new RoomDAO();
        this.paymentService = new PaymentService();
    }
    
    /**
     * 创建预订
     */
    public Booking createBooking(Integer userId, Integer hotelId, Integer roomId, 
                               LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            // 使用新的验证逻辑
            validateBookingDates(checkInDate, checkOutDate);
            
            // 检查房间是否存在且可用
            Room room = roomDAO.getRoomById(roomId)
                    .orElseThrow(() -> new BusinessException(ErrorType.ROOM_NOT_FOUND));
            
            if (!room.isAvailable()) {
                throw new BusinessException(ErrorType.ROOM_NOT_AVAILABLE);
            }
            
            // 检查日期冲突
            if (!isRoomAvailable(roomId, checkInDate, checkOutDate)) {
                throw new BusinessException(ErrorType.BOOKING_CONFLICT);
            }
            
            // 计算总价
            long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(numberOfNights));
            
            // 创建预订对象
            Booking booking = new Booking(userId, hotelId, roomId, checkInDate, checkOutDate, 
                                        totalPrice, "CONFIRMED");
            
            // 创建预订记录
            Booking createdBooking = bookingDAO.createBooking(booking);
            if (createdBooking == null) {
                throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "创建预订失败");
            }
            
            // 更新房间状态为不可用
            room.setAvailable(false);
            roomDAO.updateRoom(room);
            
            return createdBooking;
            
        } catch (BusinessException | ValidationException e) {
            // 重新抛出，让Controller层处理
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "创建预订时发生系统错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建带支付的预订
     */
    public Booking createBookingWithPayment(Integer userId, Integer hotelId, Integer roomId, 
                                          LocalDate checkInDate, LocalDate checkOutDate, 
                                          String paymentMethod) {
        try {
            // 创建临时预订对象用于验证
            Booking tempBooking = new Booking(userId, hotelId, roomId, checkInDate, checkOutDate, 
                                            BigDecimal.ZERO, "PENDING");
            
            // 1. 数据验证
            BookingValidator.validateBooking(tempBooking);
            
            // 2. 检查房间可用性
            if (!isRoomAvailable(roomId, checkInDate, checkOutDate)) {
                throw new BusinessException(ErrorType.ROOM_NOT_AVAILABLE, 
                    "房间在指定时间段不可用");
            }
            
            // 3. 计算实际价格
            Room room = roomDAO.getRoomById(roomId)
                    .orElseThrow(() -> new BusinessException(ErrorType.ROOM_NOT_FOUND));
            
            long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(numberOfNights));
            
            // 4. 创建正式预订对象
            Booking booking = new Booking(userId, hotelId, roomId, checkInDate, checkOutDate, 
                                        totalPrice, "PENDING");
            
            // 5. 创建预订记录
            Booking createdBooking = bookingDAO.createBooking(booking);
            if (createdBooking == null) {
                throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "创建预订记录失败");
            }
            
            // 6. 处理支付
            boolean paymentSuccess = paymentService.processPayment(
                createdBooking.getBookingId(), totalPrice, paymentMethod);
            
            if (!paymentSuccess) {
                // 支付失败，回滚预订
                bookingDAO.cancelBooking(createdBooking.getBookingId());
                throw new BusinessException(ErrorType.PAYMENT_FAILED, "支付失败，预订已取消");
            }
            
            // 7. 更新预订状态为已确认
            bookingDAO.updateBookingStatus(createdBooking.getBookingId(), "CONFIRMED");
            createdBooking.setStatus("CONFIRMED");
            
            // 8. 更新房间状态
            room.setAvailable(false);
            roomDAO.updateRoom(room);
            
            return createdBooking;
            
        } catch (BusinessException | ValidationException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            // 确保在异常情况下回滚
            rollbackBookingCreation(roomId);
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "创建带支付预订时发生系统错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 取消预订并退款
     */
    public boolean cancelBookingWithRefund(Integer bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId)
                    .orElseThrow(() -> new BusinessException(ErrorType.BOOKING_NOT_FOUND));
            
            // 检查预订状态是否可以取消
            if (!"CONFIRMED".equals(booking.getStatus()) && !"PENDING".equals(booking.getStatus())) {
                throw new BusinessException(ErrorType.BOOKING_CANNOT_CANCEL,
                    "当前状态的预订无法取消");
            }
            
            // 1. 取消预订
            boolean bookingCancelled = bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
            if (!bookingCancelled) {
                throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "取消预订失败");
            }
            
            // 2. 恢复房间可用性
            Optional<Room> roomOpt = roomDAO.getRoomById(booking.getRoomId());
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setAvailable(true);
                roomDAO.updateRoom(room);
            }
            
            // 3. 处理退款（只有已支付的预订才退款）
            if ("CONFIRMED".equals(booking.getStatus())) {
                boolean refundSuccess = paymentService.processRefund(booking.getBookingId());
                if (!refundSuccess) {
                    throw new BusinessException(ErrorType.REFUND_FAILED);
                }
                return refundSuccess;
            }
            
            return true;
            
        } catch (BusinessException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "取消预订时发生系统错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检查房间在指定时间段是否可用
     */
    public boolean isRoomAvailable(Integer roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            // 检查房间基础可用性
            Optional<Room> roomOpt = roomDAO.getRoomById(roomId);
            if (roomOpt.isEmpty() || !roomOpt.get().isAvailable()) {
                return false;
            }
            
            // 检查是否有时间冲突的预订
            List<Booking> existingBookings = bookingDAO.getBookingsByRoomId(roomId);
            for (Booking existing : existingBookings) {
                if (hasDateConflict(existing, checkInDate, checkOutDate)) {
                    return false;
                }
            }
            
            return true;
            
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "检查房间可用性失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 验证预订日期
     */
    private void validateBookingDates(LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDate today = LocalDate.now();
        
        if (checkInDate == null || checkOutDate == null) {
            throw new ValidationException("入住日期和离店日期不能为空");
        }
        
        if (checkInDate.isBefore(today)) {
            throw new ValidationException("入住日期不能是过去日期");
        }
        
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new ValidationException("离店日期必须晚于入住日期");
        }
        
        // 至少提前1天预订
        if (checkInDate.isBefore(today.plusDays(1))) {
            throw new ValidationException("请至少提前1天预订");
        }
    }
    
    /**
     * 检查日期冲突
     */
    private boolean hasDateConflict(Booking existing, LocalDate newCheckIn, LocalDate newCheckOut) {
        if (!"CANCELLED".equals(existing.getStatus()) && !"COMPLETED".equals(existing.getStatus())) {
            LocalDate existingCheckIn = existing.getCheckInDate();
            LocalDate existingCheckOut = existing.getCheckOutDate();
            
            // 检查日期重叠
            return newCheckIn.isBefore(existingCheckOut) && newCheckOut.isAfter(existingCheckIn);
        }
        return false;
    }
    
    /**
     * 回滚预订创建
     */
    private void rollbackBookingCreation(Integer roomId) {
        try {
            // 恢复房间状态
            Optional<Room> roomOpt = roomDAO.getRoomById(roomId);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setAvailable(true);
                roomDAO.updateRoom(room);
            }
        } catch (Exception e) {
            System.err.println("回滚失败: " + e.getMessage());
        }
    }
    
    // 查询方法 - 保持Optional返回，让调用方决定如何处理空值
    
    /**
     * 根据ID获取预订
     */
    public Optional<Booking> getBookingById(Integer id) {
        try {
            return bookingDAO.getBookingById(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取预订信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户的所有预订
     */
    public List<Booking> getBookingsByUserId(Integer userId) {
        try {
            return bookingDAO.getBookingsByUserId(userId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取用户预订列表失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取酒店的所有预订
     */
    public List<Booking> getBookingsByHotelId(Integer hotelId) {
        try {
            return bookingDAO.getBookingsByHotelId(hotelId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "获取酒店预订列表失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据状态获取预订
     */
    public List<Booking> getBookingsByStatus(String status) {
        try {
            return bookingDAO.getBookingsByStatus(status);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "根据状态获取预订列表失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 取消预订（兼容方法）
     */
    public boolean cancelBooking(Integer bookingId) {
        return cancelBookingWithRefund(bookingId);
    }
    
    /**
     * 更新预订状态
     */
    public boolean updateBookingStatus(Integer bookingId, String status) {
        try {
            return bookingDAO.updateBookingStatus(bookingId, status);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "更新预订状态失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除预订
     */
    public boolean deleteBooking(Integer bookingId) {
        try {
            return bookingDAO.deleteBooking(bookingId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "删除预订失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 完成预订（退房）
     */
    public boolean completeBooking(Integer bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId)
                    .orElseThrow(() -> new BusinessException(ErrorType.BOOKING_NOT_FOUND));
            
            // 更新预订状态
            boolean updated = bookingDAO.updateBookingStatus(bookingId, "COMPLETED");
            
            if (updated) {
                // 恢复房间可用性
                Optional<Room> roomOpt = roomDAO.getRoomById(booking.getRoomId());
                if (roomOpt.isPresent()) {
                    Room room = roomOpt.get();
                    room.setAvailable(true);
                    roomDAO.updateRoom(room);
                }
                return true;
            }
            return false;
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "完成预订失败: " + e.getMessage(), e);
        }
    }
}
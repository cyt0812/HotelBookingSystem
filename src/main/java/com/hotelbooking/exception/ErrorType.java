package com.hotelbooking.exception;

/**
 * 错误类型枚举
 * 统一管理所有错误代码和消息
 */
public enum ErrorType {
    // User-related errors
    USER_NOT_FOUND("USER_001", "User not found"),
    INVALID_CREDENTIALS("USER_002", "Invalid username or password"),
    USERNAME_EXISTS("USER_003", "Username already exists"),
    EMAIL_EXISTS("USER_004", "Email already exists"),
    USER_ALREADY_LOGGED_IN("USER_005", "User already logged in"),
    INSUFFICIENT_PERMISSION("USER_006", "Insufficient permission"),
    
    // Hotel-related errors
    HOTEL_NOT_FOUND("HOTEL_001", "Hotel not found"),
    HOTEL_ALREADY_EXISTS("HOTEL_002", "Hotel already exists"),
    INVALID_HOTEL_DATA("HOTEL_003", "Invalid hotel data"),
    
    // Room-related errors
    ROOM_NOT_FOUND("ROOM_001", "Room not found"),
    ROOM_NOT_AVAILABLE("ROOM_002", "Room unavailable"),
    ROOM_ALREADY_EXISTS("ROOM_003", "Room number already exists"),
    INVALID_ROOM_DATA("ROOM_004", "Invalid room data"),
    
    // Booking-related errors
    BOOKING_NOT_FOUND("BOOKING_001", "Booking not found"),
    INVALID_BOOKING_DATES("BOOKING_002", "Invalid booking dates"),
    BOOKING_CONFLICT("BOOKING_003", "Booking conflict"),
    INVALID_BOOKING_STATUS("BOOKING_004", "Invalid booking status"),
    BOOKING_CANNOT_CANCEL("BOOKING_005", "Booking cannot be cancelled in current status"),
    BOOKING_ALREADY_COMPLETED("BOOKING_006", "Booking already completed"),
    
    // Payment-related errors
    PAYMENT_FAILED("PAYMENT_001", "Payment failed"),
    PAYMENT_NOT_FOUND("PAYMENT_002", "Payment record not found"),
    REFUND_FAILED("PAYMENT_003", "Refund failed"),
    INVALID_PAYMENT_METHOD("PAYMENT_004", "Invalid payment method"),
    PAYMENT_ALREADY_PROCESSED("PAYMENT_005", "Payment already processed"),
    
    // Validation errors
    VALIDATION_ERROR("VALIDATION_001", "Data validation failed"),
    INVALID_PARAMETER("VALIDATION_002", "Invalid parameter"),
    MISSING_REQUIRED_FIELD("VALIDATION_003", "Missing required field"),
    
    // System errors
    DATABASE_ERROR("SYSTEM_001", "Database error"),
      DATABASE_CONNECTION_ERROR("SYSTEM_002", "Database connection failed"),
    INTERNAL_SERVER_ERROR("SYSTEM_003", "Internal server error"),
    EXTERNAL_SERVICE_ERROR("SYSTEM_004", "External service error"),
    FILE_PROCESSING_ERROR("SYSTEM_005", "File processing error");
    
    private final String code;
    private final String message;
    
    ErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据错误代码查找对应的ErrorType
     */
    public static ErrorType fromCode(String code) {
        for (ErrorType errorType : values()) {
            if (errorType.getCode().equals(code)) {
                return errorType;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
}
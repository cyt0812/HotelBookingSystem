package com.hotelbooking.exception;

/**
 * 错误类型枚举
 * 统一管理所有错误代码和消息
 */
public enum ErrorType {
    // ========== 用户相关错误 ==========
    USER_NOT_FOUND("USER_001", "用户不存在"),
    INVALID_CREDENTIALS("USER_002", "用户名或密码错误"),
    USERNAME_EXISTS("USER_003", "用户名已存在"),
    EMAIL_EXISTS("USER_004", "邮箱已存在"),
    USER_ALREADY_LOGGED_IN("USER_005", "用户已登录"),
    INSUFFICIENT_PERMISSION("USER_006", "权限不足"),
    
    // ========== 酒店相关错误 ==========
    HOTEL_NOT_FOUND("HOTEL_001", "酒店不存在"),
    HOTEL_ALREADY_EXISTS("HOTEL_002", "酒店已存在"),
    INVALID_HOTEL_DATA("HOTEL_003", "酒店数据无效"),
    
    // ========== 房间相关错误 ==========
    ROOM_NOT_FOUND("ROOM_001", "房间不存在"),
    ROOM_NOT_AVAILABLE("ROOM_002", "房间不可用"),
    ROOM_ALREADY_EXISTS("ROOM_003", "房间号已存在"),
    INVALID_ROOM_DATA("ROOM_004", "房间数据无效"),
    
    // ========== 预订相关错误 ==========
    BOOKING_NOT_FOUND("BOOKING_001", "预订不存在"),
    INVALID_BOOKING_DATES("BOOKING_002", "无效的入住日期"),
    BOOKING_CONFLICT("BOOKING_003", "预订时间冲突"),
    INVALID_BOOKING_STATUS("BOOKING_004", "无效的预订状态"),
    BOOKING_CANNOT_CANCEL("BOOKING_005", "当前状态的预订无法取消"),
    BOOKING_ALREADY_COMPLETED("BOOKING_006", "预订已完成"),
    
    // ========== 支付相关错误 ==========
    PAYMENT_FAILED("PAYMENT_001", "支付失败"),
    PAYMENT_NOT_FOUND("PAYMENT_002", "支付记录不存在"),
    REFUND_FAILED("PAYMENT_003", "退款失败"),
    INVALID_PAYMENT_METHOD("PAYMENT_004", "无效的支付方式"),
    PAYMENT_ALREADY_PROCESSED("PAYMENT_005", "支付已处理"),
    
    // ========== 验证相关错误 ==========
    VALIDATION_ERROR("VALIDATION_001", "数据验证失败"),
    INVALID_PARAMETER("VALIDATION_002", "参数无效"),
    MISSING_REQUIRED_FIELD("VALIDATION_003", "缺少必填字段"),
    
    // ========== 系统相关错误 ==========
    DATABASE_ERROR("SYSTEM_001", "数据库错误"),
    DATABASE_CONNECTION_ERROR("SYSTEM_002", "数据库连接失败"),
    INTERNAL_SERVER_ERROR("SYSTEM_003", "系统内部错误"),
    EXTERNAL_SERVICE_ERROR("SYSTEM_004", "外部服务错误"),
    FILE_PROCESSING_ERROR("SYSTEM_005", "文件处理错误");
    
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
package com.hotelbooking.exception;

import com.hotelbooking.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 负责将各种异常转换为统一的API响应格式
 */
public class GlobalExceptionHandler {
    
    private static final Map<String, String> ERROR_MESSAGES = new HashMap<>();
    
    static {
        // 预定义错误消息映射（备用，优先使用ErrorType中的消息）
        initializeErrorMessages();
    }
    
    /**
     * 处理业务异常
     */
    public static ApiResponse<Object> handleBusinessException(BusinessException e) {
        return ApiResponse.error(e.getMessage(), e.getErrorCode());
    }
    
    /**
     * 处理验证异常
     */
    public static ApiResponse<Object> handleValidationException(ValidationException e) {
        return ApiResponse.error(e.getMessage(), ErrorType.VALIDATION_ERROR.getCode());
    }
    
    /**
     * 处理通用异常
     */
    public static ApiResponse<Object> handleGenericException(Exception e) {
        // 记录系统异常日志（在实际项目中应该使用日志框架）
        System.err.println("System error occurred: " + e.getClass().getSimpleName());
        System.err.println("Error message: " + e.getMessage());
        e.printStackTrace();
        
        return ApiResponse.error(
            "系统繁忙，请稍后重试", 
            ErrorType.INTERNAL_SERVER_ERROR.getCode()
        );
    }
    
    /**
     * 统一异常处理方法
     */
    public static ApiResponse<Object> handleException(Exception e) {
        if (e instanceof BusinessException) {
            return handleBusinessException((BusinessException) e);
        } else if (e instanceof ValidationException) {
            return handleValidationException((ValidationException) e);
        } else {
            return handleGenericException(e);
        }
    }
    
    /**
     * 根据错误代码获取用户友好的错误消息
     */
    public static String getFriendlyErrorMessage(String errorCode) {
        return ERROR_MESSAGES.getOrDefault(errorCode, "系统错误，请稍后重试");
    }
    
    /**
     * 初始化错误消息映射
     */
    private static void initializeErrorMessages() {
        // 用户相关
        ERROR_MESSAGES.put("USER_001", "用户不存在，请检查用户名或注册新账户");
        ERROR_MESSAGES.put("USER_002", "用户名或密码错误，请重新输入");
        ERROR_MESSAGES.put("USER_003", "该用户名已被使用，请选择其他用户名");
        ERROR_MESSAGES.put("USER_004", "该邮箱已被注册，请使用其他邮箱或找回密码");
        ERROR_MESSAGES.put("USER_006", "您没有执行此操作的权限");
        
        // 房间相关
        ERROR_MESSAGES.put("ROOM_002", "该房间在当前时间段不可用，请选择其他房间或调整日期");
        
        // 预订相关
        ERROR_MESSAGES.put("BOOKING_002", "入住日期必须晚于今天，离店日期必须晚于入住日期");
        ERROR_MESSAGES.put("BOOKING_003", "您选择的日期与其他预订冲突，请调整日期");
        ERROR_MESSAGES.put("BOOKING_005", "该预订无法取消，可能已过期或正在处理中");
        
        // 支付相关
        ERROR_MESSAGES.put("PAYMENT_001", "支付失败，请检查支付信息或尝试其他支付方式");
        ERROR_MESSAGES.put("PAYMENT_003", "退款处理失败，请联系客服");
        
        // 系统相关
        ERROR_MESSAGES.put("SYSTEM_002", "系统暂时无法连接，请检查网络后重试");
        ERROR_MESSAGES.put("SYSTEM_003", "系统繁忙，请稍后重试");
    }
    
    /**
     * 记录异常日志（在实际项目中应该集成日志框架）
     */
    public static void logException(Exception e) {
        System.err.println("=== Exception Log ===");
        System.err.println("Time: " + java.time.LocalDateTime.now());
        System.err.println("Type: " + e.getClass().getSimpleName());
        System.err.println("Message: " + e.getMessage());
        System.err.println("Stack Trace:");
        e.printStackTrace();
        System.err.println("=====================");
    }
}
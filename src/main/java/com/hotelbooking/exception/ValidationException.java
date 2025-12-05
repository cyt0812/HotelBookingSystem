package com.hotelbooking.exception;

/**
 * 数据验证异常
 * 用于参数验证、数据格式验证等场景
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
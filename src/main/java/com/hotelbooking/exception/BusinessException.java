package com.hotelbooking.exception;

/**
 * 业务异常基类
 * 用于所有业务逻辑相关的异常
 */
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final ErrorType errorType;
    
    public BusinessException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorCode = errorType.getCode();
        this.errorType = errorType;
    }
    
    public BusinessException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorCode = errorType.getCode();
        this.errorType = errorType;
    }
    
    public BusinessException(ErrorType errorType, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorType.getCode();
        this.errorType = errorType;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
}
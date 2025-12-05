package com.hotelbooking.exception;

public class UserNotFoundException extends RuntimeException {
    
    // 构造函数
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
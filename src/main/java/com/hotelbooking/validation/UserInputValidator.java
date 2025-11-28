// UserInputValidator.java - 用户输入验证
package com.hotelbooking.validation;

import com.hotelbooking.exception.ValidationException;
import java.util.regex.Pattern;

public class UserInputValidator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^1[3-9]\\d{9}$"); // 简单的手机号验证
    
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("邮箱不能为空");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("邮箱格式不正确");
        }
    }
    
    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("密码不能为空");
        }
        
        if (password.length() < 6) {
            throw new ValidationException("密码至少6位字符");
        }
    }
    
    public static void validatePhone(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                throw new ValidationException("手机号格式不正确");
            }
        }
    }
}
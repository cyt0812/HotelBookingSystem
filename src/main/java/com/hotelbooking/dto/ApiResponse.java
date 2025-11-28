package com.hotelbooking.dto;

// 移除 @JsonInclude 注解，或者使用条件编译
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private Long timestamp;
    
    // 构造器 - 只序列化非null字段
    public ApiResponse(boolean success, String message, T data, String errorCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
        
        // 手动处理null值：如果errorCode为null，在getter中返回null
    }
    
    // 成功响应静态方法
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "操作成功", data, null);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }
    
    // 错误响应静态方法
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, null);
    }
    
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, message, null, errorCode);
    }
    
    // Getter方法 - 手动控制null值返回
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getErrorCode() { return errorCode; } // 如果为null，Jackson会自动忽略
    public Long getTimestamp() { return timestamp; }
    
    // 添加一个方法来检查errorCode是否应该序列化
    public boolean hasErrorCode() {
        return errorCode != null;
    }
}
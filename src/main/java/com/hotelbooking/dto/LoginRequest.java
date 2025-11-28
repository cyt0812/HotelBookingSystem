package com.hotelbooking.dto;

public class LoginRequest {
    private String username;
    private String password;
    
    // 默认构造器
    public LoginRequest() {}
    
    // 全参数构造器
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getter 和 Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
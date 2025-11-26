package com.hotelbooking.entity;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String role;
    private boolean status; // 新增：用户状态（true=启用，false=禁用）

    // 构造方法
    public User() {}

    public User(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = "CUSTOMER"; // 默认角色为普通用户
        this.status = true;     // 默认状态为启用
    }

    // Getter 和 Setter 方法（补充status的get/set）
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isStatus() { return status; } // 新增
    public void setStatus(boolean status) { this.status = status; } // 新增

    @Override
    public String toString() {
        return "User{userId=" + userId + ", username='" + username + "', role='" + role + "', status=" + status + "}";
    }
}
package com.hotelbooking.dao;

import com.hotelbooking.entity.User;
import com.hotelbooking.util.DatabaseConnection;

import java.sql.*;

public class UserDAO {

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, email, full_name, role) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getRole());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("创建用户失败: " + e.getMessage());
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("查询用户失败: " + e.getMessage());
        }
        return null;
    }

    // 测试方法
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        
        // 测试查询用户
        User user = userDAO.getUserByUsername("testuser");
        if (user != null) {
            System.out.println("✅ 找到用户: " + user.getUsername());
        } else {
            System.out.println("❌ 用户不存在");
        }
    }
}
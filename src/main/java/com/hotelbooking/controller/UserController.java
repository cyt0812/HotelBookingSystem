package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
import com.hotelbooking.entity.User;
import com.hotelbooking.service.UserService;
import com.hotelbooking.exception.GlobalExceptionHandler;

import java.util.List;
import java.util.Optional;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * User registration
     */
//    public ApiResponse<Object> registerUser(String username, String email, String password,String role) {
//        try {
//            User user = userService.registerUser(username, email, password,role)
//                    .orElseThrow(() -> new RuntimeException("注册失败，请重试"));
//
//            return ApiResponse.success("User registration successful", user);
//        } catch (Exception e) {
//            return GlobalExceptionHandler.handleException(e);
//        }
//    }

    /**
     * User login
     */
    
//    public ApiResponse<Object> loginUser(String username, String password) {
//        try {
//            User user = userService.loginUser(username, password)
//                    .orElseThrow(() -> new RuntimeException("登录失败，用户名或密码错误"));
//
//            return ApiResponse.success("User login successful", user);
//        } catch (Exception e) {
//            return GlobalExceptionHandler.handleException(e);
//        }
//    }

    /**
     * Get user by ID
     */
    public ApiResponse<Object> getUserById(Integer id) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found, ID: " + id));

            return ApiResponse.success("User information retrieved successfully", user);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * Get user by username
     */
    public ApiResponse<Object> getUserByUsername(String username) {
        try {
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found, username: " + username));

            return ApiResponse.success("User information retrieved successfully", user);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * Get all users
     */
    public ApiResponse<Object> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ApiResponse.success("User list retrieved successfully", users);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * Update user information
     */
    public ApiResponse<Object> updateUser(User user) {
        try {
            boolean updated = userService.updateUser(user);
            return ApiResponse.success(updated ? "User information updated successfully" : "User information update failed", updated);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * Delete user
     */
    public ApiResponse<Object> deleteUser(Integer id) {
        try {
            boolean deleted = userService.deleteUser(id);
            return ApiResponse.success(deleted ? "User deleted successfully" : "User deletion failed", deleted);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * Validate credentials
     */
    public ApiResponse<Object> validateCredentials(String username, String password) {
        try {
            Optional<User> userOpt = userService.getUserByUsername(username);
            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                return ApiResponse.success("Credentials validated successfully", true);
            } else {
                return ApiResponse.success("Credentials validation failed", false);
            }
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * Check if username exists
     */
    public ApiResponse<Object> isUsernameExists(String username) {
        try {
            boolean exists = userService.getUserByUsername(username).isPresent();
            return ApiResponse.success(exists ? "Username already exists" : "Username is available", exists);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * Check if email exists
     */
    public ApiResponse<Object> isEmailExists(String email) {
        try {
            boolean exists = userService.getAllUsers().stream()
                    .anyMatch(user -> user.getEmail().equals(email));
            return ApiResponse.success(exists ? "Email already exists" : "Email is available", exists);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
}
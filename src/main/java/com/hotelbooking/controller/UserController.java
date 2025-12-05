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
     * 用户注册
     */
//    public ApiResponse<Object> registerUser(String username, String email, String password,String role) {
//        try {
//            User user = userService.registerUser(username, email, password,role)
//                    .orElseThrow(() -> new RuntimeException("注册失败，请重试"));
//
//            return ApiResponse.success("用户注册成功", user);
//        } catch (Exception e) {
//            return GlobalExceptionHandler.handleException(e);
//        }
//    }

    /**
     * 用户登录
     */
    
//    public ApiResponse<Object> loginUser(String username, String password) {
//        try {
//            User user = userService.loginUser(username, password)
//                    .orElseThrow(() -> new RuntimeException("登录失败，用户名或密码错误"));
//
//            return ApiResponse.success("用户登录成功", user);
//        } catch (Exception e) {
//            return GlobalExceptionHandler.handleException(e);
//        }
//    }

    /**
     * 根据ID获取用户
     */
    public ApiResponse<Object> getUserById(Integer id) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + id));

            return ApiResponse.success("获取用户信息成功", user);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 根据用户名获取用户
     */
    public ApiResponse<Object> getUserByUsername(String username) {
        try {
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在，用户名: " + username));

            return ApiResponse.success("获取用户信息成功", user);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 获取所有用户
     */
    public ApiResponse<Object> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ApiResponse.success("获取用户列表成功", users);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 更新用户信息
     */
    public ApiResponse<Object> updateUser(User user) {
        try {
            boolean updated = userService.updateUser(user);
            return ApiResponse.success(updated ? "用户信息更新成功" : "用户信息更新失败", updated);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 删除用户
     */
    public ApiResponse<Object> deleteUser(Integer id) {
        try {
            boolean deleted = userService.deleteUser(id);
            return ApiResponse.success(deleted ? "用户删除成功" : "用户删除失败", deleted);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 校验凭据
     */
    public ApiResponse<Object> validateCredentials(String username, String password) {
        try {
            Optional<User> userOpt = userService.getUserByUsername(username);
            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                return ApiResponse.success("凭据验证成功", true);
            } else {
                return ApiResponse.success("凭据验证失败", false);
            }
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 检查用户名是否存在
     */
    public ApiResponse<Object> isUsernameExists(String username) {
        try {
            boolean exists = userService.getUserByUsername(username).isPresent();
            return ApiResponse.success(exists ? "用户名已存在" : "用户名可用", exists);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }

    /**
     * 检查邮箱是否存在
     */
    public ApiResponse<Object> isEmailExists(String email) {
        try {
            boolean exists = userService.getAllUsers().stream()
                    .anyMatch(user -> user.getEmail().equals(email));
            return ApiResponse.success(exists ? "邮箱已存在" : "邮箱可用", exists);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
}
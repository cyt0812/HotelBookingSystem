package com.hotelbooking.service;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
<<<<<<< HEAD
     * Register a new user
     */
    public User registerUser(String username, String email, String password, String role) {
        try {
            // Basic validation
            if (username == null || username.trim().isEmpty()) {
                throw new ValidationException("Username cannot be empty");
            }
            if (email == null || !email.contains("@")) {
                throw new ValidationException("Invalid email format");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new ValidationException("Password cannot be empty");
=======
     * 注册新用户
     */
    public User registerUser(String username, String email, String password, String role) {
        try {
            // 基本校验
            if (username == null || username.trim().isEmpty()) {
                throw new ValidationException("用户名不能为空");
            }
            if (email == null || !email.contains("@")) {
                throw new ValidationException("邮箱格式无效");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new ValidationException("密码不能为空");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            }
            if (role == null || role.trim().isEmpty()) {
                role = "CUSTOMER";
            }

<<<<<<< HEAD
            // Uniqueness validation
=======
            // 唯一性校验
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            if (userDAO.isUsernameExists(username)) {
                throw new BusinessException(ErrorType.USERNAME_EXISTS);
            }
            if (userDAO.isEmailExists(email)) {
                throw new BusinessException(ErrorType.EMAIL_EXISTS);
            }

<<<<<<< HEAD
            // Create user entity
            User user = new User(username.trim(), email.trim(), password, role);
            user.setCreatedAt(LocalDateTime.now()); // ⭐ Must be set, otherwise database insertion fails

            // Write to database
            System.out.println("Preparing to call createUser: " + username);
            User savedUser = userDAO.createUser(user);
            System.out.println("createUser call completed: " + savedUser.getId());
=======
            // 创建用户实体
            User user = new User(username.trim(), email.trim(), password, role);
            user.setCreatedAt(LocalDateTime.now()); // ⭐ 必须设置，否则数据库插入失败

            // 写入数据库
            System.out.println("准备调用 createUser: " + username);
            User savedUser = userDAO.createUser(user);
            System.out.println("createUser 调用结束: " + savedUser.getId());
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            return savedUser;

        } catch (BusinessException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
<<<<<<< HEAD
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to register user: " + e.getMessage(), e);
=======
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "用户注册失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }

    /**
<<<<<<< HEAD
     * User login (compatible with new and old versions)
     */
   public User loginUser(String username, String password) {
    try {
        // Fix 1: First validate null values, must throw ValidationException
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
        
        // Fix 2: Simplify login logic, choose one based on actual situation
        // Prefer to use the new authenticateUser method
=======
     * 用户登录（兼容新老版本）
     */
   public User loginUser(String username, String password) {
    try {
        // 修复点1：先进行空值验证，必须抛出 ValidationException
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("密码不能为空");
        }
        
        // 修复点2：简化登录逻辑，根据实际情况选择一种
        // 优先使用新版的 authenticateUser 方法
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        try {
            Optional<User> authenticatedUser = userDAO.authenticateUser(username.trim(), password.trim());
            if (authenticatedUser.isPresent()) {
                return authenticatedUser.get();
            }
<<<<<<< HEAD
            // User does not exist or password is incorrect
            throw new BusinessException(ErrorType.INVALID_CREDENTIALS);
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            // If userDAO doesn't have authenticateUser method, use old logic
            System.out.println("Using old login logic...");
=======
            // 用户不存在或密码错误
            throw new BusinessException(ErrorType.INVALID_CREDENTIALS);
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            // 如果 userDAO 没有 authenticateUser 方法，使用旧版逻辑
            System.out.println("使用旧版登录逻辑...");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            return loginUserLegacy(username.trim(), password.trim());
        }

    } catch (ValidationException e) {
<<<<<<< HEAD
        // Fix 3: Ensure ValidationException is properly thrown
=======
        // 修复点3：确保 ValidationException 被正确抛出
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        throw e;
    } catch (BusinessException e) {
        throw e;
    } catch (Exception e) {
        throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
            "Failed to login: " + e.getMessage(), e);
    }
}
   /**
     * Login parameter validation
     */
    private void validateLoginParameters(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
    }
    /**
     * User login (old version logic)
=======
            "用户登录失败: " + e.getMessage(), e);
    }
}
   /**
     * 登录参数验证
     */
    private void validateLoginParameters(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("密码不能为空");
        }
    }
    /**
     * 用户登录（旧版本逻辑）
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
   public User loginUserLegacy(String username, String password) {
        try {
            if (username == null || password == null) {
<<<<<<< HEAD
                throw new ValidationException("Username and password cannot be empty");
            }
            
            // Old version logic
=======
                throw new ValidationException("用户名和密码不能为空");
            }
            
            // 旧版本的逻辑
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            User user = userDAO.getUserByUsername(username)
                    .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));
            
            if (!user.getPassword().equals(password)) {
                throw new BusinessException(ErrorType.INVALID_CREDENTIALS);
            }
            
            return user;
        } catch (BusinessException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
<<<<<<< HEAD
                "Failed to login: " + e.getMessage(), e);
=======
                "用户登录失败: " + e.getMessage(), e);
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }

    /**
<<<<<<< HEAD
     * Get all users
=======
     * 获取所有用户
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
<<<<<<< HEAD
     * Get user by ID
=======
     * 根据ID获取用户
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public Optional<User> getUserById(Integer id) {
        return userDAO.getUserById(id);
    }

    /**
<<<<<<< HEAD
     * Get user by username
=======
     * 根据用户名获取用户
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public Optional<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
<<<<<<< HEAD
     * Delete user
=======
     * 删除用户
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public boolean deleteUser(Integer userId) {
        return userDAO.deleteUser(userId);
    }

    /**
<<<<<<< HEAD
     * Update user
=======
     * 更新用户
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        return userDAO.updateUser(user);
    }
}
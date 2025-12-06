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
            }
            if (role == null || role.trim().isEmpty()) {
                role = "CUSTOMER";
            }

            // Uniqueness validation
            if (userDAO.isUsernameExists(username)) {
                throw new BusinessException(ErrorType.USERNAME_EXISTS);
            }
            if (userDAO.isEmailExists(email)) {
                throw new BusinessException(ErrorType.EMAIL_EXISTS);
            }

            // Create user entity
            User user = new User(username.trim(), email.trim(), password, role);
            user.setCreatedAt(LocalDateTime.now()); // ⭐ Must be set, otherwise database insertion fails

            // Write to database
            System.out.println("Preparing to call createUser: " + username);
            User savedUser = userDAO.createUser(user);
            System.out.println("createUser call completed: " + savedUser.getId());
            return savedUser;

        } catch (BusinessException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, "Failed to register user: " + e.getMessage(), e);
        }
    }

    /**
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
        try {
            Optional<User> authenticatedUser = userDAO.authenticateUser(username.trim(), password.trim());
            if (authenticatedUser.isPresent()) {
                return authenticatedUser.get();
            }
            // User does not exist or password is incorrect
            throw new BusinessException(ErrorType.INVALID_CREDENTIALS);
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            // If userDAO doesn't have authenticateUser method, use old logic
            System.out.println("Using old login logic...");
            return loginUserLegacy(username.trim(), password.trim());
        }

    } catch (ValidationException e) {
        // Fix 3: Ensure ValidationException is properly thrown
        throw e;
    } catch (BusinessException e) {
        throw e;
    } catch (Exception e) {
        throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
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
     */
   public User loginUserLegacy(String username, String password) {
        try {
            if (username == null || password == null) {
                throw new ValidationException("Username and password cannot be empty");
            }
            
            // Old version logic
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
                "Failed to login: " + e.getMessage(), e);
        }
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Integer id) {
        return userDAO.getUserById(id);
    }

    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Delete user
     */
    public boolean deleteUser(Integer userId) {
        return userDAO.deleteUser(userId);
    }

    /**
     * Update user
     */
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        return userDAO.updateUser(user);
    }
}
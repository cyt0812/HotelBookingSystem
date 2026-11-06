package com.hotelbooking.controller;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import com.hotelbooking.util.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class EditProfileController {
    
    @FXML  TextField usernameField;
    @FXML  TextField emailField;
    @FXML  TextField fullNameField;
    @FXML  Label errorLabel;
    @FXML  Label successLabel;
    
    private User currentUser;
    private UserDAO userDAO;
    private OnSaveCallback onSaveCallback;
    private String originalUsername; // 存储原始username
    private String originalEmail;    // 存储原始email
    
    // Email validation regex
    private static final String EMAIL_REGEX = 
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        loadUserData();
    }
    
    private void loadUserData() {
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser == null) {
            showError("No user logged in!");
            return;
        }
        
        originalUsername = currentUser.getUsername();
        originalEmail = currentUser.getEmail();
        
        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
        fullNameField.setText(currentUser.getFullName() != null ? currentUser.getFullName() : "");
        
        clearMessages();
        System.out.println("✅ User data loaded for editing: " + currentUser.getUsername());
    }
    
    @FXML
     void handleSave() {
        clearMessages();
        
        // Validation
        String validationError = validateInput();
        if (!validationError.isEmpty()) {
            showError(validationError);
            return;
        }
        
        try {
            String newUsername = usernameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newFullName = fullNameField.getText().trim();
            
            // ⭐ 检查新username是否已被其他用户使用（如果username被修改了）
            if (!newUsername.equals(originalUsername)) {
                if (userDAO.isUsernameExists(newUsername)) {
                    showError("❌ This username is already taken");
                    System.out.println("⚠ Username already exists: " + newUsername);
                    return;
                }
            }
            
            // ⭐ 检查新email是否已被其他用户使用（如果email被修改了）
            if (!newEmail.equals(originalEmail)) {
                if (userDAO.isEmailExists(newEmail)) {
                    showError("❌ This email is already registered");
                    System.out.println("⚠ Email already exists: " + newEmail);
                    return;
                }
            }
            
            // ⭐ 使用 updateUserProfile() 方法保存username和email
            boolean isUpdated = userDAO.updateUserProfile(currentUser.getId(), newUsername, newEmail);
            
            if (isUpdated) {
                // ⭐ 更新内存中的user对象
                currentUser.setUsername(newUsername);
                currentUser.setEmail(newEmail);
                currentUser.setFullName(newFullName);
                
                // ⭐ 更新Session中的用户信息
                SessionManager.setCurrentUser(currentUser);
                
                showSuccess("✅ Profile updated successfully!");
                System.out.println("💾 Profile saved - Username: " + newUsername + ", Email: " + newEmail + ", FullName: " + newFullName);
                
                // 调用回调函数
                if (onSaveCallback != null) {
                    onSaveCallback.onSave();
                }
                
                // 关闭窗口
                Platform.runLater(() -> {
                    try {
                        Thread.sleep(1500);
                        closeWindow();
                    } catch (InterruptedException e) {
                        closeWindow();
                    }
                });
            } else {
                showError("Failed to update profile in database");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error saving profile: " + e.getMessage());
            showError("Failed to save profile. Please try again.");
            e.printStackTrace();
        }
    }
    
    @FXML
     void handleCancel() {
        closeWindow();
    }
    
    /**
     * ⭐ 完整的输入验证
     */
    String validateInput() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        
        // Username validation
        if (username.isEmpty()) {
            return "Username cannot be empty";
        }
        
        if (username.length() < 3) {
            return "Username must be at least 3 characters";
        }
        
        if (username.length() > 50) {
            return "Username is too long (max 50 characters)";
        }
        
        // Username只允许字母、数字和下划线
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return "Username can only contain letters, numbers, and underscores";
        }
        
        // Email validation
        if (email.isEmpty()) {
            return "Email address cannot be empty";
        }
        
        if (email.length() > 100) {
            return "Email address is too long (max 100 characters)";
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Please enter a valid email address";
        }
        
        // Full Name validation
        if (fullName.isEmpty()) {
            return "Full name cannot be empty";
        }
        
        if (fullName.length() < 2) {
            return "Full name must be at least 2 characters";
        }
        
        if (fullName.length() > 100) {
            return "Full name is too long (max 100 characters)";
        }
        
        if (!fullName.matches("^[a-zA-Z\\s\\-']+$")) {
            return "Full name can only contain letters, spaces, hyphens, and apostrophes";
        }
        
        return "";
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 13px;");
        successLabel.setText("");
        System.out.println("⚠ Error: " + message);
    }
    
    private void showSuccess(String message) {
        successLabel.setText(message);
        successLabel.setStyle("-fx-text-fill: #28a745; -fx-font-size: 13px;");
        errorLabel.setText("");
        System.out.println("✅ Success: " + message);
    }
    
    private void clearMessages() {
        errorLabel.setText("");
        successLabel.setText("");
    }
    
    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
    
    public interface OnSaveCallback {
        void onSave();
    }
    
    public void setOnSaveCallback(OnSaveCallback callback) {
        this.onSaveCallback = callback;
        System.out.println("📍 Save callback set");
    }
}
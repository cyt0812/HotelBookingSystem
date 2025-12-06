/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.controller;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import com.hotelbooking.util.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class ChangePasswordController {
    
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ProgressBar passwordStrengthBar;
    @FXML private Label strengthLabel;
    @FXML private Label req1;
    @FXML private Label req2;
    @FXML private Label req3;
    @FXML private Label req4;
    @FXML private Label matchLabel;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;
    
    private User currentUser;
    private UserDAO userDAO;
    private static final int MIN_PASSWORD_LENGTH = 8;
    
    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser == null) {
            showError("No user logged in!");
            return;
        }
        
        // Add listeners for real-time validation
        newPasswordField.setOnKeyReleased(e -> validatePasswordRequirements());
        confirmPasswordField.setOnKeyReleased(e -> checkPasswordMatch());
        
        clearMessages();
        System.out.println("✅ Change password controller initialized for user: " + currentUser.getUsername());
    }
    
    @FXML
    private void handleChangePassword() {
        clearMessages();
        
        // Get input values
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        String validationError = validateInput(currentPassword, newPassword, confirmPassword);
        if (!validationError.isEmpty()) {
            showError(validationError);
            return;
        }
        
        try {
            // Verify current password
            if (!verifyCurrentPassword(currentPassword)) {
                showError("❌ Current password is incorrect");
                System.out.println("⚠ Failed password verification attempt");
                return;
            }
            
            // ⭐ 保存新密码到数据库
            boolean isUpdated = userDAO.updateUserPassword(currentUser.getId(), newPassword);
            
            if (isUpdated) {
                // Update password in memory
                currentUser.setPassword(newPassword);
                SessionManager.setCurrentUser(currentUser);
                
                showSuccess("✅ Password changed successfully!");
                System.out.println("💾 Password changed for user: " + currentUser.getUsername());
                
                // Clear fields
                currentPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
                
                // Close window after delay
                Platform.runLater(() -> {
                    try {
                        Thread.sleep(2000);
                        closeWindow();
                    } catch (InterruptedException e) {
                        closeWindow();
                    }
                });
            } else {
                showError("Failed to update password in database");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error changing password: " + e.getMessage());
            showError("Failed to change password. Please try again.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCancel() {
        closeWindow();
    }
    
    /**
     * Validate all input fields
     */
    private String validateInput(String currentPassword, String newPassword, String confirmPassword) {
        // Check if fields are empty
        if (currentPassword.isEmpty()) {
            return "Current password cannot be empty";
        }
        
        if (newPassword.isEmpty()) {
            return "New password cannot be empty";
        }
        
        if (confirmPassword.isEmpty()) {
            return "Please confirm your new password";
        }
        
        // Check password length
        if (newPassword.length() < MIN_PASSWORD_LENGTH) {
            return "New password must be at least " + MIN_PASSWORD_LENGTH + " characters";
        }
        
        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            return "New passwords do not match";
        }
        
        // Check if new password is same as current
        if (newPassword.equals(currentPassword)) {
            return "New password must be different from current password";
        }
        
        // Check password strength requirements
        if (!isPasswordStrong(newPassword)) {
            return "Password does not meet strength requirements";
        }
        
        return "";
    }
    
    /**
     * Verify current password against stored password
     */
    private boolean verifyCurrentPassword(String inputPassword) {
        String storedPassword = currentUser.getPassword();
        
        // In production, you would use password hashing and comparison
        // For now, simple comparison (⚠ not recommended for production)
        // Example with BCrypt: return BCrypt.checkpw(inputPassword, storedPassword);
        
        return inputPassword.equals(storedPassword);
    }
    
    /**
     * Validate password strength requirements
     */
    private boolean isPasswordStrong(String password) {
        boolean hasLength = password.length() >= MIN_PASSWORD_LENGTH;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        
        return hasLength && hasUpper && hasLower && hasDigit;
    }
    
    /**
     * Real-time password requirement validation
     */
    private void validatePasswordRequirements() {
        String password = newPasswordField.getText();
        
        boolean hasLength = password.length() >= MIN_PASSWORD_LENGTH;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        
        // Update requirement labels
        updateRequirement(req1, hasLength, "✓ At least 8 characters", "✗ At least 8 characters");
        updateRequirement(req2, hasUpper, "✓ Contains uppercase letter (A-Z)", "✗ Contains uppercase letter (A-Z)");
        updateRequirement(req3, hasLower, "✓ Contains lowercase letter (a-z)", "✗ Contains lowercase letter (a-z)");
        updateRequirement(req4, hasDigit, "✓ Contains number (0-9)", "✗ Contains number (0-9)");
        
        // Update strength bar and label
        updatePasswordStrength(hasLength, hasUpper, hasLower, hasDigit);
    }
    
    /**
     * Update individual requirement label
     */
    private void updateRequirement(Label label, boolean met, String metText, String unmetText) {
        if (met) {
            label.setText(metText);
            label.setStyle("-fx-text-fill: #28a745;");
        } else {
            label.setText(unmetText);
            label.setStyle("-fx-text-fill: #dc3545;");
        }
    }
    
    /**
     * Update password strength bar and label
     */
    private void updatePasswordStrength(boolean hasLength, boolean hasUpper, boolean hasLower, boolean hasDigit) {
        int strength = 0;
        if (hasLength) strength++;
        if (hasUpper) strength++;
        if (hasLower) strength++;
        if (hasDigit) strength++;
        
        double progress = strength / 4.0;
        passwordStrengthBar.setProgress(progress);
        
        String strengthText;
        String colorStyle;
        
        switch (strength) {
            case 0:
                strengthText = "Strength: None";
                colorStyle = "-fx-text-fill: #999;";
                passwordStrengthBar.setStyle("-fx-control-inner-background: #e9ecef;");
                break;
            case 1:
                strengthText = "Strength: Weak";
                colorStyle = "-fx-text-fill: #dc3545;";
                passwordStrengthBar.setStyle("-fx-control-inner-background: #dc3545;");
                break;
            case 2:
                strengthText = "Strength: Fair";
                colorStyle = "-fx-text-fill: #ffc107;";
                passwordStrengthBar.setStyle("-fx-control-inner-background: #ffc107;");
                break;
            case 3:
                strengthText = "Strength: Good";
                colorStyle = "-fx-text-fill: #17a2b8;";
                passwordStrengthBar.setStyle("-fx-control-inner-background: #17a2b8;");
                break;
            default:
                strengthText = "Strength: Strong";
                colorStyle = "-fx-text-fill: #28a745;";
                passwordStrengthBar.setStyle("-fx-control-inner-background: #28a745;");
        }
        
        strengthLabel.setText(strengthText);
        strengthLabel.setStyle(colorStyle + " -fx-font-size: 11px; -fx-font-weight: bold;");
    }
    
    /**
     * Check if new password and confirm password match
     */
    private void checkPasswordMatch() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            matchLabel.setText("");
            return;
        }
        
        if (newPassword.equals(confirmPassword)) {
            matchLabel.setText("✓ Passwords match");
            matchLabel.setStyle("-fx-text-fill: #28a745; -fx-font-size: 11px;");
        } else {
            matchLabel.setText("✗ Passwords do not match");
            matchLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 11px;");
        }
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 13px;");
        successLabel.setText("");
        System.out.println("⚠ Error: " + message);
    }
    
    /**
     * Show success message
     */
    private void showSuccess(String message) {
        successLabel.setText(message);
        successLabel.setStyle("-fx-text-fill: #28a745; -fx-font-size: 13px;");
        errorLabel.setText("");
        System.out.println("✅ Success: " + message);
    }
    
    /**
     * Clear all message labels
     */
    private void clearMessages() {
        errorLabel.setText("");
        successLabel.setText("");
        matchLabel.setText("");
    }
    
    /**
     * Close the window
     */
    private void closeWindow() {
        Stage stage = (Stage) currentPasswordField.getScene().getWindow();
        stage.close();
    }
}

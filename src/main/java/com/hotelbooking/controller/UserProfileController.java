package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserProfileController {
    
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label fullNameLabel;
    @FXML private Label memberSinceLabel;
    @FXML private Label roleLabel;
    
    private User currentUser;
    
    @FXML
    public void initialize() {
        loadUserProfile();
    }
    
    private void loadUserProfile() {
        currentUser = SessionManager.getCurrentUser();
        
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Error", "No user logged in!");
            return;
        }
        
        usernameLabel.setText(currentUser.getUsername());
        emailLabel.setText(currentUser.getEmail());
        fullNameLabel.setText(
            currentUser.getFullName() != null && !currentUser.getFullName().isEmpty() 
            ? currentUser.getFullName() 
            : "Not set"
        );
        roleLabel.setText(capitalizeRole(currentUser.getRole()));
        memberSinceLabel.setText(formatDate(currentUser.getCreatedAt()));
        
        System.out.println("✅ User profile loaded: " + currentUser.getUsername());
    }
    
    
    
    
    @FXML
    private void handleEditProfile() {
        try {
            // 在任何导航前调用
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/edit_profile.fxml",
                "Edit Profile"
            );
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/edit_profile.fxml")
            );
            Parent root = loader.load();
            EditProfileController controller = loader.getController();
            
            controller.setOnSaveCallback(this::refreshProfile);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            System.out.println("➡ Edit profile window opened");
        } catch (Exception e) {
            System.err.println("❌ Failed to open edit profile window");
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open edit profile window");
        }
    }
    
    @FXML
    private void handleChangePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/change_password.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Change Password");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            System.out.println("➡ Change password window opened");
        } catch (Exception e) {
            System.err.println("❌ Failed to open change password window");
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open change password window");
        }
    }
    
    @FXML
    private void handleLogout() {
        try {
            SessionManager.logout();
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/login.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Booking System - Login");
            
            System.out.println("🚪 User logged out successfully");
        } catch (Exception e) {
            System.err.println("❌ Failed to logout");
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to logout");
        }
    }
    
    @FXML
    private void backToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/main_dashboard.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Booking System");
            
            System.out.println("🏠 Back to home");
        } catch (Exception e) {
            System.err.println("❌ Failed to navigate to home");
            e.printStackTrace();
        }
    }
    
    private void refreshProfile() {
        currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            loadUserProfile();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
            System.out.println("🔄 Profile refreshed");
        }
    }
    
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "Unknown";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        return dateTime.format(formatter);
    }
    
    private String capitalizeRole(String role) {
        if (role == null || role.isEmpty()) return "User";
        return role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
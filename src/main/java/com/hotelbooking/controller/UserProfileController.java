package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
<<<<<<< HEAD
import com.hotelbooking.util.NavigationManager;
=======
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
<<<<<<< HEAD
=======
import javafx.scene.image.ImageView;
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
<<<<<<< HEAD
import java.util.Locale;
=======
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab

public class UserProfileController {
    
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label fullNameLabel;
    @FXML private Label memberSinceLabel;
    @FXML private Label roleLabel;
<<<<<<< HEAD
    
    private User currentUser;
=======
    @FXML private ImageView profileImage;
    
    private User currentUser;
    private String avatarPath;
    private static final String DEFAULT_AVATAR = "file:src/main/resources/com/hotelbooking/images/default_avatar.png";
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    
    @FXML
    public void initialize() {
        loadUserProfile();
<<<<<<< HEAD
=======
        loadProfileImage();
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
    
<<<<<<< HEAD
    
    
=======
    private void loadProfileImage() {
        try {
            if (avatarPath != null && !avatarPath.isEmpty()) {
                profileImage.setImage(new Image(new File(avatarPath).toURI().toString()));
            } else {
                profileImage.setImage(new Image(DEFAULT_AVATAR));
            }
        } catch (Exception e) {
            System.out.println("⚠ Could not load profile image, using default");
            try {
                profileImage.setImage(new Image(DEFAULT_AVATAR));
            } catch (Exception ex) {
                System.err.println("❌ Failed to load default avatar");
            }
        }
    }
    
    @FXML
    private void handleChangeAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        Stage stage = (Stage) profileImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            avatarPath = selectedFile.getAbsolutePath();
            loadProfileImage();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile image updated!");
            System.out.println("🖼 Avatar path: " + avatarPath);
        }
    }
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    
    @FXML
    private void handleEditProfile() {
        try {
<<<<<<< HEAD
            // 在任何导航前调用
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/edit_profile.fxml",
                "Edit Profile"
            );
            
=======
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
<<<<<<< HEAD
                getClass().getResource("/com/hotelbooking/view/main_dashboard.fxml")
=======
                getClass().getResource("/com/hotelbooking/view/home.fxml")
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
<<<<<<< HEAD
            stage.setTitle("Hotel Booking System");
=======
            stage.setTitle("Hotel Booking System - Home");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            
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
<<<<<<< HEAD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
=======
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.hotelbooking.controller;
//
///**
// *
// * @author a1-6
// */
//import com.hotelbooking.entity.User;
//import com.hotelbooking.util.SessionManager;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.stage.FileChooser;
//
//import java.io.File;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//public class UserProfileController {
//
//    @FXML private Label lblUsername;
//    @FXML private Label lblEmail;
//    @FXML private Label lblFullName;
//
//    private User currentUser;
//
//    @FXML
//    public void initialize() {
//        // 从 SessionManager 获取当前登录用户
//        currentUser = SessionManager.getCurrentUser();
//
//        if (currentUser == null) {
//            System.out.println("No user logged in!");
//            return;
//        }
//
//        lblUsername.setText(currentUser.getUsername());
//        lblEmail.setText(currentUser.getEmail());
//        lblFullName.setText(currentUser.getFullName());
//
//    }
//
//    @FXML
//    private void handleEditProfile() {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                getClass().getResource("/com/hotelbooking/view/edit_profile.fxml")
//            );
//            Parent root = loader.load();
//
//            EditProfileController controller = loader.getController();
//            // 设置回调：保存后刷新页面
//            controller.setOnSaveCallback(this::refreshProfile);
//
//            Stage stage = new Stage();
//            stage.setTitle("Edit Profile");
//            stage.setScene(new Scene(root));
//            stage.show();
//
//            System.out.println("➡ 打开编辑资料窗口");
//
//        } catch (Exception e) {
//            System.err.println("❌ 无法打开编辑资料窗口");
//            e.printStackTrace();
//        }
//    }
//
//
//
//    private void refreshProfile() {
//    currentUser = SessionManager.getCurrentUser();
//    if (currentUser != null) {
//        lblUsername.setText(currentUser.getUsername());
//        lblEmail.setText(currentUser.getEmail());
//        lblFullName.setText(currentUser.getFullName()); // 如果 lblMemberSince 要显示 fullName
//    }
//        System.out.println("🔄 用户资料已刷新");
//    }
//}
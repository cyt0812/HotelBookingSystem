package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.service.UserService;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {
    
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private CheckBox termsCheckbox;
    @FXML private Label errorLabel;
    
    // Assume you have a UserDAO class, instantiate and pass it to UserService
    UserDAO userDAO = new UserDAO();  // Create UserDAO instance
    private UserService userService = new UserService(userDAO);
    
    @FXML
    public void initialize() {
        System.out.println("✅ Register page initialized");
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }
    
    /**
     * Handle registration
     */
    @FXML
    private void handleRegister() {
        System.out.println("🔘 Register button clicked");

        // Get user input
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic validation (you already have this)
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("All fields must be filled");
            return;
        }
        if (!email.contains("@")) {
            showError("Please enter a valid email address");
            return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }
        if (!termsCheckbox.isSelected()) {
            showError("Please agree to the terms and conditions");
            return;
        }

        try {
            // ⭐ Call service to register user (no longer using Optional)
            User registeredUser = userService.registerUser(username, email, password, "CUSTOMER");

            // Auto-login
            SessionManager.login(registeredUser);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome! You will be redirected to the main page.");
            alert.showAndWait();

            backToHome();
        } catch (Exception e) {
            showError("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Go to the login page
     */
    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/login.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Go back to the home page
     */
    @FXML
    private void backToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/main_dashboard.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Booking System");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Show error messages
     */
    void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        }
    }
}
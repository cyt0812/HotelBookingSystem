package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
import com.hotelbooking.service.UserService;
import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField confirmPasswordField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    UserDAO userDAO = new UserDAO();
    private UserService userService = new UserService(userDAO);

    @FXML
    public void initialize() {
        System.out.println("✅ Login page initialized");

        // Clear error message
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }

    /**
     * Handle login
     */
    @FXML
    private void handleLogin() {
        System.out.println("🔘 Login button clicked");

        // Input validation
        if (usernameField == null || passwordField == null) {
            showError("UI initialization failed");
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty");
            return;
        }

        try {
            // Call UserService to login (no longer using Optional)
            User user = userService.loginUser(username, password); // Returns User or null

            if (user != null) {
                // Login successful
                SessionManager.login(user);
                navigateToMainDashboard();
            } else {
                // This case shouldn't be hit since it's already handled by the loginUser method
                // User does not exist or password is incorrect
                showError("Incorrect username or password");
            }
        } catch (ValidationException e) {
            // Catch validation exception from frontend
            showError(e.getMessage());  // Show invalid input message
        } catch (BusinessException e) {
            // Catch business exception
            if (e.getErrorType() == ErrorType.INVALID_CREDENTIALS) {
                showError("Incorrect username or password");  // Update error message here
            } else {
                showError("Login failed, please try again");
            }
        } catch (Exception e) {
            showError("Login failed, please try again");
            e.printStackTrace();  // Print stack trace for debugging
        }
    }

    /**
     * Navigate to the register page
     */
    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/register.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return to the home page
     */
    @FXML
    private void backToHome() {
        navigateToMainDashboard();
    }

    /**
     * Navigate to the main dashboard
     */
    private void navigateToMainDashboard() {
        try {
            // Call before any navigation
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/main_dashboard.fxml",
                "Hotel Booking System"
            );
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/main_dashboard.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Booking System");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            System.out.println("Error displayed: " + message);  // Debug log
        } else {
            System.out.println("Error label is null!");  // Debug: check if errorLabel is correctly bound
        }
    }
}
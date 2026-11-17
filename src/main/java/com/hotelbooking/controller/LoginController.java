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

    private UserService userService;

    // 生产代码用原来的无参构造
    public LoginController() {
        this.userService = new UserService(new UserDAO());
    }

    // 测试用的包可见构造器（把假 Service 塞进来）
    LoginController(UserService userService) {
        this.userService = userService;
    }

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

        if (usernameField == null || passwordField == null) {
            showError("UI initialization failed");
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // 调用抽出来的纯逻辑方法
        boolean success = performLogin(username, password);

        if (success) {
            navigateToMainDashboard();
        }
    }

    // 这是你抽出来的可测试方法，不依赖 JavaFX UI
    boolean performLogin(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            showError("Username and password cannot be empty");
            return false;
        }

        try {
            User user = userService.loginUser(username, password);

            if (user != null) {
                SessionManager.login(user); // 生产时正常登录
                return true;
            } else {
                showError("Incorrect username or password");
                return false;
            }
        } catch (ValidationException e) {
            showError(e.getMessage());
            return false;
        } catch (BusinessException e) {
            if (e.getErrorType() == ErrorType.INVALID_CREDENTIALS) {
                showError("Incorrect username or password");
            } else {
                showError("Login failed, please try again");
            }
            return false;
        } catch (Exception e) {
            showError("Login failed, please try again");
            e.printStackTrace();
            return false;
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
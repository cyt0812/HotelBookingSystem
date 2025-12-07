package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.dao.UserDAO;
<<<<<<< HEAD
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
import com.hotelbooking.service.UserService;
import com.hotelbooking.util.NavigationManager;
=======
import com.hotelbooking.service.UserService;
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
<<<<<<< HEAD

public class LoginController {

=======
import java.util.Optional;

public class LoginController {
    
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField confirmPasswordField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
<<<<<<< HEAD

    UserDAO userDAO = new UserDAO();
    private UserService userService = new UserService(userDAO);

    @FXML
    public void initialize() {
        System.out.println("✅ Login page initialized");

        // Clear error message
=======
    
    UserDAO userDAO = new UserDAO();
    private UserService userService = new UserService(userDAO);
    
    @FXML
    public void initialize() {
        System.out.println("✅ 登录页面初始化");
        
        // 清空错误提示
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }
<<<<<<< HEAD

    /**
     * Handle login
     */
    @FXML
    private void handleLogin() {
        System.out.println("🔘 Login button clicked");

        // Input validation
        if (usernameField == null || passwordField == null) {
            showError("UI initialization failed");
=======
    
    /**
     * 处理登录
     */
    
    @FXML
    private void handleLogin() {
        System.out.println("🔘 登录按钮被点击");

        // 验证输入
        if (usernameField == null || passwordField == null) {
            showError("界面初始化失败");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
<<<<<<< HEAD
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
=======
            showError("用户名和密码不能为空");
            return;
        }

        // 调用 UserService 登录（不再使用 Optional）
        User user = userService.loginUser(username, password); // 注意这里返回 User 或 null

        if (user != null) {
            // 登录成功
            SessionManager.login(user);
            navigateToMainDashboard();
        } else {
            // 用户不存在或密码错误
            showError("用户名或密码错误");
        }
    }
//    @FXML
//    private void handleLogin() {
//        System.out.println("🔘 登录按钮被点击");
//        
//        // 验证输入
//        if (usernameField == null || passwordField == null) {
//            showError("界面初始化失败");
//            return;
//        }
//        
//        String username = usernameField.getText().trim();
//        String password = passwordField.getText();
//        
//        if (username.isEmpty() || password.isEmpty()) {
//            showError("用户名和密码不能为空");
//            return;
//        }
//        
//        // 调用 UserService 登录
//        Optional<User> user = userService.loginUser(username, password);
//
//        
//        if (user.isPresent()) {
//            // 登录成功
//            SessionManager.login(user.get());
//            navigateToMainDashboard();
//        } else {
//            // 用户不存在或密码错误
//            showError("用户名或密码错误");
//        }
//    }
    
    /**
     * 跳转到注册页面
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
<<<<<<< HEAD

    /**
     * Return to the home page
=======
    
    /**
     * 返回主页
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    @FXML
    private void backToHome() {
        navigateToMainDashboard();
    }
<<<<<<< HEAD

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
=======
    
    /**
     * 跳转到主界面
     */
    private void navigateToMainDashboard() {
        try {
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
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
<<<<<<< HEAD

    /**
     * Show error message
=======
    
    /**
     * 显示错误信息
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
     */
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
<<<<<<< HEAD
            System.out.println("Error displayed: " + message);  // Debug log
        } else {
            System.out.println("Error label is null!");  // Debug: check if errorLabel is correctly bound
=======
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        }
    }
}
package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.service.UserService;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    
    private UserService userService = new UserService();
    
    @FXML
    public void initialize() {
        System.out.println("✅ 登录页面初始化");
        
        // 清空错误提示
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }
    
    /**
     * 处理登录
     */
    @FXML
    private void handleLogin() {
        System.out.println("🔘 登录按钮被点击");
        
        // 验证输入
        if (usernameField == null || passwordField == null) {
            showError("界面初始化失败");
            return;
        }
        
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("用户名和密码不能为空");
            return;
        }
        
        // 调用 UserService 登录
        User user = userService.loginUser(username, password);
        
        if (user != null) {
            // 登录成功
            SessionManager.login(user);
            navigateToMainDashboard();
        } else {
            showError("用户名或密码错误");
        }
    }
    
    /**
     * 跳转到注册页面
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
     * 返回主页
     */
    @FXML
    private void backToHome() {
        navigateToMainDashboard();
    }
    
    /**
     * 跳转到主界面
     */
    private void navigateToMainDashboard() {
        try {
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
     * 显示错误信息
     */
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        }
    }
}
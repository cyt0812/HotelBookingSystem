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
import java.util.Optional;

public class RegisterController {
    
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private CheckBox termsCheckbox;
    @FXML private Label errorLabel;
    
    // 假设你有一个 UserDAO 类，实例化并传递给 UserService
    UserDAO userDAO = new UserDAO();  // 创建 UserDAO 实例
    private UserService userService = new UserService(userDAO);
    
    @FXML
    public void initialize() {
        System.out.println("✅ 注册页面初始化");
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }
    
    /**
     * 处理注册
     */
    @FXML
    private void handleRegister() {
        System.out.println("🔘 注册按钮被点击");

        // 获取输入
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 基础验证（你已有）
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("所有字段都必须填写");
            return;
        }
        if (!email.contains("@")) {
            showError("请输入有效的邮箱地址");
            return;
        }
        if (password.length() < 6) {
            showError("密码长度至少6位");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("两次输入的密码不一致");
            return;
        }
        if (!termsCheckbox.isSelected()) {
            showError("请同意服务条款");
            return;
        }

        try {
            // ⭐ 调用不使用 Optional 的服务
            User registeredUser = userService.registerUser(username, email, password, "CUSTOMER");

            // 自动登录
            SessionManager.login(registeredUser);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("注册成功");
            alert.setHeaderText(null);
            alert.setContentText("欢迎加入！即将跳转到主页面");
            alert.showAndWait();

            backToHome();
        } catch (Exception e) {
            showError("注册失败: " + e.getMessage());
        }
    }
//    @FXML
//    private void handleRegister() {
//        System.out.println("🔘 注册按钮被点击");
//        
//        // 获取输入
//        String username = usernameField.getText().trim();
//        String email = emailField.getText().trim();
//        String password = passwordField.getText();
//        String confirmPassword = confirmPasswordField.getText();
//        
//        // 验证输入
//        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
//            showError("所有字段都必须填写");
//            return;
//        }
//        
//        if (!email.contains("@")) {
//            showError("请输入有效的邮箱地址");
//            return;
//        }
//        
//        if (password.length() < 6) {
//            showError("密码长度至少6位");
//            return;
//        }
//        
//        if (!password.equals(confirmPassword)) {
//            showError("两次输入的密码不一致");
//            return;
//        }
//        
//        if (!termsCheckbox.isSelected()) {
//            showError("请同意服务条款");
//            return;
//        }
//        
//        // 创建用户
//        User newUser = new User(username, email, password);
//
//        // 调用 UserService 注册
//        Optional<User> registeredUser = userService.registerUser(username, email, password, "CUSTOMER");
//
//        if (registeredUser.isPresent()) {
//            // 注册成功，自动登录
//            SessionManager.login(registeredUser.get());
//
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("注册成功");
//            alert.setHeaderText(null);
//            alert.setContentText("欢迎加入！即将跳转到主页面");
//            alert.showAndWait();
//
//            backToHome();
//        } else {
//            showError("注册失败，用户名可能已存在");
//        }
//    }
    
    /**
     * 跳转到登录页面
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
     * 返回主页
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
     * 显示错误信息
     */
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        }
    }
}
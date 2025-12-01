/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.controller;

/**
 *
 * @author a1-6
 */
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TopNavigationController {
    
    @FXML private Button btnHelp;
    @FXML private Button btnTrips;
    @FXML private Button btnLogin;
    
    @FXML
    public void initialize() {
        setupHoverEffects();
        updateLoginButton();
    }
    
    /**
     * 设置鼠标悬停效果
     */
    private void setupHoverEffects() {
        // Help 按钮悬停效果
        btnHelp.setOnMouseEntered(e -> {
            btnHelp.setStyle(
                "-fx-background-color: #f5f5f5; " +
                "-fx-text-fill: #333333; " +
                "-fx-font-size: 14px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 15; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;"
            );
        });
        
        btnHelp.setOnMouseExited(e -> {
            btnHelp.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #333333; " +
                "-fx-font-size: 14px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 15; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;"
            );
        });
        
        // Trips 按钮悬停效果
        btnTrips.setOnMouseEntered(e -> {
            btnTrips.setStyle(
                "-fx-background-color: #f5f5f5; " +
                "-fx-text-fill: #333333; " +
                "-fx-font-size: 14px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 15; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;"
            );
        });
        
        btnTrips.setOnMouseExited(e -> {
            btnTrips.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #333333; " +
                "-fx-font-size: 14px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 15; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;"
            );
        });
        
        // Login 按钮悬停效果（变色加深）
        btnLogin.setOnMouseEntered(e -> {
            btnLogin.setStyle(
                "-fx-background-color: #8B4513; " +  // 深棕色（类似Marriott风格）
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 10 25; " +
                "-fx-border-radius: 20; " +
                "-fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"
            );
        });
        
        btnLogin.setOnMouseExited(e -> {
            btnLogin.setStyle(
                "-fx-background-color: #1a1a1a; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 10 25; " +
                "-fx-border-radius: 20; " +
                "-fx-background-radius: 20;"
            );
        });
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
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 更新登录按钮状态（如果已登录显示用户名）
     */
    private void updateLoginButton() {
        if (SessionManager.isLoggedIn()) {
            String username = SessionManager.getLoggedInUsername();
            btnLogin.setText("👤 " + username);
        } else {
            btnLogin.setText("👤 Sign In");
        }
    }
    
    /**
     * 处理 Help 按钮点击
     */
    @FXML
    private void handleHelp() {
        System.out.println("🔘 Help 按钮被点击");
        // 可以打开帮助对话框或跳转到帮助页面
        showHelpDialog();
    }
    
    /**
     * 处理 Trips 按钮点击
     */
    @FXML
    private void handleTrips() {
        System.out.println("🔘 Trips 按钮被点击");
        
        if (!SessionManager.isLoggedIn()) {
            System.out.println("⚠️ 用户未登录，跳转到登录页面");
            handleLogin();
            return;
        }
        
        // 跳转到我的订单页面
        navigateToTrips();
    }
    
    /**
     * 处理 Login 按钮点击
     */
    @FXML
    private void handleLogin() {
        System.out.println("🔘 Login 按钮被点击");
        
        if (SessionManager.isLoggedIn()) {
            // 如果已登录，显示用户菜单
            showUserMenu();
        } else {
            // 如果未登录，跳转到登录页面
            navigateToLogin();
        }
    }
    
    /**
     * 显示帮助对话框
     */
    private void showHelpDialog() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION
        );
        alert.setTitle("帮助中心");
        alert.setHeaderText("需要帮助吗？");
        alert.setContentText(
            "常见问题：\n\n" +
            "1. 如何预订房间？\n" +
            "   - 选择日期和目的地，浏览可用房间并完成预订\n\n" +
            "2. 如何查看我的订单？\n" +
            "   - 点击 'My Trips' 按钮查看所有预订\n\n" +
            "3. 如何联系客服？\n" +
            "   - 拨打热线: 400-888-8888\n" +
            "   - 邮箱: support@hotel.com"
        );
        alert.showAndWait();
    }
    
    /**
     * 跳转到我的行程页面
     */
    private void navigateToTrips() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/my_trips.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnTrips.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("我的行程");
            
        } catch (Exception e) {
            System.err.println("❌ 跳转失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 跳转到登录页面
     */
    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/login.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("用户登录");
            
        } catch (Exception e) {
            System.err.println("❌ 跳转失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 显示用户菜单（已登录状态）
     */
    private void showUserMenu() {
        javafx.scene.control.ContextMenu contextMenu = new javafx.scene.control.ContextMenu();
        
        javafx.scene.control.MenuItem profileItem = new javafx.scene.control.MenuItem("👤 我的资料");
        profileItem.setOnAction(e -> navigateToProfile());
        
        javafx.scene.control.MenuItem tripsItem = new javafx.scene.control.MenuItem("🧳 我的行程");
        tripsItem.setOnAction(e -> navigateToTrips());
        
        javafx.scene.control.MenuItem logoutItem = new javafx.scene.control.MenuItem("🚪 退出登录");
        logoutItem.setOnAction(e -> handleLogout());
        
        contextMenu.getItems().addAll(profileItem, tripsItem, logoutItem);
        contextMenu.show(btnLogin, javafx.geometry.Side.BOTTOM, 0, 5);
    }
    
    /**
     * 跳转到用户资料页面
     */
    private void navigateToProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/user_profile.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("用户资料");
            
        } catch (Exception e) {
            System.err.println("❌ 跳转失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理退出登录
     */
    private void handleLogout() {
        SessionManager.logout();
        System.out.println("✅ 用户已退出登录");
        updateLoginButton();
        
        // 可以选择跳转回主页
        navigateToLogin();
    }
}

package com.hotelbooking.controller;

import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TopNavigationController {
    
    @FXML private Button btnHelp;
    @FXML private Button btnTrips;
    @FXML private Button btnLogin;
    @FXML private Button btnBack;  // 返回按钮
    
    @FXML
    public void initialize() {
        setupHoverEffects();
        updateLoginButton();
        updateBackButton();
    }
    
    /**
     * 更新返回按钮状态
     */
    private void updateBackButton() {
        if (btnBack != null) {
            // 检查是否可以返回
            boolean canGoBack = NavigationManager.getInstance().hasPrevious();
            btnBack.setDisable(!canGoBack);
            btnBack.setStyle(
                "-fx-background-color: " + (canGoBack ? "#f5f5f5" : "#e0e0e0") + "; " +
                "-fx-text-fill: " + (canGoBack ? "#333333" : "#999999") + "; " +
                "-fx-font-size: 14px; " +
                "-fx-cursor: " + (canGoBack ? "hand" : "default") + "; " +
                "-fx-padding: 8 15; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;"
            );
        }
    }
    
    /**
     * 设置鼠标悬停效果
     */
    private void setupHoverEffects() {
        // 返回按钮悬停效果
        if (btnBack != null) {
            btnBack.setOnMouseEntered(e -> {
                if (!btnBack.isDisabled()) {
                    btnBack.setStyle(
                        "-fx-background-color: #ddd; " +
                        "-fx-text-fill: #1a1a1a; " +
                        "-fx-font-size: 14px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 8 15; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
                    );
                }
            });
            
            btnBack.setOnMouseExited(e -> {
                if (!btnBack.isDisabled()) {
                    btnBack.setStyle(
                        "-fx-background-color: #f5f5f5; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-font-size: 14px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 8 15; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
                    );
                }
            });
        }
        
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
        
        // Login 按钮悬停效果
        btnLogin.setOnMouseEntered(e -> {
            btnLogin.setStyle(
                "-fx-background-color: #8B4513; " +
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
     * 返回上一个界面
     */
    @FXML
    private void goBack() {
        System.out.println("⬅️ 返回上一页");
        NavigationManager navManager = NavigationManager.getInstance();
        NavigationManager.NavigationHistory previous = navManager.getPrevious();
        
        if (previous != null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(previous.fxmlPath)
                );
                Parent root = loader.load();
                
                Stage stage = (Stage) btnBack.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle(previous.title);
                
                // 导航完成后，弹出当前页面
                navManager.popCurrent();
                updateBackButton();
                
            } catch (Exception e) {
                System.err.println("❌ 返回失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ 没有上一页");
        }
    }
    
    /**
     * 返回主页
     */
    @FXML
    private void backToHome() {
        System.out.println("🏠 返回主页");
        try {
            NavigationManager.getInstance().goHome(
                "/com/hotelbooking/view/main_dashboard.fxml",
                "Hotel Booking System"
            );
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/main_dashboard.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Booking System");
            
        } catch (Exception e) {
            System.err.println("❌ 返回主页失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 更新登录按钮状态
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
        
        navigateToTrips();
    }
    
    /**
     * 处理 Login 按钮点击
     */
    @FXML
    private void handleLogin() {
        System.out.println("🔘 Login 按钮被点击");
        
        if (SessionManager.isLoggedIn()) {
            showUserMenu();
        } else {
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
        alert.setTitle("Help Center");
        alert.setHeaderText("Need help?");
        alert.setContentText(
            "Frequently Asked Questions:\n\n" +
            "1. How to book a room?\n   Select the dates and destination, then browse the available rooms.\n\n" +
            "2. How to view my bookings?\n   Click the 'My Trips' button.\n\n" +
            "3. Contact customer service: 400-888-8888"
        );
        alert.showAndWait();
    }
    
    /**
     * 跳转到我的行程页面
     */
    private void navigateToTrips() {
        try {
            // 记录当前页面到导航栈
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/my_bookings.fxml",
                "My Trips"
            );
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/my_bookings.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnTrips.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Trips");
            
            updateBackButton();
            
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
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/login.fxml",
                "User Login"
            );
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/login.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Login");
            
            updateBackButton();
            
        } catch (Exception e) {
            System.err.println("❌ 跳转失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 显示用户菜单
     */
    private void showUserMenu() {
        javafx.scene.control.ContextMenu contextMenu = new javafx.scene.control.ContextMenu();
        
        javafx.scene.control.MenuItem profileItem = new javafx.scene.control.MenuItem("👤 My Profile");
        profileItem.setOnAction(e -> navigateToProfile());
      
        
        javafx.scene.control.MenuItem logoutItem = new javafx.scene.control.MenuItem("🚪 Logout");
        logoutItem.setOnAction(e -> handleLogout());
        
        contextMenu.getItems().addAll(profileItem, logoutItem);
        contextMenu.show(btnLogin, javafx.geometry.Side.BOTTOM, 0, 5);
    }
    
    /**
     * 跳转到用户资料页面
     */
    private void navigateToProfile() {
        try {
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/user_profile.fxml",
                "用户资料"
            );
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/user_profile.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("用户资料");
            
            updateBackButton();
            
        } catch (Exception e) {
            System.err.println("❌ 跳转失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 处理退出登录
     */
    private void handleLogout() {
        SessionManager.logout();
        System.out.println("✅ 用户已退出登录");
        updateLoginButton();
        navigateToLogin();
    }
}
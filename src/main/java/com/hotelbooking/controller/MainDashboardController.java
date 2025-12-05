package com.hotelbooking.controller;

import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainDashboardController {
    
    // 顶部导航栏
    @FXML private Label lblWelcome;
    @FXML private Button btnHelp;
    @FXML private Button btnTrips;
    @FXML private Button btnLogin;
    
    // 搜索表单
    @FXML private TextField txtDestination;
    @FXML private DatePicker checkInDate;
    @FXML private DatePicker checkOutDate;
    
    // Rooms & Guests Selector
    @FXML private Button btnRoomsGuests;
    @FXML private Label lblRoomsGuestsDisplay;
    @FXML private Label lblArrow;
    @FXML private VBox selectorPanel;
    
    @FXML private Button btnRoomMinus;
    @FXML private Button btnRoomPlus;
    @FXML private Label lblRoomCount;
    
    @FXML private Button btnAdultMinus;
    @FXML private Button btnAdultPlus;
    @FXML private Label lblAdultCount;
    
    @FXML private Button btnChildMinus;
    @FXML private Button btnChildPlus;
    @FXML private Label lblChildCount;
    
    @FXML private VBox childrenAgeContainer;
    
    // 数据存储
    private int roomCount = 1;
    private int adultCount = 1;
    private int childCount = 0;
    private List<Integer> childrenAges = new ArrayList<>();
    
    // 常量
    private static final int MAX_ROOMS = 9;
    private static final int MIN_ROOMS = 1;
    private static final int MAX_GUESTS_PER_ROOM = 8;
    private static final int MIN_ADULTS = 1;
    
    @FXML
    public void initialize() {
        System.out.println("✅ 主界面初始化成功");
        
        // 初始化儿童年龄
        for (int i = 0; i < childCount; i++) {
            childrenAges.add(0);
        }
        
        // 初始化日期选择器（默认值）
        if (checkInDate != null) {
            checkInDate.setValue(java.time.LocalDate.now().plusDays(1));
        }
        if (checkOutDate != null) {
            checkOutDate.setValue(java.time.LocalDate.now().plusDays(2));
        }
        
        setupHoverEffects();
        updateWelcomeMessage();
        updateLoginButton();
        updateRoomsGuestsDisplay();
        updateChildrenAgeSelectors();
        updateButtons();
    }
    
    /**
     * 处理搜索酒店按钮
     */
    @FXML
    private void handleSearchHotel() {
        System.out.println("🔍 开始搜索酒店");
        // 保存日期
        SessionManager.setCheckInDate(checkInDate.getValue());
        SessionManager.setCheckOutDate(checkOutDate.getValue());

        // 保存人数
        SessionManager.setRoomCount(roomCount);
        SessionManager.setAdultCount(adultCount);
        SessionManager.setChildCount(childCount);
        
        navigateToHotelSearch();
    }
    
    /**
     * 跳转到酒店搜索页面
     */
    private void navigateToHotelSearch() {
        try {
            String keyword = txtDestination.getText().trim();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/search_hotels.fxml")
            );
            Parent root = loader.load();
            
            // ⭐⭐ 获取 search 页面 controller
            SearchHotelsController controller = loader.getController();

            // ⭐⭐ 把搜索关键词传进去
            controller.setSearchKeyword(keyword);
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Search");
            
            System.out.println("✅ 跳转到酒店搜索页面");
            
        } catch (Exception e) {
            System.err.println("❌ 跳转失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ==================== Rooms & Guests Selector 方法 ====================
    
    /**
     * 切换选择器显示/隐藏
     */
    @FXML
    private void toggleRoomsSelector() {
        boolean isVisible = selectorPanel.isVisible();
        selectorPanel.setVisible(!isVisible);
        selectorPanel.setManaged(!isVisible);
        
        // 更新箭头
        lblArrow.setText(isVisible ? "▼" : "▲");
        
        // 如果要显示，计算面板位置
        if (!isVisible) {
            // 获取按钮在屏幕上的位置
            javafx.geometry.Bounds buttonBounds = btnRoomsGuests.localToScreen(btnRoomsGuests.getBoundsInLocal());
            

            // 获取窗口对象
            javafx.stage.Window window = selectorPanel.getScene().getWindow();

             // 计算面板位置：与按钮左对齐，显示在按钮下方
            selectorPanel.setLayoutX(buttonBounds.getMinX() - window.getX());
            selectorPanel.setLayoutY(buttonBounds.getMaxY() - window.getY() + 5);
//            // 计算面板位置
//            selectorPanel.setLayoutX(buttonBounds.getMinX() - 200);
//            selectorPanel.setLayoutY(buttonBounds.getMaxY() + 5);
//            
//            // 设置面板位置：按钮下方5px，右对齐
//            selectorPanel.setLayoutX(buttonBounds.getMinX() - selectorPanel.getScene().getWindow().getX() - 200);
//            selectorPanel.setLayoutY(buttonBounds.getMaxY() - selectorPanel.getScene().getWindow().getY() + 5);
        }
        
        System.out.println("选择器" + (isVisible ? "关闭" : "打开"));
    }
    
    /**
     * 增加房间数
     */
    @FXML
    private void increaseRooms() {
        if (roomCount < MAX_ROOMS) {
            roomCount++;
            updateRoomsGuestsDisplay();
            updateButtons();
            System.out.println("房间数: " + roomCount);
        }
    }
    
    /**
     * 减少房间数
     */
    @FXML
    private void decreaseRooms() {
        if (roomCount > MIN_ROOMS) {
            roomCount--;
            updateRoomsGuestsDisplay();
            updateButtons();
            System.out.println("房间数: " + roomCount);
        }
    }
    
    /**
     * 增加成人数
     */
    @FXML
    private void increaseAdults() {
        int totalGuests = adultCount + childCount;
        int maxAllowed = roomCount * MAX_GUESTS_PER_ROOM;
        
        if (totalGuests < maxAllowed) {
            adultCount++;
            updateRoomsGuestsDisplay();
            updateButtons();
            System.out.println("成人数: " + adultCount);
        }
    }
    
    /**
     * 减少成人数
     */
    @FXML
    private void decreaseAdults() {
        if (adultCount > MIN_ADULTS) {
            adultCount--;
            updateRoomsGuestsDisplay();
            updateButtons();
            System.out.println("成人数: " + adultCount);
        }
    }
    
    /**
     * 增加儿童数
     */
    @FXML
    private void increaseChildren() {
        int totalGuests = adultCount + childCount;
        int maxAllowed = roomCount * MAX_GUESTS_PER_ROOM;
        
        if (totalGuests < maxAllowed) {
            childCount++;
            childrenAges.add(0); // 默认 <1 岁
            updateRoomsGuestsDisplay();
            updateChildrenAgeSelectors();
            updateButtons();
            System.out.println("儿童数: " + childCount);
        }
    }
    
    /**
     * 减少儿童数
     */
    @FXML
    private void decreaseChildren() {
        if (childCount > 0) {
            childCount--;
            if (!childrenAges.isEmpty()) {
                childrenAges.remove(childrenAges.size() - 1);
            }
            updateRoomsGuestsDisplay();
            updateChildrenAgeSelectors();
            updateButtons();
            System.out.println("儿童数: " + childCount);
        }
    }
    
    /**
     * 更新儿童年龄选择器
     */
    private void updateChildrenAgeSelectors() {
        childrenAgeContainer.getChildren().clear();
        
        if (childCount == 0) {
            return;
        }
        
        for (int i = 0; i < childCount; i++) {
            final int index = i;
            
            HBox ageSelector = new HBox(15);
            ageSelector.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            // 标签
            Label label = new Label("Child " + (i + 1) + ": Age");
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
            
            // 年龄下拉框
            ComboBox<String> ageComboBox = new ComboBox<>();
            ageComboBox.setItems(FXCollections.observableArrayList(
                "<1", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
                "10", "11", "12", "13", "14", "15", "16", "17"
            ));
            
            // 设置默认值
            if (index < childrenAges.size()) {
                int age = childrenAges.get(index);
                ageComboBox.setValue(age == 0 ? "<1" : String.valueOf(age));
            } else {
                ageComboBox.setValue("<1");
            }
            
            ageComboBox.setStyle("-fx-pref-width: 100px; -fx-font-size: 14px;");
            
            // 监听选择变化
            ageComboBox.setOnAction(e -> {
                String selected = ageComboBox.getValue();
                int age = selected.equals("<1") ? 0 : Integer.parseInt(selected);
                
                if (index < childrenAges.size()) {
                    childrenAges.set(index, age);
                }
                
                System.out.println("儿童 " + (index + 1) + " 年龄: " + selected);
            });
            
            ageSelector.getChildren().addAll(label, ageComboBox);
            childrenAgeContainer.getChildren().add(ageSelector);
        }
    }
    
    /**
     * 更新 Rooms & Guests 显示
     */
    private void updateRoomsGuestsDisplay() {
        // 更新数字显示
        lblRoomCount.setText(String.valueOf(roomCount));
        lblAdultCount.setText(String.valueOf(adultCount));
        lblChildCount.setText(String.valueOf(childCount));
        
        // 更新按钮上的总显示
        int totalGuests = adultCount + childCount;
        String displayText = roomCount + " Room" + (roomCount > 1 ? "s" : "") + 
                           ", " + totalGuests + " Guest" + (totalGuests > 1 ? "s" : "");
        lblRoomsGuestsDisplay.setText(displayText);
    }
    
    /**
     * 更新按钮启用/禁用状态 ⭐ 这是你问的方法！
     */
    private void updateButtons() {
        // 房间按钮
        btnRoomMinus.setDisable(roomCount <= MIN_ROOMS);
        btnRoomPlus.setDisable(roomCount >= MAX_ROOMS);
        
        // 成人按钮
        btnAdultMinus.setDisable(adultCount <= MIN_ADULTS);
        
        int totalGuests = adultCount + childCount;
        int maxAllowed = roomCount * MAX_GUESTS_PER_ROOM;
        
        btnAdultPlus.setDisable(totalGuests >= maxAllowed);
        btnChildPlus.setDisable(totalGuests >= maxAllowed);
        
        // 儿童按钮
        btnChildMinus.setDisable(childCount <= 0);
    }
    
    /**
     * 重置为默认值
     */
    @FXML
    private void handleReset() {
        System.out.println("🔄 重置选择");
        
        roomCount = 1;
        adultCount = 1;
        childCount = 0;
        childrenAges.clear();
        
        updateRoomsGuestsDisplay();
        updateChildrenAgeSelectors();
        updateButtons();
    }
    
    /**
     * 完成选择
     */
    @FXML
    private void handleDone() {
        System.out.println("✅ 选择完成");
        System.out.println("房间: " + roomCount);
        System.out.println("成人: " + adultCount);
        System.out.println("儿童: " + childCount);
        
        if (childCount > 0) {
            System.out.println("儿童年龄: " + childrenAges);
        }
        
        // 关闭选择器
        toggleRoomsSelector();
    }
    
    // ==================== 导航栏方法 ====================
    
    /**
     * 设置导航栏按钮悬停效果
     */
//    第一行是悬停时效果，第二行是悬停后的效果
    private void setupHoverEffects() {
        // Help 按钮
        setupButtonHover(btnHelp, 
            "-fx-background-color: #f5f5f5; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;",
            "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;"  // 恢复白色文字
        );
        
        // Trips 按钮
        setupButtonHover(btnTrips,
            "-fx-background-color: #f5f5f5; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;",
            "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;"
        );
        
        // Login 按钮
        setupButtonHover(btnLogin,
            "-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 25; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);",
            "-fx-background-color: white; -fx-text-fill: #1a1a1a; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 25; -fx-border-radius: 20; -fx-background-radius: 20;"
        );
    }
    
    private void setupButtonHover(Button button, String hoverStyle, String normalStyle) {
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }
    
    private void updateWelcomeMessage() {
        if (SessionManager.isLoggedIn()) {
            String username = SessionManager.getLoggedInUsername();
            lblWelcome.setText("Welcome back，" + username + "！");
        } else {
            lblWelcome.setText("Welcome to the hotel reservation system");
        }
    }
    
    private void updateLoginButton() {
        if (SessionManager.isLoggedIn()) {
            String username = SessionManager.getLoggedInUsername();
            btnLogin.setText("👤 " + username);
        } else {
            btnLogin.setText("👤 Sign In");
        }
    }
    
    @FXML
    private void handleHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("帮助中心");
        alert.setHeaderText("需要帮助吗？");
        alert.setContentText(
            "常见问题：\n\n" +
            "1. 如何预订房间？\n   选择日期和目的地，浏览可用房间\n\n" +
            "2. 如何查看订单？\n   点击 'My Trips' 按钮\n\n" +
            "3. 联系客服：400-888-8888"
        );
        alert.showAndWait();
    }
    
    /**
     * 处理 Trips 按钮
     */
    @FXML
    private void handleTrips() {
        System.out.println("🔘 Trips 按钮被点击");
        
        if (!SessionManager.isLoggedIn()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("need login");
            alert.setHeaderText(null);
            alert.setContentText("请先登录查看您的订单");
            // 添加按钮
            ButtonType loginBtn = new ButtonType("Login");
            ButtonType cancelBtn = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(loginBtn, cancelBtn);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == loginBtn) {
                System.out.println("➡ 用户选择登录，导航到login");
                navigateToLogin(); 
            }
            return;
        }
        
        navigateToBooking();
        // 这里可以跳转到订单页面
        System.out.println("✅ 跳转到我的订单页面");
        
    }
    
    /**
     * 跳转到用户预订页面
     */
    private void navigateToBooking() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/my_bookings.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bookings");
            
            System.out.println("✅ 跳转到用户预订");
            
        } catch (Exception e) {
            System.err.println("❌ 跳转失败: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogin() {
        if (SessionManager.isLoggedIn()) {
            showUserMenu();
        } else {
            navigateToLogin();
        }
    }
    
    private void showUserMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem profileItem = new MenuItem("👤 My Profile");
        profileItem.setOnAction(e -> System.out.println("打开资料"));
        
        MenuItem logoutItem = new MenuItem("🚪 Logout");
        logoutItem.setOnAction(e -> handleLogout());
        
        contextMenu.getItems().addAll(profileItem, logoutItem);
        contextMenu.show(btnLogin, javafx.geometry.Side.BOTTOM, 0, 5);
    }
    
    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/login.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleLogout() {
        SessionManager.logout();
        updateWelcomeMessage();
        updateLoginButton();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout Successful");
        alert.setContentText("您已成功退出登录");
        alert.showAndWait();
    }
    
    // ==================== 数据获取方法 ====================
    
    public int getRoomCount() {
        return roomCount;
    }
    
    public int getAdultCount() {
        return adultCount;
    }
    
    public int getChildCount() {
        return childCount;
    }
    
    public List<Integer> getChildrenAges() {
        return new ArrayList<>(childrenAges);
    }
}
package com.hotelbooking.controller;

import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;  // ⭐ 新增导入
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javafx.util.converter.LocalDateStringConverter;

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
    @FXML private StackPane selectorPanel;  // ⭐ 改为 StackPane
    
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
        System.out.println("✅ Main Dashboard initialized");
        Locale.setDefault(Locale.ENGLISH);

        // Set default dates (Check-in tomorrow, Check-out day after tomorrow)
        checkInDate.setValue(java.time.LocalDate.now().plusDays(1));
        checkOutDate.setValue(java.time.LocalDate.now().plusDays(2));

        // Ensure check-out date is always later than check-in date
        checkInDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (checkOutDate.getValue().isBefore(newValue)) {
                checkOutDate.setValue(newValue.plusDays(1)); // Set check-out date to one day after check-in
            }
        });

        // Initialize other UI elements
        setupHoverEffects();
        updateWelcomeMessage();
        updateLoginButton();
        updateRoomsGuestsDisplay();
        updateChildrenAgeSelectors();
        updateButtons();
    }
    

    public void initializeDatePickers() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);

        if (checkInDate != null) {
            LocalDate defaultCheckInDate = LocalDate.now().plusDays(1);
            checkInDate.setValue(defaultCheckInDate);
            checkInDate.setConverter(new LocalDateStringConverter(formatter, null));
        }

        if (checkOutDate != null) {
            LocalDate defaultCheckOutDate = LocalDate.now().plusDays(2);
            checkOutDate.setValue(defaultCheckOutDate);
            checkOutDate.setConverter(new LocalDateStringConverter(formatter, null));
        }
    }
    
    /**
     * 处理搜索酒店按钮
     */
    @FXML
    private void handleSearchHotel() {
        System.out.println("🔍 Searching for hotels");

        // Get today's date
        LocalDate today = LocalDate.now();

        // Check if the check-in date is before today's date
        if (checkInDate.getValue().isBefore(today)) {
            showAlert("Invalid Date", "Check-in date cannot be earlier than today's date.");
            return;
        }

        // Check if the check-out date is before the check-in date
        if (checkOutDate.getValue().isBefore(checkInDate.getValue())) {
            showAlert("Invalid Date Range", "Check-out date cannot be earlier than check-in date.");
            return;
        }

        // Ensure check-in and check-out dates are not the same day
        if (checkInDate.getValue().isEqual(checkOutDate.getValue())) {
            showAlert("Invalid Date Range", "Check-in and check-out dates cannot be the same day.");
            return;
        }

        // Save dates and guest counts to session
        SessionManager.setCheckInDate(checkInDate.getValue());
        SessionManager.setCheckOutDate(checkOutDate.getValue());

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
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/search_hotels.fxml",
                "Search Hotel"
            );
            String keyword = txtDestination.getText().trim();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/search_hotels.fxml")
            );
            Parent root = loader.load();
            
            SearchHotelsController controller = loader.getController();
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
            childrenAges.add(0);
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
            
            Label label = new Label("Child " + (i + 1) + ": Age");
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
            
            ComboBox<String> ageComboBox = new ComboBox<>();
            ageComboBox.setItems(FXCollections.observableArrayList(
                "<1", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
                "10", "11", "12", "13", "14", "15", "16", "17"
            ));
            
            if (index < childrenAges.size()) {
                int age = childrenAges.get(index);
                ageComboBox.setValue(age == 0 ? "<1" : String.valueOf(age));
            } else {
                ageComboBox.setValue("<1");
            }
            
            ageComboBox.setStyle("-fx-pref-width: 100px; -fx-font-size: 14px;");
            
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
        lblRoomCount.setText(String.valueOf(roomCount));
        lblAdultCount.setText(String.valueOf(adultCount));
        lblChildCount.setText(String.valueOf(childCount));
        
        int totalGuests = adultCount + childCount;
        String displayText = roomCount + " Room" + (roomCount > 1 ? "s" : "") + 
                           ", " + totalGuests + " Guest" + (totalGuests > 1 ? "s" : "");
        lblRoomsGuestsDisplay.setText(displayText);
    }
    
    /**
     * 更新按钮启用/禁用状态
     */
    private void updateButtons() {
        btnRoomMinus.setDisable(roomCount <= MIN_ROOMS);
        btnRoomPlus.setDisable(roomCount >= MAX_ROOMS);
        
        btnAdultMinus.setDisable(adultCount <= MIN_ADULTS);
        
        int totalGuests = adultCount + childCount;
        int maxAllowed = roomCount * MAX_GUESTS_PER_ROOM;
        
        btnAdultPlus.setDisable(totalGuests >= maxAllowed);
        btnChildPlus.setDisable(totalGuests >= maxAllowed);
        
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
        
        toggleRoomsSelector();
    }
    
    // ==================== 导航栏方法 ====================
    
    /**
     * 设置导航栏按钮悬停效果
     */
    private void setupHoverEffects() {
        setupButtonHover(btnHelp, 
            "-fx-background-color: #f5f5f5; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;",
            "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;"
        );
        
        setupButtonHover(btnTrips,
            "-fx-background-color: #f5f5f5; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;",
            "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;"
        );
        
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
            lblWelcome.setText("Travel Like You Mean It");
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
     * 处理 Trips 按钮
     */
    @FXML
    private void handleTrips() {
        System.out.println("🔘 Trips 按钮被点击");
        
        if (!SessionManager.isLoggedIn()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("need login");
            alert.setHeaderText(null);
            alert.setContentText("please log in first");
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
        System.out.println("✅ 跳转到我的订单页面");
    }
    
    /**
     * 跳转到用户预订页面
     */
    private void navigateToBooking() {
        try {
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/my_bookings.fxml",
                "Bookings"
            );
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
        profileItem.setOnAction(e -> navigateToProfile());
        
        MenuItem logoutItem = new MenuItem("🚪 Logout");
        logoutItem.setOnAction(e -> handleLogout());
        
        contextMenu.getItems().addAll(profileItem, logoutItem);
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
            stage.setTitle("User Profile");
            
        } catch (Exception e) {
            System.err.println("❌ 跳转失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
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
        alert.setContentText("You have successfully logged out");
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


    private void showAlert(String title, String message) {
        // Create an alert of type ERROR
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header
        alert.setContentText(message);  // Display the message

        // Show the alert and wait for user interaction
        alert.showAndWait();
    }
}
package com.hotelbooking.controller;

import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class SearchHotelsController {
    
    @FXML private TextField txtSearch;
    @FXML private Label lblResultCount;
    @FXML private VBox hotelListContainer;
    @FXML private Button btnLogin;
    
    private HotelService hotelService;
    
//    @FXML
//    public void initialize() {
//        System.out.println("✅ 酒店搜索页面初始化");
//        hotelService = new HotelService(); // ✅❗一定用无参构造
//    }
    @FXML
    public void initialize() {
        System.out.println("✅ 酒店搜索页面初始化");
        hotelService = new HotelService(); // ✅❗一定用无参构造

        // 默认显示全部酒店
        List<Hotel> allHotels = hotelService.getAllHotels();
        displayHotels(allHotels);
    }
    
    /**
     * 处理搜索
     */
    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        List<Hotel> results = hotelService.searchHotels(keyword);
        displayHotels(results);
    }
    
    // ⭐⭐ 主界面传入 keyword 后，会调用这个方法
    public void setSearchKeyword(String keyword) {
        System.out.println("🔍 收到 keyword: " + keyword);

        List<Hotel> results;
        if (keyword == null || keyword.isEmpty()) {
            results = hotelService.getAllHotels();
        } else {
            results = hotelService.searchHotels(keyword);
        }

        displayHotels(results);
    }
    
    /**
     * 显示酒店列表
     */
    private void displayHotels(List<Hotel> hotels) {
        hotelListContainer.getChildren().clear();
        
        // 更新结果数量
        lblResultCount.setText("Found " + hotels.size() + " hotel" + (hotels.size() != 1 ? "s" : ""));
        
        if (hotels.isEmpty()) {
            Label noResults = new Label("No hotels found. Try a different search.");
            noResults.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            hotelListContainer.getChildren().add(noResults);
            return;
        }
        
        // 为每个酒店创建卡片
        for (Hotel hotel : hotels) {
            VBox hotelCard = createHotelCard(hotel);
            hotelListContainer.getChildren().add(hotelCard);
        }
    }
    
    /**
     * 创建酒店卡片
     */
    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox(15);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 20; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
            "-fx-cursor: hand;"
        );
        
        // 鼠标悬停效果
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 15, 0, 0, 3); " +
                "-fx-cursor: hand;"
            );
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
                "-fx-cursor: hand;"
            );
        });
        
        // 酒店名称
        Label lblName = new Label(hotel.getName());
        lblName.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
        
        // 地址
        HBox addressBox = new HBox(5);
        addressBox.setAlignment(Pos.CENTER_LEFT);
        Label iconAddress = new Label("📍");
        Label lblAddress = new Label(hotel.getAddress());
        lblAddress.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        addressBox.getChildren().addAll(iconAddress, lblAddress);
        
        // 描述
        Label lblDescription = new Label(hotel.getDescription());
        lblDescription.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        lblDescription.setWrapText(true);
        
        // 设施
        HBox amenitiesBox = new HBox(5);
        amenitiesBox.setAlignment(Pos.CENTER_LEFT);
        Label iconAmenities = new Label("✨");
        Label lblAmenities = new Label(hotel.getAmenities());
        lblAmenities.setStyle("-fx-font-size: 13px; -fx-text-fill: #777;");
        lblAmenities.setWrapText(true);
        amenitiesBox.getChildren().addAll(iconAmenities, lblAmenities);
        
        // 底部：价格和查看详情按钮
        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(bottomBox, Priority.ALWAYS);
        
        // 价格信息
        VBox priceBox = new VBox(2);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        Label lblFromPrice = new Label("From");
        lblFromPrice.setStyle("-fx-font-size: 12px; -fx-text-fill: #999;");
        
//        double minPrice = hotelService.getMinPrice(hotel.getHotelId());
//        Label lblPrice = new Label("$" + String.format("%.0f", minPrice));
//        lblPrice.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #8B4513;");
        
        Label lblPerNight = new Label("per night");
        lblPerNight.setStyle("-fx-font-size: 12px; -fx-text-fill: #999;");
        
//        priceBox.getChildren().addAll(lblFromPrice, lblPrice, lblPerNight);
        
        // 查看详情按钮
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button btnViewRooms = new Button("View Rooms");
        btnViewRooms.setStyle(
            "-fx-background-color: #8B4513; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 12 30; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        );
        
        btnViewRooms.setOnAction(e -> viewHotelRooms(hotel));
        
        bottomBox.getChildren().addAll(priceBox, spacer, btnViewRooms);
        
        // 添加所有元素到卡片
        card.getChildren().addAll(
            lblName,
            addressBox,
            lblDescription,
            amenitiesBox,
            new Separator(),
            bottomBox
        );
        
        return card;
    }
    
    /**
     * 查看酒店房间
     */
    private void viewHotelRooms(Hotel hotel) {
        try {
            System.out.println("🏨 查看酒店房间: " + hotel.getName());
            
            SessionManager.setCurrentHotel(hotel);  // 设置当前酒店信息

            
            // 在任何导航前调用
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/hotel_rooms.fxml",
                "Hotel Rooms"
            );
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/hotel_rooms.fxml")
            );
            Parent root = loader.load();
            
            
            // 传递酒店信息给房间页面
            HotelRoomsController controller = loader.getController();
            controller.setHotel(hotel);
            
            Stage stage = (Stage) hotelListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Rooms - " + hotel.getName());
            
        } catch (Exception e) {
            System.err.println("❌ 加载房间页面失败: " + e.getMessage());
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
            
            Stage stage = (Stage) hotelListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Booking System");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
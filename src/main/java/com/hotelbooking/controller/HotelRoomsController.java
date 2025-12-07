package com.hotelbooking.controller;

import com.hotelbooking.dao.HotelDAO;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class HotelRoomsController {
    
    @FXML private Label lblHotelName;
    @FXML private Label lblHotelAddress;
    @FXML private Label lblHotelDescription;
    @FXML private Label lblHotelAmenities;
    @FXML private Label lblRoomCount;
    @FXML private VBox roomListContainer;
    
    @FXML
    public void initialize() {
        // 从 SessionManager 获取当前酒店信息
        currentHotel = SessionManager.getCurrentHotel();

        // 如果当前酒店不为空，则显示酒店信息和房间信息
        if (currentHotel != null) {
            displayHotelInfo();  // 显示酒店信息
            displayRooms();      // 显示房间信息
        } else {
            // 如果没有酒店信息，则显示错误信息或进行其他处理
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Hotel Information Missing");
            alert.setContentText("The hotel information could not be loaded. Please go back and try again.");
            alert.showAndWait();
        }
    }
    
//    private HotelService hotelService = new HotelService();
    private Hotel currentHotel;
    
    /**
     * 设置酒店信息
     */
    public void setHotel(Hotel hotel) {
        this.currentHotel = hotel;
        displayHotelInfo();
        displayRooms();
    }
    
    /**
     * 显示酒店信息
     */
    private void displayHotelInfo() {
        if (currentHotel == null) return;
        
        lblHotelName.setText(currentHotel.getName());
        lblHotelAddress.setText(currentHotel.getAddress());
        lblHotelDescription.setText(currentHotel.getDescription());
        lblHotelAmenities.setText(currentHotel.getAmenities());
    }
    
    /**
     * 显示房型列表
     */
    private void displayRooms() {
        if (currentHotel == null) return;
        
<<<<<<< HEAD
        System.out.println("✅ Current hotel ID = " + currentHotel.getId());
=======
        System.out.println("✅ 当前酒店ID = " + currentHotel.getId());
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        
        // 假设你已经有了一个 DatabaseConnection 类来获取数据库连接
        HotelDAO hotelDAO = new HotelDAO();  // 创建 HotelDAO 实例

        // 创建 HotelService 实例
        HotelService hotelService = new HotelService(hotelDAO);

        
        // 通过实例调用 getRoomsByHotelId 方法
        List<Room> rooms = hotelService.getRoomsByHotelId(currentHotel.getId());
        lblRoomCount.setText("(" + rooms.size() + " room" + (rooms.size() != 1 ? "s" : "") + ")");
        
        roomListContainer.getChildren().clear();
        
        if (rooms.isEmpty()) {
            Label noRooms = new Label("No rooms available at the moment.");
            noRooms.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            roomListContainer.getChildren().add(noRooms);
            return;
        }
        
        for (Room room : rooms) {
            VBox roomCard = createRoomCard(room);
            roomListContainer.getChildren().add(roomCard);
        }
    }
    
    /**
     * 创建房间卡片
     */
    private VBox createRoomCard(Room room) {
        VBox card = new VBox(15);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 25; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        // 顶部：房型名称和可用状态
        HBox topBox = new HBox(15);
        topBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblRoomType = new Label(room.getRoomType());
        lblRoomType.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
        
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        
        // 可用状态标签
        Label lblStatus = new Label(room.isAvailable() ? "✓ Available" : "✗ Unavailable");
        lblStatus.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: " + (room.isAvailable() ? "#28a745" : "#dc3545") + "; " +
            "-fx-background-color: " + (room.isAvailable() ? "#d4edda" : "#f8d7da") + "; " +
            "-fx-padding: 5 15; " +
            "-fx-border-radius: 15; " +
            "-fx-background-radius: 15;"
        );
        
        topBox.getChildren().addAll(lblRoomType, spacer1, lblStatus);
        
        // 房间信息
        HBox infoBox = new HBox(30);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        // 房间号
        VBox roomNumberBox = new VBox(3);
        Label lblRoomNumberTitle = new Label("Room Number");
        lblRoomNumberTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #999;");
        Label lblRoomNumber = new Label(room.getRoomNumber());
        lblRoomNumber.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        roomNumberBox.getChildren().addAll(lblRoomNumberTitle, lblRoomNumber);
        
        // 最大入住人数
        VBox occupancyBox = new VBox(3);
        Label lblOccupancyTitle = new Label("Max Occupancy");
        lblOccupancyTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #999;");
        Label lblOccupancy = new Label("👥 " + room.getMaxOccupancy() + " guests");
        lblOccupancy.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        occupancyBox.getChildren().addAll(lblOccupancyTitle, lblOccupancy);
        
        infoBox.getChildren().addAll(roomNumberBox, occupancyBox);
        
        // 分隔线
        Separator separator = new Separator();
        
        // 底部：价格和预订按钮
        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        
        // 价格信息
        VBox priceBox = new VBox(3);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblPrice = new Label("$" + String.format("%.2f", room.getPricePerNight()));
        lblPrice.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #8B4513;");
        
        Label lblPerNight = new Label("per night");
        lblPerNight.setStyle("-fx-font-size: 14px; -fx-text-fill: #999;");
        
        priceBox.getChildren().addAll(lblPrice, lblPerNight);
        
        // 占位符
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        // 预订按钮
        Button btnBook = new Button(room.isAvailable() ? "Book Now" : "Unavailable");
        btnBook.setDisable(!room.isAvailable());
        btnBook.setStyle(
            "-fx-background-color: " + (room.isAvailable() ? "#8B4513" : "#cccccc") + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 12 40; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: " + (room.isAvailable() ? "hand" : "default") + ";"
        );
        
        if (room.isAvailable()) {
            btnBook.setOnAction(e -> bookRoom(room));
            
            // 悬停效果
            btnBook.setOnMouseEntered(e -> {
                btnBook.setStyle(
                    "-fx-background-color: #6d3410; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 16px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 12 40; " +
                    "-fx-border-radius: 5; " +
                    "-fx-background-radius: 5; " +
                    "-fx-cursor: hand;"
                );
            });
            
            btnBook.setOnMouseExited(e -> {
                btnBook.setStyle(
                    "-fx-background-color: #8B4513; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 16px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 12 40; " +
                    "-fx-border-radius: 5; " +
                    "-fx-background-radius: 5; " +
                    "-fx-cursor: hand;"
                );
            });
        }
        
        bottomBox.getChildren().addAll(priceBox, spacer2, btnBook);
        
        // 组装卡片
        card.getChildren().addAll(
            topBox,
            infoBox,
            separator,
            bottomBox
        );
        
        return card;
    }
    
    /**
     * 预订房间
     */
    private void bookRoom(Room room) {
<<<<<<< HEAD
        System.out.println("🎫 Booking room: " + room.getRoomType() + " - " + room.getRoomNumber());
=======
        System.out.println("🎫 预订房间: " + room.getRoomType() + " - " + room.getRoomNumber());
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
        
        // 显示确认对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Booking");
        alert.setHeaderText("Book " + room.getRoomType() + "?");
        alert.setContentText(
            "Room: " + room.getRoomNumber() + "\n" +
            "Hotel: " + currentHotel.getName() + "\n" +
            "Price: $" + String.format("%.2f", room.getPricePerNight()) + " per night\n\n" +
            "Proceed to payment?"
        );
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // 跳转到支付页面
                navigateToPayment(room);
            }
        });
    }
    
    /**
     * 跳转到支付页面
     */
    private void navigateToPayment(Room room) {
        try {
            // 在任何导航前调用
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/payment.fxml",  // ← 要导航到的页面
                "Payment"
            );
<<<<<<< HEAD
            System.out.println("💳 Navigating to payment page");
=======
            System.out.println("💳 跳转到支付页面");
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/payment.fxml")
            );
            Parent root = loader.load();
            
            // 传递预订信息给支付页面
             PaymentController controller = loader.getController();
            controller.setBookingInfo(currentHotel, room);
            
            Stage stage = (Stage) roomListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Payment - " + currentHotel.getName());
            
<<<<<<< HEAD
            System.out.println("✅ Navigation successful");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to navigate to payment page: " + e.getMessage());
=======
            System.out.println("✅ 跳转成功");
            
        } catch (Exception e) {
            System.err.println("❌ 跳转支付页面失败: " + e.getMessage());
>>>>>>> 6649ffb6f11ba4a21e86e142d60c4668e7b802ab
            e.printStackTrace();
            
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Navigation Failed");
            errorAlert.setContentText("Unable to open payment page. Please try again.");
            errorAlert.showAndWait();
        }
    }
    
    /**
     * 返回搜索页面
     */
    @FXML
    private void backToSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/search_hotels.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = (Stage) roomListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Search");
            
        } catch (Exception e) {
            System.err.println("❌ 返回搜索页面失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
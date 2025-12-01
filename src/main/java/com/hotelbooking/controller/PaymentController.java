package com.hotelbooking.controller;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PaymentController {
    
    // 支付表单字段
    @FXML private TextField cardNumberField;
    @FXML private TextField cardNameField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private ComboBox<String> countryCombo;
    @FXML private TextField postalCodeField;
    @FXML private Label errorLabel;
    
    // 订单摘要字段
    @FXML private Label lblHotelName;
    @FXML private Label lblRoomType;
    @FXML private Label lblCheckIn;
    @FXML private Label lblCheckOut;
    @FXML private Label lblNights;
    @FXML private Label lblGuests;
    @FXML private Label lblRoomPrice;
    @FXML private Label lblServiceFee;
    @FXML private Label lblTax;
    @FXML private Label lblTotal;
    
    // 预订信息
    private Hotel currentHotel;
    private Room currentRoom;
    private LocalDate checkInDate = LocalDate.now().plusDays(7);
    private LocalDate checkOutDate = LocalDate.now().plusDays(10);
    private int numberOfNights = 3;
    private double roomPrice;
    private double serviceFee;
    private double tax;
    private double totalPrice;
    
    @FXML
    public void initialize() {
        System.out.println("✅ 支付页面初始化");
        
        // 初始化国家列表
        if (countryCombo != null) {
            countryCombo.setItems(FXCollections.observableArrayList(
                "Singapore",
                "United States",
                "United Kingdom",
                "China",
                "Japan",
                "Australia",
                "Canada",
                "Germany",
                "France"
            ));
        }
        
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }
    
    /**
     * 设置预订信息（从房型页面传递过来）
     */
    public void setBookingInfo(Hotel hotel, Room room) {
        this.currentHotel = hotel;
        this.currentRoom = room;
        
        System.out.println("📋 设置预订信息:");
        System.out.println("   酒店: " + hotel.getName());
        System.out.println("   房型: " + room.getRoomType());
        System.out.println("   价格: $" + room.getPricePerNight());
        
        // 计算价格
        calculatePrices();
        
        // 更新界面显示
        updateOrderSummary();
    }
    
    /**
     * 计算价格
     */
    private void calculatePrices() {
        // 计算入住天数
        numberOfNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        
        // 计算总房价
        roomPrice = currentRoom.getPricePerNight() * numberOfNights;
        
        // 服务费（固定25美元）
        serviceFee = 25.00;
        
        // 税费（10%）
        tax = (roomPrice + serviceFee) * 0.10;
        
        // 总价
        totalPrice = roomPrice + serviceFee + tax;
        
        System.out.println("💰 价格计算:");
        System.out.println("   房价: $" + String.format("%.2f", roomPrice));
        System.out.println("   服务费: $" + String.format("%.2f", serviceFee));
        System.out.println("   税费: $" + String.format("%.2f", tax));
        System.out.println("   总计: $" + String.format("%.2f", totalPrice));
    }
    
    /**
     * 更新订单摘要显示
     */
    private void updateOrderSummary() {
        if (currentHotel == null || currentRoom == null) {
            System.out.println("⚠️ 预订信息未设置，使用默认数据");
            loadSampleOrderData();
            return;
        }
        
        // 更新酒店和房型信息
        if (lblHotelName != null) lblHotelName.setText(currentHotel.getName());
        if (lblRoomType != null) lblRoomType.setText(currentRoom.getRoomType());
        
        // 更新日期信息
        if (lblCheckIn != null) lblCheckIn.setText(checkInDate.toString());
        if (lblCheckOut != null) lblCheckOut.setText(checkOutDate.toString());
        if (lblNights != null) lblNights.setText(numberOfNights + " night" + (numberOfNights > 1 ? "s" : ""));
        if (lblGuests != null) lblGuests.setText(currentRoom.getMaxOccupancy() + " guests max");
        
        // 更新价格信息
        if (lblRoomPrice != null) lblRoomPrice.setText("$" + String.format("%.2f", roomPrice));
        if (lblServiceFee != null) lblServiceFee.setText("$" + String.format("%.2f", serviceFee));
        if (lblTax != null) lblTax.setText("$" + String.format("%.2f", tax));
        if (lblTotal != null) lblTotal.setText("$" + String.format("%.2f", totalPrice));
    }
    
    /**
     * 加载示例订单数据（备用）
     */
    private void loadSampleOrderData() {
        if (lblHotelName != null) lblHotelName.setText("Grand Luxury Hotel");
        if (lblRoomType != null) lblRoomType.setText("Deluxe Room");
        if (lblCheckIn != null) lblCheckIn.setText("Dec 15, 2024");
        if (lblCheckOut != null) lblCheckOut.setText("Dec 18, 2024");
        if (lblNights != null) lblNights.setText("3 nights");
        if (lblGuests != null) lblGuests.setText("2 adults");
        if (lblRoomPrice != null) lblRoomPrice.setText("$450.00");
        if (lblServiceFee != null) lblServiceFee.setText("$25.00");
        if (lblTax != null) lblTax.setText("$47.50");
        if (lblTotal != null) lblTotal.setText("$522.50");
    }
    
    /**
     * 处理支付
     */
    @FXML
    private void handlePayment() {
        System.out.println("💳 处理支付");
        
        // 验证支付信息
        if (!validatePaymentInfo()) {
            return;
        }
        
        // 模拟支付处理
        Alert processingAlert = new Alert(Alert.AlertType.INFORMATION);
        processingAlert.setTitle("Processing Payment");
        processingAlert.setHeaderText(null);
        processingAlert.setContentText("Processing your payment...");
        
        // 显示处理中对话框（实际项目中应该是异步处理）
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 模拟支付处理时间
                
                javafx.application.Platform.runLater(() -> {
                    processingAlert.close();
                    showPaymentSuccess();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        processingAlert.show();
    }
    
    /**
     * 验证支付信息
     */
    private boolean validatePaymentInfo() {
        String cardNumber = cardNumberField.getText().trim();
        String cardName = cardNameField.getText().trim();
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();
        String postalCode = postalCodeField.getText().trim();
        
        if (cardNumber.isEmpty() || cardName.isEmpty() || expiry.isEmpty() || 
            cvv.isEmpty() || postalCode.isEmpty()) {
            showError("请填写所有必填字段");
            return false;
        }
        
        // 简单的卡号验证（应该是16位数字）
        String cardNumberClean = cardNumber.replaceAll("\\s+", "");
        if (cardNumberClean.length() != 16 || !cardNumberClean.matches("\\d+")) {
            showError("请输入有效的16位卡号");
            return false;
        }
        
        // CVV验证（应该是3-4位数字）
        if (cvv.length() < 3 || cvv.length() > 4 || !cvv.matches("\\d+")) {
            showError("请输入有效的CVV码");
            return false;
        }
        
        // 到期日验证（MM/YY格式）
        if (!expiry.matches("\\d{2}/\\d{2}")) {
            showError("请输入有效的到期日期 (MM/YY)");
            return false;
        }
        
        if (countryCombo.getValue() == null) {
            showError("请选择国家/地区");
            return false;
        }
        
        return true;
    }
    
    /**
     * 显示支付成功
     */
    private void showPaymentSuccess() {
        // 标记房间为已预订
        if (currentRoom != null) {
            currentRoom.setIsAvailable(false);
        }
        
        String hotelName = currentHotel != null ? currentHotel.getName() : "Grand Luxury Hotel";
        String roomType = currentRoom != null ? currentRoom.getRoomType() : "Deluxe Room";
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Successful");
        alert.setHeaderText("✅ Your booking is confirmed!");
        alert.setContentText(
            "Booking ID: #BK" + System.currentTimeMillis() % 100000 + "\n\n" +
            "Hotel: " + hotelName + "\n" +
            "Room: " + roomType + "\n" +
            "Check-in: " + checkInDate + "\n" +
            "Check-out: " + checkOutDate + "\n" +
            "Total Paid: $" + String.format("%.2f", totalPrice) + "\n\n" +
            "A confirmation email has been sent to your registered email address."
        );
        
        alert.showAndWait();
        
        System.out.println("✅ 预订成功！");
        
        // 跳转到订单页面
        navigateToBookings();
    }
    
    /**
     * 跳转到订单页面
     */
    private void navigateToBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/my_bookings.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) cardNumberField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Bookings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 返回上一页（房型页面）
     */
    @FXML
    private void backToPrevious() {
        try {
            System.out.println("🔙 返回房型页面");
            
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotelbooking/view/hotel_rooms.fxml")
            );
            Parent root = loader.load();
            
            // ⭐ 关键：传递酒店信息回去
            if (currentHotel != null) {
                HotelRoomsController controller = loader.getController();
                controller.setHotel(currentHotel);
                System.out.println("✅ 已传递酒店信息: " + currentHotel.getName());
            } else {
                System.out.println("⚠️ 没有酒店信息可传递");
            }
            
            Stage stage = (Stage) cardNumberField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Rooms");
            
        } catch (Exception e) {
            System.err.println("❌ 返回失败: " + e.getMessage());
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
            // 获取当前窗口
            Stage stage = (Stage) cardNumberField.getScene().getWindow();

            // 设置新场景
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // 可选：设置窗口大小
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
            stage.centerOnScreen();

            System.out.println("✅ 成功返回主页");
            
            
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
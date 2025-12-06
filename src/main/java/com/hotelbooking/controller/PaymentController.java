package com.hotelbooking.controller;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.PaymentService;
import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.PaymentDAO;
import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
    @FXML private Button btnPayment;

    // 预订信息
    private Hotel currentHotel;
    private Room currentRoom;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfNights;
    private BigDecimal roomPrice;
    private BigDecimal serviceFee;
    private BigDecimal tax;
    private BigDecimal totalPrice;

    // Service 层
    private PaymentService paymentService;
    private BookingService bookingService;

    @FXML
    public void initialize() {
        System.out.println("✅ 支付页面初始化");

        // 初始化 Service
        paymentService = new PaymentService(new PaymentDAO());
        bookingService = new BookingService(new BookingDAO(), new RoomDAO());

        // 初始化国家列表
        if (countryCombo != null) {
            countryCombo.setItems(FXCollections.observableArrayList(
                    "Singapore", "United States", "United Kingdom",
                    "China", "Japan", "Australia", "Canada",
                    "Germany", "France"
            ));
        }

        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * 设置预订信息（从房型页面传递过来）
     */
    public void setBookingInfo(Hotel hotel, Room room) {
        this.currentHotel = hotel;
        this.currentRoom = room;
        this.checkInDate = SessionManager.getCheckInDate();
        this.checkOutDate = SessionManager.getCheckOutDate();

        System.out.println("📋 设置预订信息:");
        System.out.println("   酒店: " + hotel.getName());
        System.out.println("   房型: " + room.getRoomType());
        System.out.println("   价格: $" + room.getPricePerNight());

        calculatePrices();
        updateOrderSummary();
    }

    /**
     * 计算价格
     */
    private void calculatePrices() {
        numberOfNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        
        // 使用 BigDecimal 确保精度
        roomPrice = BigDecimal.valueOf(currentRoom.getPricePerNight()*(numberOfNights));
        serviceFee = new BigDecimal("25.00");
        
        // 税费 = (房价 + 服务费) * 10%
        BigDecimal subtotal = roomPrice.add(serviceFee);
        tax = subtotal.multiply(new BigDecimal("0.10")).setScale(2, BigDecimal.ROUND_HALF_UP);
        
        totalPrice = subtotal.add(tax);

        System.out.println("💰 价格计算:");
        System.out.println("   房价: $" + roomPrice);
        System.out.println("   服务费: $" + serviceFee);
        System.out.println("   税费: $" + tax);
        System.out.println("   总计: $" + totalPrice);
    }

    /**
     * 更新订单摘要显示
     */
    private void updateOrderSummary() {
        if (currentHotel == null || currentRoom == null) {
            System.out.println("⚠️ 预订信息未设置");
            return;
        }

        if (lblHotelName != null) lblHotelName.setText(currentHotel.getName());
        if (lblRoomType != null) lblRoomType.setText(currentRoom.getRoomType());
        if (lblCheckIn != null) lblCheckIn.setText(checkInDate.toString());
        if (lblCheckOut != null) lblCheckOut.setText(checkOutDate.toString());
        if (lblNights != null) lblNights.setText(numberOfNights + " night" + (numberOfNights > 1 ? "s" : ""));
        if (lblGuests != null) lblGuests.setText(currentRoom.getMaxOccupancy() + " guests max");
        if (lblRoomPrice != null) lblRoomPrice.setText("$" + roomPrice.toPlainString());
        if (lblServiceFee != null) lblServiceFee.setText("$" + serviceFee.toPlainString());
        if (lblTax != null) lblTax.setText("$" + tax.toPlainString());
        if (lblTotal != null) lblTotal.setText("$" + totalPrice.toPlainString());
    }

    /**
     * 处理支付
     */
    @FXML
    private void handlePayment() {
        System.out.println("💳 处理支付");

        // 1. 验证用户是否登录
        if (!SessionManager.isLoggedIn()) {
            showError("请先登录");
            return;
        }

        // 2. 验证支付信息
        if (!validatePaymentInfo()) {
            return;
        }

        // 3. 验证预订信息
        if (currentHotel == null || currentRoom == null) {
            showError("预订信息不完整，请返回重新选择");
            return;
        }

        // 4. 禁用支付按钮，防止重复点击
        if (btnPayment != null) {
            btnPayment.setDisable(true);
        }

        // 5. 在后台线程中处理支付
        new Thread(() -> {
            try {
                Integer userId = SessionManager.getLoggedInId();
                Integer hotelId = currentHotel.getId();
                Integer roomId = currentRoom.getId();

                System.out.println("📝 准备创建预订...");
                System.out.println("   用户ID: " + userId);
                System.out.println("   酒店ID: " + hotelId);
                System.out.println("   房间ID: " + roomId);

                // 6. 创建预订
                Optional<Booking> bookingOpt = bookingService.createBooking(
                    userId, 
                    hotelId, 
                    roomId, 
                    checkInDate, 
                    checkOutDate
                );

                if (bookingOpt.isEmpty()) {
                    Platform.runLater(() -> {
                        showError("创建预订失败，房间可能不可用");
                        if (btnPayment != null) btnPayment.setDisable(false);
                    });
                    return;
                }

                Booking booking = bookingOpt.get();
                System.out.println("✅ 预订已创建，预订ID: " + booking.getId());

                // 7. 处理支付 - 使用 booking_id（VARCHAR）而不是 id（INTEGER）
                System.out.println("💳 处理支付...");
                // ⚠️ 重要：使用 booking.getBookingId() 而不是 booking.getId()
                String bookingIdForPayment = booking.getBookingId();
                if (bookingIdForPayment == null || bookingIdForPayment.isEmpty()) {
                    // 如果 booking_id 为空，使用生成的 ID
                    bookingIdForPayment = "BK_" + booking.getId() + "_" + System.currentTimeMillis();
                }
                System.out.println("   预订ID (支付用): " + bookingIdForPayment);
                
                boolean paymentSuccess = paymentService.processPayment(
                    bookingIdForPayment,
                    totalPrice,
                    "CREDIT_CARD"
                );

                if (paymentSuccess) {
                    System.out.println("✅ 支付成功！");
                    Platform.runLater(() -> {
                        showPaymentSuccess();
                        navigateToBookings();
                    });
                } else {
                    System.out.println("❌ 支付失败");
                    // 取消预订
                    bookingService.cancelBooking(booking.getId());
                    Platform.runLater(() -> {
                        showError("支付失败，预订已取消，请重试");
                        if (btnPayment != null) btnPayment.setDisable(false);
                    });
                }

            } catch (BusinessException e) {
                System.err.println("❌ 业务异常: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> {
                    showError("支付失败: " + e.getMessage());
                    if (btnPayment != null) btnPayment.setDisable(false);
                });
            } catch (Exception e) {
                System.err.println("❌ 系统异常: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> {
                    showError("系统错误: " + e.getMessage());
                    if (btnPayment != null) btnPayment.setDisable(false);
                });
            }
        }).start();
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

        if (!cardNumber.replaceAll("\\s+", "").matches("\\d{16}")) {
            showError("请输入有效的16位卡号");
            return false;
        }

        if (!cvv.matches("\\d{3,4}")) {
            showError("请输入有效的CVV码");
            return false;
        }

        if (!expiry.matches("\\d{2}/\\d{2}")) {
            showError("请输入有效的到期日期 (MM/YY)");
            return false;
        }

        if (countryCombo.getValue() == null || countryCombo.getValue().isEmpty()) {
            showError("请选择国家/地区");
            return false;
        }

        return true;
    }

    /**
     * 显示支付成功提示
     */
    private void showPaymentSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Successful");
        alert.setHeaderText("✅ Your booking is confirmed!");
        alert.setContentText(
                "Hotel: " + currentHotel.getName() + "\n" +
                "Room: " + currentRoom.getRoomType() + "\n" +
                "Check-in: " + checkInDate + "\n" +
                "Check-out: " + checkOutDate + "\n" +
                "Total Paid: $" + totalPrice.toPlainString()
        );
        alert.showAndWait();
    }

    /**
     * 导航到我的预订页面
     */
    private void navigateToBookings() {
        try {
            // 在任何导航前调用
            NavigationManager.getInstance().push(
                "/com/hotelbooking/view/payment.fxml",
                "Payment"
            );
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hotelbooking/view/my_bookings.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) cardNumberField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Bookings");
        } catch (Exception e) {
            System.err.println("❌ 导航失败: " + e.getMessage());
            e.printStackTrace();
            showError("页面加载失败: " + e.getMessage());
        }
    }

    /**
     * 返回上一页
     */
//    @FXML
//    private void backToPrevious() {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("/com/hotelbooking/view/hotel_rooms.fxml")
//            );
//            Parent root = loader.load();
//
//            if (currentHotel != null) {
//                HotelRoomsController controller = loader.getController();
//                controller.setHotel(currentHotel);
//            }
//
//            Stage stage = (Stage) cardNumberField.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Hotel Rooms");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showError("返回失败: " + e.getMessage());
//        }
//    }

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
            Stage stage = (Stage) cardNumberField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hotel Booking System");
        } catch (Exception e) {
            e.printStackTrace();
            showError("返回失败: " + e.getMessage());
        }
    }

    /**
     * 显示错误信息
     */
    private void showError(String message) {
        System.err.println("❌ 错误: " + message);
        if (errorLabel != null) {
            Platform.runLater(() -> errorLabel.setText(message));
        }
    }
}
//package com.hotelbooking.controller;
//
//import com.hotelbooking.entity.Hotel;
//import com.hotelbooking.entity.Room;
//import com.hotelbooking.entity.Payment;
//import com.hotelbooking.service.PaymentService;
//import com.hotelbooking.dao.PaymentDAO;
//import com.hotelbooking.exception.BusinessException;
//import com.hotelbooking.util.SessionManager;
//import java.math.BigDecimal;
//import javafx.collections.FXCollections;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.stage.Stage;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//
//public class PaymentController {
//
//    // 支付表单字段
//    @FXML private TextField cardNumberField;
//    @FXML private TextField cardNameField;
//    @FXML private TextField expiryField;
//    @FXML private TextField cvvField;
//    @FXML private ComboBox<String> countryCombo;
//    @FXML private TextField postalCodeField;
//    @FXML private Label errorLabel;
//
//    // 订单摘要字段
//    @FXML private Label lblHotelName;
//    @FXML private Label lblRoomType;
//    @FXML private Label lblCheckIn;
//    @FXML private Label lblCheckOut;
//    @FXML private Label lblNights;
//    @FXML private Label lblGuests;
//    @FXML private Label lblRoomPrice;
//    @FXML private Label lblServiceFee;
//    @FXML private Label lblTax;
//    @FXML private Label lblTotal;
//
//    // 预订信息
//    private Hotel currentHotel;
//    private Room currentRoom;
//    private LocalDate checkInDate;
//    private LocalDate checkOutDate;
//    private int numberOfNights;
//    private double roomPrice;
//    private double serviceFee;
//    private double tax;
//    private double totalPrice;
//
//    // Service 层
//    private PaymentService paymentService;
//
//    @FXML
//    public void initialize() {
//        System.out.println("✅ 支付页面初始化");
//
//        // 初始化 PaymentService
//        paymentService = new PaymentService(new PaymentDAO());
//
//        // 初始化国家列表
//        if (countryCombo != null) {
//            countryCombo.setItems(FXCollections.observableArrayList(
//                    "Singapore", "United States", "United Kingdom",
//                    "China", "Japan", "Australia", "Canada",
//                    "Germany", "France"
//            ));
//        }
//
//        if (errorLabel != null) errorLabel.setText("");
//    }
//
//    /**
//     * 设置预订信息（从房型页面传递过来）
//     */
//    public void setBookingInfo(Hotel hotel, Room room) {
//        this.currentHotel = hotel;
//        this.currentRoom = room;
//        
//        this.checkInDate = SessionManager.getCheckInDate();
//        this.checkOutDate = SessionManager.getCheckOutDate();
//
//        System.out.println("📋 设置预订信息:");
//        System.out.println("   酒店: " + hotel.getName());
//        System.out.println("   房型: " + room.getRoomType());
//        System.out.println("   价格: $" + room.getPricePerNight());
//
//        // 计算入住天数
//        this.numberOfNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
//
//        calculatePrices();
//        updateOrderSummary();
//    }
//
//    /**
//     * 计算价格
//     */
//    private void calculatePrices() {
//        numberOfNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
//        roomPrice = currentRoom.getPricePerNight() * numberOfNights;
//        serviceFee = 25.00;
//        tax = (roomPrice + serviceFee) * 0.10;
//        totalPrice = roomPrice + serviceFee + tax;
//
//        System.out.println("💰 价格计算:");
//        System.out.println("   房价: $" + String.format("%.2f", roomPrice));
//        System.out.println("   服务费: $" + String.format("%.2f", serviceFee));
//        System.out.println("   税费: $" + String.format("%.2f", tax));
//        System.out.println("   总计: $" + String.format("%.2f", totalPrice));
//    }
//
//    /**
//     * 更新订单摘要显示
//     */
//    private void updateOrderSummary() {
//        if (currentHotel == null || currentRoom == null) {
//            System.out.println("⚠️ 预订信息未设置");
//            return;
//        }
//
//        if (lblHotelName != null) lblHotelName.setText(currentHotel.getName());
//        if (lblRoomType != null) lblRoomType.setText(currentRoom.getRoomType());
//        if (lblCheckIn != null) lblCheckIn.setText(checkInDate.toString());
//        if (lblCheckOut != null) lblCheckOut.setText(checkOutDate.toString());
//        if (lblNights != null) lblNights.setText(numberOfNights + " night" + (numberOfNights > 1 ? "s" : ""));
//        if (lblGuests != null) lblGuests.setText(currentRoom.getMaxOccupancy() + " guests max");
//        if (lblRoomPrice != null) lblRoomPrice.setText("$" + String.format("%.2f", roomPrice));
//        if (lblServiceFee != null) lblServiceFee.setText("$" + String.format("%.2f", serviceFee));
//        if (lblTax != null) lblTax.setText("$" + String.format("%.2f", tax));
//        if (lblTotal != null) lblTotal.setText("$" + String.format("%.2f", totalPrice));
//    }
//
//    /**
//     * 处理支付
//     */
//    @FXML
//    private void handlePayment() {
//        System.out.println("💳 处理支付");
//
//        if (!validatePaymentInfo()) return;
//        
//        if (!SessionManager.isLoggedIn()) {
//            showError("请先登录");
//            return;
//        }
//        String userId = SessionManager.getLoggedInUsername();
//
//        // 创建支付实体
//        Payment payment = new Payment();
//        payment.setPaymentId("PAY_" + System.currentTimeMillis() % 100000);
//        payment.setBookingId("BK_" + System.currentTimeMillis() % 100000); // TODO: 替换为真实 booking_id
//        payment.setAmount(BigDecimal.valueOf(totalPrice));
////payment.setAmount(totalPrice);
//        payment.setPaymentMethod("CREDIT_CARD");
//        payment.setPaymentStatus("COMPLETED");
//        payment.setTransactionId("TXN_" + System.currentTimeMillis() % 100000);
//
//        // 保存支付信息到数据库
//        try {
//            // 调用 PaymentService 处理支付
//            boolean paymentSuccess = paymentService.processPayment(payment.getBookingId(),
//                    payment.getAmount(), payment.getPaymentMethod());
//            System.out.println("✅ 支付已记录到数据库");
//
//            if (paymentSuccess) {
//                System.out.println("✅ 支付已成功");
//
//                // 显示成功提示
//                showPaymentSuccess();
//            } else {
//                showError("支付失败，请重试");
//            }
//        } catch (BusinessException e) {
//            // 处理支付失败的异常
//            showError("支付失败: " + e.getMessage());
//            System.err.println("支付失败: " + e.getMessage());
//        } catch (Exception e) {
//            // 捕获其他异常
//            showError("系统错误，请稍后再试");
//            e.printStackTrace();
//        }
//
//    }
//
//    private boolean validatePaymentInfo() {
//        String cardNumber = cardNumberField.getText().trim();
//        String cardName = cardNameField.getText().trim();
//        String expiry = expiryField.getText().trim();
//        String cvv = cvvField.getText().trim();
//        String postalCode = postalCodeField.getText().trim();
//
//        if (cardNumber.isEmpty() || cardName.isEmpty() || expiry.isEmpty() ||
//            cvv.isEmpty() || postalCode.isEmpty()) {
//            showError("请填写所有必填字段");
//            return false;
//        }
//
//        if (!cardNumber.replaceAll("\\s+", "").matches("\\d{16}")) {
//            showError("请输入有效的16位卡号");
//            return false;
//        }
//
//        if (!cvv.matches("\\d{3,4}")) {
//            showError("请输入有效的CVV码");
//            return false;
//        }
//
//        if (!expiry.matches("\\d{2}/\\d{2}")) {
//            showError("请输入有效的到期日期 (MM/YY)");
//            return false;
//        }
//
//        if (countryCombo.getValue() == null) {
//            showError("请选择国家/地区");
//            return false;
//        }
//
//        return true;
//    }
//
//    private void showPaymentSuccess() {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Payment Successful");
//        alert.setHeaderText("✅ Your booking is confirmed!");
//        alert.setContentText(
//                "Hotel: " + currentHotel.getName() + "\n" +
//                "Room: " + currentRoom.getRoomType() + "\n" +
//                "Total Paid: $" + String.format("%.2f", totalPrice)
//        );
//        alert.showAndWait();
//
//        System.out.println("✅ 预订成功！");
//        navigateToBookings();
//    }
//
//    private void navigateToBookings() {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("/com/hotelbooking/view/my_bookings.fxml")
//            );
//            Parent root = loader.load();
//            Stage stage = (Stage) cardNumberField.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setTitle("My Bookings");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void backToPrevious() {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("/com/hotelbooking/view/hotel_rooms.fxml")
//            );
//            Parent root = loader.load();
//
//            if (currentHotel != null) {
//                HotelRoomsController controller = loader.getController();
//                controller.setHotel(currentHotel);
//            }
//
//            Stage stage = (Stage) cardNumberField.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Hotel Rooms");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void backToHome() {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("/com/hotelbooking/view/main_dashboard.fxml")
//            );
//            Parent root = loader.load();
//            Stage stage = (Stage) cardNumberField.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Hotel Booking System");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void showError(String message) {
//        if (errorLabel != null) errorLabel.setText(message);
//    }
//}
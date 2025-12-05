package com.hotelbooking.controller;

import com.hotelbooking.dao.BookingDAO;
import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.util.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class MyBookingsController {
    
    @FXML
    private VBox bookingListContainer;
    
    @FXML
    private VBox emptyStateContainer;
    
    @FXML
    private Label lblBookingCount;
    
    @FXML
    private ComboBox<String> cbStatus;
    
    @FXML
    private Button btnLogin;
    
    private BookingService bookingService;
    private Integer currentUserId;
    private String selectedStatus = "All";
    
    @FXML
    public void initialize() {
        // 初始化 DAO 和 Service
        BookingDAO bookingDAO = new BookingDAO();
        RoomDAO roomDAO = new RoomDAO();
        bookingService = new BookingService(bookingDAO, roomDAO);
        
        setupStatusFilter();
        loadBookings();
    }
    
    private void setupStatusFilter() {
        cbStatus.getItems().addAll("All", "CONFIRMED", "PENDING", "CANCELLED", "COMPLETED");
        cbStatus.setValue("All");
    }
    
    private void loadBookings() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    currentUserId = getCurrentUserId();

                    if (currentUserId == null) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                showLoginRequired();
                            }
                        });
                        return;
                    }

                    List<Booking> bookings = bookingService.getBookingsByUserId(currentUserId);

                    // 按选定的状态过滤
                    if (!"All".equals(selectedStatus)) {
                        List<Booking> filtered = new ArrayList<>();
                        for (Booking b : bookings) {
                            if (selectedStatus.equals(b.getStatus())) {
                                filtered.add(b);
                            }
                        }
                        bookings = filtered;
                    }

                    final List<Booking> finalBookings = bookings;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            displayBookings(finalBookings);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            showError("Failed to load bookings: " + e.getMessage());
                        }
                    });
                }
            }
        });

        thread.start();
    }
    
    private void displayBookings(List<Booking> bookings) {
        bookingListContainer.getChildren().clear();
        
        if (bookings.isEmpty()) {
            bookingListContainer.setVisible(false);
            emptyStateContainer.setVisible(true);
            lblBookingCount.setText("Total bookings: 0");
            return;
        }
        
        bookingListContainer.setVisible(true);
        emptyStateContainer.setVisible(false);
        lblBookingCount.setText("Total bookings: " + bookings.size());
        
        for (Booking booking : bookings) {
            VBox bookingCard = createBookingCard(booking);
            bookingListContainer.getChildren().add(bookingCard);
        }
    }
    
    private VBox createBookingCard(Booking booking) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 20;");
        card.setSpacing(12);
        
        // 预订信息头部
        HBox header = createCardHeader(booking);
        
        // 预订详情
        VBox details = createBookingDetails(booking);
        
        // 操作按钮
        HBox actions = createActionButtons(booking);
        
        card.getChildren().addAll(header, new Separator(), details, actions);
        return card;
    }
    
    private HBox createCardHeader(Booking booking) {
        HBox header = new HBox();
        header.setSpacing(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label bookingId = new Label("Booking #" + booking.getId());
        bookingId.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
        
        Label status = createStatusBadge(booking.getStatus());
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label date = new Label("Booked: " + booking.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        date.setStyle("-fx-font-size: 12px; -fx-text-fill: #999;");
        
        header.getChildren().addAll(bookingId, status, spacer, date);
        return header;
    }
    
    private Label createStatusBadge(String status) {
        Label badge = new Label(status);
        String color = switch (status) {
            case "CONFIRMED" -> "#4CAF50";
            case "PENDING" -> "#FF9800";
            case "CANCELLED" -> "#F44336";
            case "COMPLETED" -> "#2196F3";
            default -> "#999";
        };
        badge.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 5 12; -fx-border-radius: 15; -fx-background-radius: 15; -fx-font-size: 12px; -fx-font-weight: bold;");
        return badge;
    }
    
    private VBox createBookingDetails(Booking booking) {
        VBox details = new VBox();
        details.setSpacing(8);
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // 入住和退房日期
        HBox dates = new HBox();
        dates.setSpacing(20);
        Label checkIn = new Label("✓ Check-in: " + booking.getCheckInDate().format(dateFormat));
        Label checkOut = new Label("✓ Check-out: " + booking.getCheckOutDate().format(dateFormat));
        checkIn.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
        checkOut.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
        dates.getChildren().addAll(checkIn, checkOut);
        
        // 房间和酒店信息
        HBox info = new HBox();
        info.setSpacing(20);
        Label room = new Label("🛏️ Room ID: " + booking.getRoomId());
        Label hotel = new Label("🏨 Hotel ID: " + booking.getHotelId());
        room.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
        hotel.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
        info.getChildren().addAll(room, hotel);
        
        // 总价格
        Label price = new Label("💰 Total Price: $" + booking.getTotalPrice());
        price.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #8B4513;");
        
        details.getChildren().addAll(dates, info, price);
        return details;
    }
    
    private HBox createActionButtons(Booking booking) {
        HBox actions = new HBox();
        actions.setSpacing(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        
        Button viewDetails = new Button("View Details");
        viewDetails.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
        viewDetails.setOnAction(e -> showBookingDetails(booking));
        
        Button completeBooking = new Button("Complete");
        completeBooking.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
        completeBooking.setOnAction(e -> handleCompleteBooking(booking));
        
        Button cancelBooking = new Button("Cancel");
        cancelBooking.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-size: 12px; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;");
        cancelBooking.setOnAction(e -> handleCancelBooking(booking));
        
        // 根据预订状态禁用相应的按钮
        if ("CANCELLED".equals(booking.getStatus()) || "COMPLETED".equals(booking.getStatus())) {
            cancelBooking.setDisable(true);
            completeBooking.setDisable(true);
        } else if ("COMPLETED".equals(booking.getStatus())) {
            completeBooking.setDisable(true);
        }
        
        actions.getChildren().addAll(viewDetails, completeBooking, cancelBooking);
        return actions;
    }
    
    @FXML
    private void handleStatusFilter() {
        selectedStatus = cbStatus.getValue();
        loadBookings();
    }
    
    private void showBookingDetails(Booking booking) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Details");
        alert.setHeaderText("Booking #" + booking.getId());
        
        String details = String.format(
            "User ID: %d\n" +
            "Hotel ID: %d\n" +
            "Room ID: %d\n" +
            "Check-in: %s\n" +
            "Check-out: %s\n" +
            "Total Price: $%s\n" +
            "Status: %s\n" +
            "Created: %s",
            booking.getUserId(),
            booking.getHotelId(),
            booking.getRoomId(),
            booking.getCheckInDate(),
            booking.getCheckOutDate(),
            booking.getTotalPrice(),
            booking.getStatus(),
            booking.getCreatedAt()
        );
        
        alert.setContentText(details);
        alert.showAndWait();
    }
    
    private void handleCancelBooking(Booking booking) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Cancel Booking");
        confirmDialog.setHeaderText("Are you sure?");
        confirmDialog.setContentText("Cancel booking #" + booking.getId() + "?");
        
        if (confirmDialog.showAndWait().get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    boolean success = bookingService.cancelBooking(booking.getId());
                    Platform.runLater(() -> {
                        if (success) {
                            showSuccess("Booking cancelled successfully");
                            loadBookings();
                        } else {
                            showError("Failed to cancel booking");
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showError("Failed to cancel booking: " + e.getMessage()));
                }
            }).start();
        }
    }
    
    private void handleCompleteBooking(Booking booking) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Complete Booking");
        confirmDialog.setHeaderText("Check out?");
        confirmDialog.setContentText("Mark booking #" + booking.getId() + " as completed?");
        
        if (confirmDialog.showAndWait().get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    boolean success = bookingService.completeBooking(booking.getId());
                    Platform.runLater(() -> {
                        if (success) {
                            showSuccess("Booking completed successfully");
                            loadBookings();
                        } else {
                            showError("Failed to complete booking");
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showError("Failed to complete booking: " + e.getMessage()));
                }
            }).start();
        }
    }
    
    @FXML
    private void backToHome() {
        loadScene("MainWindow.fxml");
    }
    
    @FXML
    private void goToSearch() {
        loadScene("SearchHotels.fxml");
    }
    
    private void showLoginRequired() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("Please log in first");
        alert.setContentText("You need to log in to view your bookings.");
        alert.showAndWait();
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotelbooking/view/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) bookingListContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load page: " + e.getMessage());
        }
    }
    
    private Integer getCurrentUserId() {
        // 从全局用户会话获取当前用户ID
        // 这里需要根据你的实际实现调整
        return UserSession.getInstance().getCurrentUserId();
    }
}
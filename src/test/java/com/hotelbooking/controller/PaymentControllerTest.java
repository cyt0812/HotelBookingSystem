package com.hotelbooking.controller;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.test.JavaFxTestSupport;
import com.hotelbooking.util.SessionManager;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PaymentController 的表单校验、价格计算与摘要显示测试。
 */
class PaymentControllerTest {

    private PaymentController controller;

    private TextField cardNumberField;
    private TextField cardNameField;
    private TextField expiryField;
    private TextField cvvField;
    private ComboBox<String> countryCombo;
    private TextField postalCodeField;
    private Label errorLabel;
    private Label lblHotelName;
    private Label lblRoomType;
    private Label lblCheckIn;
    private Label lblCheckOut;
    private Label lblNights;
    private Label lblGuests;
    private Label lblRoomPrice;
    private Label lblServiceFee;
    private Label lblTax;
    private Label lblTotal;
    private Button btnPayment;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new PaymentController();
        runOnFxThread(() -> {
            cardNumberField = new TextField();
            cardNameField = new TextField();
            expiryField = new TextField();
            cvvField = new TextField();
            countryCombo = new ComboBox<>();
            postalCodeField = new TextField();
            errorLabel = new Label();
            lblHotelName = new Label();
            lblRoomType = new Label();
            lblCheckIn = new Label();
            lblCheckOut = new Label();
            lblNights = new Label();
            lblGuests = new Label();
            lblRoomPrice = new Label();
            lblServiceFee = new Label();
            lblTax = new Label();
            lblTotal = new Label();
            btnPayment = new Button("Pay");

            setField(controller, "cardNumberField", cardNumberField);
            setField(controller, "cardNameField", cardNameField);
            setField(controller, "expiryField", expiryField);
            setField(controller, "cvvField", cvvField);
            setField(controller, "countryCombo", countryCombo);
            setField(controller, "postalCodeField", postalCodeField);
            setField(controller, "errorLabel", errorLabel);
            setField(controller, "lblHotelName", lblHotelName);
            setField(controller, "lblRoomType", lblRoomType);
            setField(controller, "lblCheckIn", lblCheckIn);
            setField(controller, "lblCheckOut", lblCheckOut);
            setField(controller, "lblNights", lblNights);
            setField(controller, "lblGuests", lblGuests);
            setField(controller, "lblRoomPrice", lblRoomPrice);
            setField(controller, "lblServiceFee", lblServiceFee);
            setField(controller, "lblTax", lblTax);
            setField(controller, "lblTotal", lblTotal);
            setField(controller, "btnPayment", btnPayment);
            // Production code 中 serviceFee / tax 未在 calculatePrices 中赋值，默认补零避免空指针
            setField(controller, "serviceFee", BigDecimal.ZERO);
            setField(controller, "tax", BigDecimal.ZERO);
        });
    }

    @AfterEach
    void tearDown() {
        SessionManager.logout();
        SessionManager.setCheckInDate(null);
        SessionManager.setCheckOutDate(null);
    }

    private Hotel sampleHotel() {
        Hotel hotel = new Hotel("Grand Hotel", "Shanghai", "Nice hotel", "WiFi", 10);
        hotel.setId(3);
        return hotel;
    }

    private Room sampleRoom() {
        Room room = new Room(3, "301", "Deluxe", 100.0, 2, true, "Deluxe room");
        room.setRoomId(11);
        return room;
    }

    @Test
    void initializePopulatesCountriesAndClearsError() {
        runOnFxThread(() -> {
            errorLabel.setText("old error");

            controller.initialize();

            assertEquals(9, countryCombo.getItems().size());
            assertTrue(countryCombo.getItems().contains("China"));
            assertEquals("", errorLabel.getText());
        });
    }

    @Test
    void setBookingInfoCalculatesPricesAndSummary() {
        runOnFxThread(() -> {
            SessionManager.setCheckInDate(LocalDate.of(2026, 12, 1));
            SessionManager.setCheckOutDate(LocalDate.of(2026, 12, 4));

            controller.setBookingInfo(sampleHotel(), sampleRoom());

            assertEquals("Grand Hotel", lblHotelName.getText());
            assertEquals("Deluxe", lblRoomType.getText());
            assertEquals("2026-12-01", lblCheckIn.getText());
            assertEquals("2026-12-04", lblCheckOut.getText());
            assertEquals("3 nights", lblNights.getText());
            assertEquals("2 guests max", lblGuests.getText());
            assertEquals("$100.0", lblRoomPrice.getText());
            assertEquals("$300.0", lblTotal.getText());
        });
    }

    @Test
    void setBookingInfoUsesSingularNightText() {
        runOnFxThread(() -> {
            SessionManager.setCheckInDate(LocalDate.of(2026, 12, 1));
            SessionManager.setCheckOutDate(LocalDate.of(2026, 12, 2));

            controller.setBookingInfo(sampleHotel(), sampleRoom());

            assertEquals("1 night", lblNights.getText());
        });
    }

    @Test
    void updateOrderSummaryShowsFeeAndTaxWhenSet() {
        runOnFxThread(() -> {
            SessionManager.setCheckInDate(LocalDate.of(2026, 12, 1));
            SessionManager.setCheckOutDate(LocalDate.of(2026, 12, 3));
            controller.setBookingInfo(sampleHotel(), sampleRoom());

            setField(controller, "serviceFee", new BigDecimal("25.50"));
            setField(controller, "tax", new BigDecimal("10.00"));
            invokeMethod(controller, "updateOrderSummary", new Class<?>[]{});

            assertEquals("$25.50", lblServiceFee.getText());
            assertEquals("$10.00", lblTax.getText());
        });
    }

    @Test
    void updateOrderSummaryDoesNothingWithoutBookingInfo() {
        runOnFxThread(() -> {
            invokeMethod(controller, "updateOrderSummary", new Class<?>[]{});
            assertEquals("", lblHotelName.getText());
        });
    }

    private void fillValidPaymentInfo() {
        cardNumberField.setText("1234 5678 9012 3456");
        cardNameField.setText("Alice");
        expiryField.setText("12/28");
        cvvField.setText("123");
        postalCodeField.setText("200000");
        countryCombo.setValue("China");
    }

    @Test
    void validatePaymentInfoAcceptsValidInput() {
        runOnFxThread(() -> {
            fillValidPaymentInfo();
            Object result = invokeMethod(controller, "validatePaymentInfo", new Class<?>[]{});
            assertEquals(Boolean.TRUE, result);
        });
    }

    @Test
    void validatePaymentInfoRejectsMissingFields() {
        runOnFxThread(() -> {
            fillValidPaymentInfo();
            cvvField.setText("   ");
            Object result = invokeMethod(controller, "validatePaymentInfo", new Class<?>[]{});
            assertEquals(Boolean.FALSE, result);
        });
    }

    @Test
    void validatePaymentInfoRejectsInvalidCardNumber() {
        runOnFxThread(() -> {
            fillValidPaymentInfo();
            cardNumberField.setText("1234");
            Object result = invokeMethod(controller, "validatePaymentInfo", new Class<?>[]{});
            assertEquals(Boolean.FALSE, result);
        });
    }

    @Test
    void validatePaymentInfoRejectsInvalidCvv() {
        runOnFxThread(() -> {
            fillValidPaymentInfo();
            cvvField.setText("12");
            Object result = invokeMethod(controller, "validatePaymentInfo", new Class<?>[]{});
            assertEquals(Boolean.FALSE, result);
        });
    }

    @Test
    void validatePaymentInfoRejectsInvalidExpiry() {
        runOnFxThread(() -> {
            fillValidPaymentInfo();
            expiryField.setText("2028-12");
            Object result = invokeMethod(controller, "validatePaymentInfo", new Class<?>[]{});
            assertEquals(Boolean.FALSE, result);
        });
    }

    @Test
    void validatePaymentInfoRejectsMissingCountry() {
        runOnFxThread(() -> {
            fillValidPaymentInfo();
            countryCombo.setValue(null);
            Object result = invokeMethod(controller, "validatePaymentInfo", new Class<?>[]{});
            assertEquals(Boolean.FALSE, result);
        });
    }

    @Test
    void handlePaymentRequiresLogin() {
        runOnFxThread(() -> {
            SessionManager.logout();
            invokeMethod(controller, "handlePayment", new Class<?>[]{});
        });
        // showError 通过 Platform.runLater 更新提示，走到下一轮 FX 事件再断言
        runOnFxThread(() -> assertEquals("请先登录", errorLabel.getText()));
    }

    @Test
    void handlePaymentStopsOnInvalidForm() {
        runOnFxThread(() -> {
            SessionManager.setCurrentUser(new com.hotelbooking.entity.User(1, "alice", "a@b.com", "pw"));
            invokeMethod(controller, "handlePayment", new Class<?>[]{});
        });
        runOnFxThread(() -> assertEquals("请填写所有必填字段", errorLabel.getText()));
        assertFalse(btnPayment.isDisabled());
    }
}

package com.hotelbooking.controller;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.User;
import com.hotelbooking.test.JavaFxTestSupport;
import com.hotelbooking.util.SessionManager;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MyBookingsController 的列表展示、状态徽章与卡片构建逻辑测试。
 */
class MyBookingsControllerTest {

    private MyBookingsController controller;

    private VBox bookingListContainer;
    private VBox emptyStateContainer;
    private Label lblBookingCount;
    private ComboBox<String> cbStatus;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new MyBookingsController();
        runOnFxThread(() -> {
            bookingListContainer = new VBox();
            emptyStateContainer = new VBox();
            lblBookingCount = new Label();
            cbStatus = new ComboBox<>();
            setField(controller, "bookingListContainer", bookingListContainer);
            setField(controller, "emptyStateContainer", emptyStateContainer);
            setField(controller, "lblBookingCount", lblBookingCount);
            setField(controller, "cbStatus", cbStatus);
            setField(controller, "btnLogin", new Button("Sign In"));
        });
    }

    @AfterEach
    void tearDown() {
        SessionManager.logout();
    }

    private Booking booking(int id, String status) {
        Booking booking = new Booking(5, 2, 9, LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 3), new BigDecimal("250.00"), status);
        booking.setId(id);
        return booking;
    }

    @Test
    void setupStatusFilterPopulatesOptions() {
        runOnFxThread(() -> {
            invokeMethod(controller, "setupStatusFilter", new Class<?>[]{});
            assertEquals(5, cbStatus.getItems().size());
            assertTrue(cbStatus.getItems().contains("CANCELLED"));
            assertEquals("All", cbStatus.getValue());
        });
    }

    @Test
    void displayBookingsShowsEmptyState() {
        runOnFxThread(() -> {
            invokeMethod(controller, "displayBookings", new Class<?>[]{List.class}, Collections.emptyList());

            assertFalse(bookingListContainer.isVisible());
            assertTrue(emptyStateContainer.isVisible());
            assertEquals("Total bookings: 0", lblBookingCount.getText());
            assertTrue(bookingListContainer.getChildren().isEmpty());
        });
    }

    @Test
    void displayBookingsCreatesCardPerBooking() {
        runOnFxThread(() -> {
            invokeMethod(controller, "displayBookings", new Class<?>[]{List.class},
                    List.of(booking(1, "CONFIRMED"), booking(2, "PENDING")));

            assertTrue(bookingListContainer.isVisible());
            assertFalse(emptyStateContainer.isVisible());
            assertEquals("Total bookings: 2", lblBookingCount.getText());
            assertEquals(2, bookingListContainer.getChildren().size());
        });
    }

    @Test
    void createStatusBadgeUsesStatusColors() {
        runOnFxThread(() -> {
            Label confirmed = (Label) invokeMethod(controller, "createStatusBadge",
                    new Class<?>[]{String.class}, "CONFIRMED");
            Label pending = (Label) invokeMethod(controller, "createStatusBadge",
                    new Class<?>[]{String.class}, "PENDING");
            Label cancelled = (Label) invokeMethod(controller, "createStatusBadge",
                    new Class<?>[]{String.class}, "CANCELLED");
            Label completed = (Label) invokeMethod(controller, "createStatusBadge",
                    new Class<?>[]{String.class}, "COMPLETED");
            Label unknown = (Label) invokeMethod(controller, "createStatusBadge",
                    new Class<?>[]{String.class}, "OTHER");

            assertEquals("CONFIRMED", confirmed.getText());
            assertTrue(confirmed.getStyle().contains("#4CAF50"));
            assertTrue(pending.getStyle().contains("#FF9800"));
            assertTrue(cancelled.getStyle().contains("#F44336"));
            assertTrue(completed.getStyle().contains("#2196F3"));
            assertTrue(unknown.getStyle().contains("#999"));
        });
    }

    @Test
    void createBookingDetailsContainsAllFields() {
        runOnFxThread(() -> {
            VBox details = (VBox) invokeMethod(controller, "createBookingDetails",
                    new Class<?>[]{Booking.class}, booking(3, "CONFIRMED"));

            assertEquals(3, details.getChildren().size());
            HBox dates = (HBox) details.getChildren().get(0);
            assertEquals(2, dates.getChildren().size());
            assertEquals("✓ Check-in: 2026-12-01", ((Label) dates.getChildren().get(0)).getText());
            assertEquals("✓ Check-out: 2026-12-03", ((Label) dates.getChildren().get(1)).getText());

            HBox info = (HBox) details.getChildren().get(1);
            assertEquals("🛏️ Room ID: 9", ((Label) info.getChildren().get(0)).getText());
            assertEquals("🏨 Hotel ID: 2", ((Label) info.getChildren().get(1)).getText());

            assertEquals("💰 Total Price: $250.00", ((Label) details.getChildren().get(2)).getText());
        });
    }

    @Test
    void createBookingCardHasHeaderDetailsAndActions() {
        runOnFxThread(() -> {
            VBox card = (VBox) invokeMethod(controller, "createBookingCard",
                    new Class<?>[]{Booking.class}, booking(4, "CONFIRMED"));

            assertEquals(4, card.getChildren().size());
            HBox header = (HBox) card.getChildren().get(0);
            assertEquals("Booking #4", ((Label) header.getChildren().get(0)).getText());
            assertEquals("CONFIRMED", ((Label) header.getChildren().get(1)).getText());
        });
    }

    @Test
    void actionButtonsEnabledForActiveBooking() {
        runOnFxThread(() -> {
            HBox actions = (HBox) invokeMethod(controller, "createActionButtons",
                    new Class<?>[]{Booking.class}, booking(5, "PENDING"));

            Button complete = (Button) actions.getChildren().get(1);
            Button cancel = (Button) actions.getChildren().get(2);
            assertFalse(complete.isDisabled());
            assertFalse(cancel.isDisabled());
        });
    }

    @Test
    void actionButtonsDisabledForCancelledAndCompletedBookings() {
        runOnFxThread(() -> {
            HBox cancelledActions = (HBox) invokeMethod(controller, "createActionButtons",
                    new Class<?>[]{Booking.class}, booking(6, "CANCELLED"));
            Button cancelledComplete = (Button) cancelledActions.getChildren().get(1);
            Button cancelledCancel = (Button) cancelledActions.getChildren().get(2);
            assertTrue(cancelledComplete.isDisabled());
            assertTrue(cancelledCancel.isDisabled());

            HBox completedActions = (HBox) invokeMethod(controller, "createActionButtons",
                    new Class<?>[]{Booking.class}, booking(7, "COMPLETED"));
            Button completedComplete = (Button) completedActions.getChildren().get(1);
            Button completedCancel = (Button) completedActions.getChildren().get(2);
            assertTrue(completedComplete.isDisabled());
            assertTrue(completedCancel.isDisabled());
        });
    }

    @Test
    void getCurrentUserIdReflectsSession() {
        runOnFxThread(() -> {
            // 显式清空会话，避免受其他测试类设置的静态状态影响（JUnit 执行顺序不固定）
            SessionManager.logout();
            assertNull(invokeMethod(controller, "getCurrentUserId", new Class<?>[]{}));

            SessionManager.setCurrentUser(new User(42, "bob", "bob@example.com", "pw"));
            assertEquals(42, invokeMethod(controller, "getCurrentUserId", new Class<?>[]{}));
        });
    }
}

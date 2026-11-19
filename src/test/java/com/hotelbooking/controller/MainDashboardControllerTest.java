package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.test.JavaFxTestSupport;
import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MainDashboardController 的 UI 逻辑测试（不加载 FXML、不显示窗口）。
 */
class MainDashboardControllerTest {

    private MainDashboardController controller;

    private Label lblWelcome;
    private Button btnLogin;
    private DatePicker checkInDate;
    private DatePicker checkOutDate;
    private Label lblRoomsGuestsDisplay;
    private Label lblArrow;
    private StackPane selectorPanel;
    private Button btnRoomMinus;
    private Button btnRoomPlus;
    private Button btnAdultMinus;
    private Button btnAdultPlus;
    private Button btnChildMinus;
    private Button btnChildPlus;
    private Label lblRoomCount;
    private Label lblAdultCount;
    private Label lblChildCount;
    private VBox childrenAgeContainer;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new MainDashboardController();
        runOnFxThread(() -> {
            lblWelcome = new Label();
            btnLogin = new Button();
            checkInDate = new DatePicker();
            checkOutDate = new DatePicker();
            lblRoomsGuestsDisplay = new Label();
            lblArrow = new Label();
            selectorPanel = new StackPane();
            selectorPanel.setVisible(true);
            btnRoomMinus = new Button("-");
            btnRoomPlus = new Button("+");
            btnAdultMinus = new Button("-");
            btnAdultPlus = new Button("+");
            btnChildMinus = new Button("-");
            btnChildPlus = new Button("+");
            lblRoomCount = new Label();
            lblAdultCount = new Label();
            lblChildCount = new Label();
            childrenAgeContainer = new VBox();

            setField(controller, "lblWelcome", lblWelcome);
            setField(controller, "btnHelp", new Button("Help"));
            setField(controller, "btnTrips", new Button("Trips"));
            setField(controller, "btnLogin", btnLogin);
            setField(controller, "txtDestination", new TextField());
            setField(controller, "checkInDate", checkInDate);
            setField(controller, "checkOutDate", checkOutDate);
            setField(controller, "btnRoomsGuests", new Button("Rooms"));
            setField(controller, "lblRoomsGuestsDisplay", lblRoomsGuestsDisplay);
            setField(controller, "lblArrow", lblArrow);
            setField(controller, "selectorPanel", selectorPanel);
            setField(controller, "btnRoomMinus", btnRoomMinus);
            setField(controller, "btnRoomPlus", btnRoomPlus);
            setField(controller, "lblRoomCount", lblRoomCount);
            setField(controller, "btnAdultMinus", btnAdultMinus);
            setField(controller, "btnAdultPlus", btnAdultPlus);
            setField(controller, "lblAdultCount", lblAdultCount);
            setField(controller, "btnChildMinus", btnChildMinus);
            setField(controller, "btnChildPlus", btnChildPlus);
            setField(controller, "lblChildCount", lblChildCount);
            setField(controller, "childrenAgeContainer", childrenAgeContainer);

            resetNavigationHistory();
        });
    }

    @AfterEach
    void tearDown() {
        SessionManager.logout();
        runOnFxThread(MainDashboardControllerTest::resetNavigationHistory);
    }

    private static void resetNavigationHistory() {
        try {
            java.lang.reflect.Field instanceField = NavigationManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception ignored) {
            // 忽略反射失败
        }
        NavigationManager.getInstance().clear();
    }

    @Test
    void initializeSetsDefaultValuesAndDates() {
        runOnFxThread(() -> {
            controller.initialize();

            assertEquals(LocalDate.now().plusDays(1), checkInDate.getValue());
            assertEquals(LocalDate.now().plusDays(2), checkOutDate.getValue());
            assertEquals("Travel Like You Mean It", lblWelcome.getText());
            assertEquals("👤 Sign In", btnLogin.getText());
            assertEquals("1 Room, 1 Guest", lblRoomsGuestsDisplay.getText());
            assertEquals("1", lblRoomCount.getText());
            assertEquals("1", lblAdultCount.getText());
            assertEquals("0", lblChildCount.getText());
            assertTrue(btnRoomMinus.isDisabled());
            assertTrue(btnAdultMinus.isDisabled());
            assertTrue(btnChildMinus.isDisabled());
            assertEquals(0, childrenAgeContainer.getChildren().size());
        });
    }

    @Test
    void initializeShowsLoggedInUser() {
        runOnFxThread(() -> {
            User user = new User(7, "alice", "alice@example.com", "pw");
            SessionManager.setCurrentUser(user);

            controller.initialize();

            assertEquals("Welcome back，alice！", lblWelcome.getText());
            assertEquals("👤 alice", btnLogin.getText());
        });
    }

    @Test
    void checkInDateListenerPushesCheckOutDate() {
        runOnFxThread(() -> {
            controller.initialize();

            LocalDate newCheckIn = LocalDate.now().plusDays(10);
            checkInDate.setValue(newCheckIn);

            assertEquals(newCheckIn.plusDays(1), checkOutDate.getValue(),
                    "退房日期应被自动调整到入住日期之后");
        });
    }

    @Test
    void initializeDatePickersSetsConverters() {
        runOnFxThread(() -> {
            controller.initializeDatePickers();

            assertEquals(LocalDate.now().plusDays(1), checkInDate.getValue());
            assertEquals(LocalDate.now().plusDays(2), checkOutDate.getValue());
            assertNotNull(checkInDate.getConverter());
            assertNotNull(checkOutDate.getConverter());
        });
    }

    @Test
    void toggleRoomsSelectorSwitchesVisibility() {
        runOnFxThread(() -> {
            controller.initialize();

            invokeMethod(controller, "toggleRoomsSelector", new Class<?>[]{});
            assertFalse(selectorPanel.isVisible());
            assertFalse(selectorPanel.isManaged());
            assertEquals("▼", lblArrow.getText());

            invokeMethod(controller, "toggleRoomsSelector", new Class<?>[]{});
            assertTrue(selectorPanel.isVisible());
            assertEquals("▲", lblArrow.getText());
        });
    }

    @Test
    void roomCounterRespectsBounds() {
        runOnFxThread(() -> {
            controller.initialize();

            for (int i = 0; i < 15; i++) {
                invokeMethod(controller, "increaseRooms", new Class<?>[]{});
            }
            assertEquals(9, controller.getRoomCount());
            assertTrue(btnRoomPlus.isDisabled());

            for (int i = 0; i < 15; i++) {
                invokeMethod(controller, "decreaseRooms", new Class<?>[]{});
            }
            assertEquals(1, controller.getRoomCount());
            assertTrue(btnRoomMinus.isDisabled());
        });
    }

    @Test
    void adultCounterRespectsRoomCapacity() {
        runOnFxThread(() -> {
            controller.initialize();

            for (int i = 0; i < 20; i++) {
                invokeMethod(controller, "increaseAdults", new Class<?>[]{});
            }
            assertEquals(8, controller.getAdultCount());
            assertTrue(btnAdultPlus.isDisabled());

            while (controller.getAdultCount() > 1) {
                invokeMethod(controller, "decreaseAdults", new Class<?>[]{});
            }
            assertEquals(1, controller.getAdultCount());
            assertTrue(btnAdultMinus.isDisabled());
        });
    }

    @Test
    void childCounterUpdatesAgesAndSelectors() {
        runOnFxThread(() -> {
            controller.initialize();

            invokeMethod(controller, "increaseChildren", new Class<?>[]{});
            assertEquals(1, controller.getChildCount());
            assertEquals(1, controller.getChildrenAges().size());
            assertEquals(1, childrenAgeContainer.getChildren().size());
            assertEquals("1 Room, 2 Guests", lblRoomsGuestsDisplay.getText());

            invokeMethod(controller, "decreaseChildren", new Class<?>[]{});
            assertEquals(0, controller.getChildCount());
            assertTrue(controller.getChildrenAges().isEmpty());
            assertEquals(0, childrenAgeContainer.getChildren().size());
        });
    }

    @Test
    void childCounterStopsAtCapacity() {
        runOnFxThread(() -> {
            controller.initialize();
            setField(controller, "adultCount", 8);
            setField(controller, "childCount", 0);
            setField(controller, "childrenAges", new ArrayList<Integer>());
            invokeMethod(controller, "updateButtons", new Class<?>[]{});

            invokeMethod(controller, "increaseChildren", new Class<?>[]{});
            assertEquals(0, controller.getChildCount());
            assertTrue(btnChildPlus.isDisabled());
        });
    }

    @Test
    void resetRestoresDefaults() {
        runOnFxThread(() -> {
            controller.initialize();
            invokeMethod(controller, "increaseRooms", new Class<?>[]{});
            invokeMethod(controller, "increaseAdults", new Class<?>[]{});
            invokeMethod(controller, "increaseChildren", new Class<?>[]{});

            invokeMethod(controller, "handleReset", new Class<?>[]{});

            assertEquals(1, controller.getRoomCount());
            assertEquals(1, controller.getAdultCount());
            assertEquals(0, controller.getChildCount());
            assertTrue(controller.getChildrenAges().isEmpty());
            assertEquals("1 Room, 1 Guest", lblRoomsGuestsDisplay.getText());
        });
    }

    @Test
    void doneClosesSelector() {
        runOnFxThread(() -> {
            controller.initialize();
            invokeMethod(controller, "handleDone", new Class<?>[]{});
            assertFalse(selectorPanel.isVisible());
        });
    }

    @Test
    void childrenAgesGetterReturnsCopy() {
        runOnFxThread(() -> {
            controller.initialize();
            invokeMethod(controller, "increaseChildren", new Class<?>[]{});
            int size = controller.getChildrenAges().size();
            controller.getChildrenAges().clear();
            assertEquals(size, controller.getChildrenAges().size());
        });
    }
}

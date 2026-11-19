package com.hotelbooking.controller;

import com.hotelbooking.test.JavaFxTestSupport;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RoomsGuestsSelectorController 的 UI 逻辑单元测试。
 * 所有控件均在 FX 线程上创建，不创建真实窗口。
 */
class RoomsGuestsSelectorControllerTest {

    private RoomsGuestsSelectorController controller;

    private Button btnRoomMinus;
    private Button btnRoomPlus;
    private Button btnAdultMinus;
    private Button btnAdultPlus;
    private Button btnChildMinus;
    private Button btnChildPlus;
    private Label lblRoomCount;
    private Label lblAdultCount;
    private Label lblChildCount;
    private Label lblRoomsGuestsDisplay;
    private Label lblArrow;
    private VBox selectorPanel;
    private VBox childrenAgeContainer;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new RoomsGuestsSelectorController();
        runOnFxThread(() -> {
            btnRoomMinus = new Button("-");
            btnRoomPlus = new Button("+");
            btnAdultMinus = new Button("-");
            btnAdultPlus = new Button("+");
            btnChildMinus = new Button("-");
            btnChildPlus = new Button("+");
            lblRoomCount = new Label();
            lblAdultCount = new Label();
            lblChildCount = new Label();
            lblRoomsGuestsDisplay = new Label();
            lblArrow = new Label();
            selectorPanel = new VBox();
            childrenAgeContainer = new VBox();
            selectorPanel.setVisible(true);

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
        });
    }

    @Test
    void initializeDisplaysDefaultSelection() {
        runOnFxThread(() -> {
            controller.initialize();

            assertEquals("2", lblRoomCount.getText());
            assertEquals("3", lblAdultCount.getText());
            assertEquals("2", lblChildCount.getText());
            assertEquals("2 Rooms, 5 Guests", lblRoomsGuestsDisplay.getText());
            assertEquals(2, childrenAgeContainer.getChildren().size());
            assertEquals(2, controller.getChildrenAges().size());
            assertFalse(btnRoomMinus.isDisabled());
            assertFalse(btnRoomPlus.isDisabled());
        });
    }

    @Test
    void toggleSelectorSwitchesVisibilityAndArrow() {
        runOnFxThread(() -> {
            controller.initialize();

            invokeMethod(controller, "toggleSelector", new Class<?>[]{});
            assertFalse(selectorPanel.isVisible());
            assertFalse(selectorPanel.isManaged());
            assertEquals("▼", lblArrow.getText());

            invokeMethod(controller, "toggleSelector", new Class<?>[]{});
            assertTrue(selectorPanel.isVisible());
            assertTrue(selectorPanel.isManaged());
            assertEquals("▲", lblArrow.getText());
        });
    }

    @Test
    void increaseAndDecreaseRoomsRespectLimits() {
        runOnFxThread(() -> {
            controller.initialize();

            for (int i = 0; i < 20; i++) {
                invokeMethod(controller, "increaseRooms", new Class<?>[]{});
            }
            assertEquals(9, controller.getRoomCount());
            assertTrue(btnRoomPlus.isDisabled());

            for (int i = 0; i < 20; i++) {
                invokeMethod(controller, "decreaseRooms", new Class<?>[]{});
            }
            assertEquals(1, controller.getRoomCount());
            assertTrue(btnRoomMinus.isDisabled());
            assertEquals("1", lblRoomCount.getText());
        });
    }

    @Test
    void increaseAndDecreaseAdultsRespectLimits() {
        runOnFxThread(() -> {
            controller.initialize();

            invokeMethod(controller, "increaseAdults", new Class<?>[]{});
            assertEquals(4, controller.getAdultCount());

            while (controller.getAdultCount() > 1) {
                invokeMethod(controller, "decreaseAdults", new Class<?>[]{});
            }
            assertEquals(1, controller.getAdultCount());
            assertTrue(btnAdultMinus.isDisabled());
        });
    }

    @Test
    void adultsCannotExceedRoomCapacity() {
        runOnFxThread(() -> {
            controller.initialize();
            setField(controller, "roomCount", 1);
            setField(controller, "adultCount", 8);
            setField(controller, "childCount", 0);
            setField(controller, "childrenAges", new java.util.ArrayList<Integer>());

            invokeMethod(controller, "increaseAdults", new Class<?>[]{});
            assertEquals(8, controller.getAdultCount());
            invokeMethod(controller, "updateButtons", new Class<?>[]{});
            assertTrue(btnAdultPlus.isDisabled());
            assertTrue(btnChildPlus.isDisabled());
        });
    }

    @Test
    void increaseAndDecreaseChildrenUpdatesAgeSelectors() {
        runOnFxThread(() -> {
            controller.initialize();
            int initial = controller.getChildCount();

            invokeMethod(controller, "increaseChildren", new Class<?>[]{});
            assertEquals(initial + 1, controller.getChildCount());
            assertEquals(initial + 1, childrenAgeContainer.getChildren().size());
            assertEquals(initial + 1, controller.getChildrenAges().size());

            invokeMethod(controller, "decreaseChildren", new Class<?>[]{});
            assertEquals(initial, controller.getChildCount());
            assertEquals(initial, childrenAgeContainer.getChildren().size());
        });
    }

    @Test
    void childrenCannotExceedCapacity() {
        runOnFxThread(() -> {
            controller.initialize();
            setField(controller, "roomCount", 1);
            setField(controller, "adultCount", 8);
            setField(controller, "childCount", 0);
            setField(controller, "childrenAges", new java.util.ArrayList<Integer>());

            invokeMethod(controller, "increaseChildren", new Class<?>[]{});
            assertEquals(0, controller.getChildCount());
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
            assertEquals(0, childrenAgeContainer.getChildren().size());
        });
    }

    @Test
    void doneClosesSelector() {
        runOnFxThread(() -> {
            controller.initialize();
            invokeMethod(controller, "handleDone", new Class<?>[]{});
            assertFalse(selectorPanel.isVisible());
            assertEquals("▼", lblArrow.getText());
        });
    }

    @Test
    void gettersReturnCopiesOfChildrenAges() {
        runOnFxThread(() -> controller_initializeAndReturnAges());
    }

    private int controller_initializeAndReturnAges() {
        controller.initialize();
        int size = controller.getChildrenAges().size();
        controller.getChildrenAges().clear();
        assertEquals(size, controller.getChildrenAges().size(), "返回的年龄列表应为副本");
        return size;
    }
}

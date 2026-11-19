package com.hotelbooking.controller;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.test.JavaFxTestSupport;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

/**
 * HotelRoomsController 的酒店信息展示与房间卡片构建测试。
 */
class HotelRoomsControllerTest {

    private HotelRoomsController controller;

    private Label lblHotelName;
    private Label lblHotelAddress;
    private Label lblHotelDescription;
    private Label lblHotelAmenities;
    private Label lblRoomCount;
    private VBox roomListContainer;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new HotelRoomsController();
        runOnFxThread(() -> {
            lblHotelName = new Label();
            lblHotelAddress = new Label();
            lblHotelDescription = new Label();
            lblHotelAmenities = new Label();
            lblRoomCount = new Label();
            roomListContainer = new VBox();
            setField(controller, "lblHotelName", lblHotelName);
            setField(controller, "lblHotelAddress", lblHotelAddress);
            setField(controller, "lblHotelDescription", lblHotelDescription);
            setField(controller, "lblHotelAmenities", lblHotelAmenities);
            setField(controller, "lblRoomCount", lblRoomCount);
            setField(controller, "roomListContainer", roomListContainer);
        });
    }

    private Hotel hotel() {
        Hotel hotel = new Hotel("Grand Hotel", "Shanghai", "A nice place", "WiFi, Pool", 5);
        hotel.setId(8);
        return hotel;
    }

    private Room room(boolean available) {
        return new Room(8, "801", "Deluxe", 199.5, 3, available, "Deluxe room");
    }

    @Test
    void displayHotelInfoFillsLabels() {
        runOnFxThread(() -> {
            setField(controller, "currentHotel", hotel());
            invokeMethod(controller, "displayHotelInfo", new Class<?>[]{});

            assertEquals("Grand Hotel", lblHotelName.getText());
            assertEquals("Shanghai", lblHotelAddress.getText());
            assertEquals("A nice place", lblHotelDescription.getText());
            assertEquals("WiFi, Pool", lblHotelAmenities.getText());
        });
    }

    @Test
    void displayHotelInfoWithoutHotelDoesNothing() {
        runOnFxThread(() -> {
            assertDoesNotThrow(() -> invokeMethod(controller, "displayHotelInfo", new Class<?>[]{}));
            assertEquals("", lblHotelName.getText());
        });
    }

    @Test
    void createRoomCardForAvailableRoom() {
        runOnFxThread(() -> {
            VBox card = (VBox) invokeMethod(controller, "createRoomCard",
                    new Class<?>[]{Room.class}, room(true));

            assertEquals(4, card.getChildren().size());

            HBox bottom = (HBox) card.getChildren().get(3);
            Button bookButton = (Button) bottom.getChildren().get(2);
            assertEquals("Book Now", bookButton.getText());
            assertFalse(bookButton.isDisabled());
            assertTrue(bookButton.getStyle().contains("#8B4513"));

            bookButton.getOnMouseEntered().handle(null);
            assertTrue(bookButton.getStyle().contains("#6d3410"));
            bookButton.getOnMouseExited().handle(null);
            assertTrue(bookButton.getStyle().contains("#8B4513"));
        });
    }

    @Test
    void createRoomCardForUnavailableRoom() {
        runOnFxThread(() -> {
            VBox card = (VBox) invokeMethod(controller, "createRoomCard",
                    new Class<?>[]{Room.class}, room(false));

            HBox bottom = (HBox) card.getChildren().get(3);
            Button bookButton = (Button) bottom.getChildren().get(2);
            assertEquals("Unavailable", bookButton.getText());
            assertTrue(bookButton.isDisabled());
        });
    }

    @Test
    void displayRoomsShowsEmptyMessage() {
        runOnFxThread(() -> {
            setField(controller, "currentHotel", hotel());
            try (MockedConstruction<HotelService> ignored = mockConstruction(HotelService.class,
                    (mock, context) -> when(mock.getRoomsByHotelId(anyInt())).thenReturn(List.of()))) {
                invokeMethod(controller, "displayRooms", new Class<?>[]{});
            }

            assertEquals("(0 rooms)", lblRoomCount.getText());
            assertEquals(1, roomListContainer.getChildren().size());
            assertEquals("No rooms available at the moment.",
                    ((Label) roomListContainer.getChildren().get(0)).getText());
        });
    }

    @Test
    void displayRoomsCreatesCardForEachRoom() {
        runOnFxThread(() -> {
            setField(controller, "currentHotel", hotel());
            List<Room> rooms = List.of(room(true), room(false));
            try (MockedConstruction<HotelService> ignored = mockConstruction(HotelService.class,
                    (mock, context) -> when(mock.getRoomsByHotelId(anyInt())).thenReturn(rooms))) {
                invokeMethod(controller, "displayRooms", new Class<?>[]{});
            }

            assertEquals("(2 rooms)", lblRoomCount.getText());
            assertEquals(2, roomListContainer.getChildren().size());
        });
    }

    @Test
    void displayRoomsUsesSingularRoomText() {
        runOnFxThread(() -> {
            setField(controller, "currentHotel", hotel());
            try (MockedConstruction<HotelService> ignored = mockConstruction(HotelService.class,
                    (mock, context) -> when(mock.getRoomsByHotelId(anyInt())).thenReturn(List.of(room(true))))) {
                invokeMethod(controller, "displayRooms", new Class<?>[]{});
            }

            assertEquals("(1 room)", lblRoomCount.getText());
        });
    }

    @Test
    void displayRoomsWithoutHotelDoesNothing() {
        runOnFxThread(() -> {
            assertDoesNotThrow(() -> invokeMethod(controller, "displayRooms", new Class<?>[]{}));
            assertEquals("", lblRoomCount.getText());
        });
    }
}

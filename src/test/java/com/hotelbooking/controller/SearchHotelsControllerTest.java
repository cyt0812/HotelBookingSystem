package com.hotelbooking.controller;

import com.hotelbooking.entity.Hotel;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.test.JavaFxTestSupport;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SearchHotelsController 的搜索结果展示与卡片构建测试。
 */
class SearchHotelsControllerTest {

    private SearchHotelsController controller;
    private HotelService hotelService;

    private TextField txtSearch;
    private Label lblResultCount;
    private VBox hotelListContainer;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new SearchHotelsController();
        hotelService = mock(HotelService.class);
        runOnFxThread(() -> {
            txtSearch = new TextField();
            lblResultCount = new Label();
            hotelListContainer = new VBox();
            setField(controller, "txtSearch", txtSearch);
            setField(controller, "lblResultCount", lblResultCount);
            setField(controller, "hotelListContainer", hotelListContainer);
            setField(controller, "btnLogin", new Button("Sign In"));
            setField(controller, "hotelService", hotelService);
        });
    }

    private Hotel hotel(String name, String address) {
        Hotel hotel = new Hotel(name, address, "Description of " + name, "WiFi", 3);
        hotel.setId(1);
        return hotel;
    }

    @Test
    void displayHotelsShowsEmptyMessage() {
        runOnFxThread(() -> {
            invokeMethod(controller, "displayHotels", new Class<?>[]{List.class}, Collections.emptyList());

            assertEquals("Found 0 hotels", lblResultCount.getText());
            assertEquals(1, hotelListContainer.getChildren().size());
            assertEquals("No hotels found. Try a different search.",
                    ((Label) hotelListContainer.getChildren().get(0)).getText());
        });
    }

    @Test
    void displayHotelsCreatesCardForEachResult() {
        runOnFxThread(() -> {
            invokeMethod(controller, "displayHotels", new Class<?>[]{List.class},
                    List.of(hotel("A", "X"), hotel("B", "Y")));

            assertEquals("Found 2 hotels", lblResultCount.getText());
            assertEquals(2, hotelListContainer.getChildren().size());
        });
    }

    @Test
    void displayHotelsUsesSingularText() {
        runOnFxThread(() -> {
            invokeMethod(controller, "displayHotels", new Class<?>[]{List.class},
                    List.of(hotel("A", "X")));
            assertEquals("Found 1 hotel", lblResultCount.getText());
        });
    }

    @Test
    void createHotelCardContainsHotelInformation() {
        runOnFxThread(() -> {
            VBox card = (VBox) invokeMethod(controller, "createHotelCard",
                    new Class<?>[]{Hotel.class}, hotel("Grand", "Shanghai"));

            assertEquals(6, card.getChildren().size());
            assertEquals("Grand", ((Label) card.getChildren().get(0)).getText());

            HBox addressBox = (HBox) card.getChildren().get(1);
            assertEquals("Shanghai", ((Label) addressBox.getChildren().get(1)).getText());

            HBox bottom = (HBox) card.getChildren().get(5);
            Button viewRooms = (Button) bottom.getChildren().get(2);
            assertEquals("View Rooms", viewRooms.getText());

            // 触发卡片悬停效果
            card.getOnMouseEntered().handle(null);
            assertTrue(card.getStyle().contains("rgba(0,0,0,0.2)"));
            card.getOnMouseExited().handle(null);
            assertTrue(card.getStyle().contains("rgba(0,0,0,0.1)"));
        });
    }

    @Test
    void setSearchKeywordWithNullShowsAllHotels() {
        when(hotelService.getAllHotels()).thenReturn(List.of(hotel("A", "X")));
        runOnFxThread(() -> {
            controller.setSearchKeyword(null);
            assertEquals("Found 1 hotel", lblResultCount.getText());
            verify(hotelService).getAllHotels();
        });
    }

    @Test
    void setSearchKeywordWithEmptyShowsAllHotels() {
        when(hotelService.getAllHotels()).thenReturn(List.of());
        runOnFxThread(() -> {
            controller.setSearchKeyword("");
            assertEquals("Found 0 hotels", lblResultCount.getText());
            verify(hotelService).getAllHotels();
        });
    }

    @Test
    void setSearchKeywordSearchesHotels() {
        when(hotelService.searchHotels("beach")).thenReturn(List.of(hotel("Beach", "Sanya")));
        runOnFxThread(() -> {
            controller.setSearchKeyword("beach");
            assertEquals("Found 1 hotel", lblResultCount.getText());
            verify(hotelService).searchHotels("beach");
        });
    }

    @Test
    void handleSearchUsesEnteredKeyword() {
        when(hotelService.searchHotels("city")).thenReturn(List.of(hotel("City", "Beijing")));
        runOnFxThread(() -> {
            txtSearch.setText("  city  ");
            invokeMethod(controller, "handleSearch", new Class<?>[]{});
            assertEquals("Found 1 hotel", lblResultCount.getText());
            verify(hotelService).searchHotels("city");
        });
    }
}

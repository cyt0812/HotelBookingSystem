package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.test.JavaFxTestSupport;
import com.hotelbooking.util.NavigationManager;
import com.hotelbooking.util.SessionManager;
import javafx.scene.control.Button;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TopNavigationController 的初始化、按钮状态与悬停样式测试。
 */
class TopNavigationControllerTest {

    private TopNavigationController controller;

    private Button btnHelp;
    private Button btnTrips;
    private Button btnLogin;
    private Button btnBack;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new TopNavigationController();
        runOnFxThread(() -> {
            btnHelp = new Button("Help");
            btnTrips = new Button("Trips");
            btnLogin = new Button("Login");
            btnBack = new Button("Back");
            setField(controller, "btnHelp", btnHelp);
            setField(controller, "btnTrips", btnTrips);
            setField(controller, "btnLogin", btnLogin);
            setField(controller, "btnBack", btnBack);
            resetNavigationHistory();
        });
    }

    @AfterEach
    void tearDown() {
        SessionManager.logout();
        runOnFxThread(TopNavigationControllerTest::resetNavigationHistory);
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
    void initializeWhenLoggedOutDisablesBackButton() {
        runOnFxThread(() -> {
            controller.initialize();

            assertEquals("👤 Sign In", btnLogin.getText());
            assertTrue(btnBack.isDisabled());
            assertTrue(btnBack.getStyle().contains("#e0e0e0"));
        });
    }

    @Test
    void initializeWhenLoggedInShowsUsernameAndEnablesBack() {
        runOnFxThread(() -> {
            SessionManager.setCurrentUser(new User(1, "alice", "alice@example.com", "pw"));
            NavigationManager.getInstance().push("/com/hotelbooking/view/main_dashboard.fxml", "Home");
            NavigationManager.getInstance().push("/com/hotelbooking/view/login.fxml", "Login");

            controller.initialize();

            assertEquals("👤 alice", btnLogin.getText());
            assertFalse(btnBack.isDisabled());
            assertTrue(btnBack.getStyle().contains("#f5f5f5"));
        });
    }

    @Test
    void hoverHandlersChangeButtonStyles() {
        runOnFxThread(() -> {
            NavigationManager.getInstance().push("/com/hotelbooking/view/main_dashboard.fxml", "Home");
            NavigationManager.getInstance().push("/com/hotelbooking/view/login.fxml", "Login");
            controller.initialize();

            btnBack.getOnMouseEntered().handle(null);
            assertTrue(btnBack.getStyle().contains("#ddd"));
            btnBack.getOnMouseExited().handle(null);
            assertTrue(btnBack.getStyle().contains("#f5f5f5"));

            btnHelp.getOnMouseEntered().handle(null);
            assertTrue(btnHelp.getStyle().contains("#f5f5f5"));
            btnHelp.getOnMouseExited().handle(null);
            assertTrue(btnHelp.getStyle().contains("transparent"));

            btnTrips.getOnMouseEntered().handle(null);
            assertTrue(btnTrips.getStyle().contains("#f5f5f5"));
            btnTrips.getOnMouseExited().handle(null);
            assertTrue(btnTrips.getStyle().contains("transparent"));

            btnLogin.getOnMouseEntered().handle(null);
            assertTrue(btnLogin.getStyle().contains("#8B4513"));
            btnLogin.getOnMouseExited().handle(null);
            assertTrue(btnLogin.getStyle().contains("#1a1a1a"));
        });
    }

    @Test
    void goBackWithoutPreviousDoesNothing() {
        runOnFxThread(() -> assertDoesNotThrow(
                () -> invokeMethod(controller, "goBack", new Class<?>[]{})));
    }

    @Test
    void goBackWithPreviousAttemptsToLoadPage() {
        runOnFxThread(() -> {
            NavigationManager.getInstance().push("/com/hotelbooking/view/main_dashboard.fxml", "Home");
            NavigationManager.getInstance().push("/com/hotelbooking/view/login.fxml", "User Login");

            assertDoesNotThrow(() -> invokeMethod(controller, "goBack", new Class<?>[]{}));
        });
    }

    @Test
    void backToHomeAttemptsNavigationWithoutThrowing() {
        runOnFxThread(() -> assertDoesNotThrow(
                () -> invokeMethod(controller, "backToHome", new Class<?>[]{})));
    }

    @Test
    void handleLoginWhenLoggedOutNavigatesToLogin() {
        runOnFxThread(() -> {
            SessionManager.logout();
            assertDoesNotThrow(() -> invokeMethod(controller, "handleLogin", new Class<?>[]{}));
        });
    }

    @Test
    void handleTripsWhenLoggedOutRedirectsToLogin() {
        runOnFxThread(() -> {
            SessionManager.logout();
            assertDoesNotThrow(() -> invokeMethod(controller, "handleTrips", new Class<?>[]{}));
        });
    }

    @Test
    void handleLogoutClearsSessionAndUpdatesLoginButton() {
        runOnFxThread(() -> {
            SessionManager.setCurrentUser(new User(2, "bob", "bob@example.com", "pw"));
            controller.initialize();

            assertDoesNotThrow(() -> invokeMethod(controller, "handleLogout", new Class<?>[]{}));

            assertFalse(SessionManager.isLoggedIn());
            assertEquals("👤 Sign In", btnLogin.getText());
        });
    }
}

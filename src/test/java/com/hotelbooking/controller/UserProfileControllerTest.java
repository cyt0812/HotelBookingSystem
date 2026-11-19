package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.test.JavaFxTestSupport;
import com.hotelbooking.util.SessionManager;
import javafx.scene.control.Label;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UserProfileController 的标签填充与格式化工具方法测试。
 */
class UserProfileControllerTest {

    private UserProfileController controller;

    private Label usernameLabel;
    private Label emailLabel;
    private Label fullNameLabel;
    private Label memberSinceLabel;
    private Label roleLabel;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new UserProfileController();
        runOnFxThread(() -> {
            usernameLabel = new Label();
            emailLabel = new Label();
            fullNameLabel = new Label();
            memberSinceLabel = new Label();
            roleLabel = new Label();
            setField(controller, "usernameLabel", usernameLabel);
            setField(controller, "emailLabel", emailLabel);
            setField(controller, "fullNameLabel", fullNameLabel);
            setField(controller, "memberSinceLabel", memberSinceLabel);
            setField(controller, "roleLabel", roleLabel);
        });
    }

    @AfterEach
    void tearDown() {
        SessionManager.logout();
    }

    @Test
    void initializeFillsProfileLabels() {
        runOnFxThread(() -> {
            User user = new User(5, "alice", "alice@example.com", "pw");
            user.setFullName("Alice Chen");
            user.setRole("ADMIN");
            user.setCreatedAt(LocalDateTime.of(2026, 3, 8, 10, 30));
            SessionManager.setCurrentUser(user);

            controller.initialize();

            assertEquals("alice", usernameLabel.getText());
            assertEquals("alice@example.com", emailLabel.getText());
            assertEquals("Alice Chen", fullNameLabel.getText());
            assertEquals("Admin", roleLabel.getText());
            assertEquals("Mar 08, 2026", memberSinceLabel.getText());
        });
    }

    @Test
    void initializeShowsNotSetWhenFullNameMissing() {
        runOnFxThread(() -> {
            User user = new User(6, "bob", "bob@example.com", "pw");
            user.setFullName("");
            user.setRole("CUSTOMER");
            SessionManager.setCurrentUser(user);

            controller.initialize();

            assertEquals("Not set", fullNameLabel.getText());
            assertEquals("Customer", roleLabel.getText());
        });
    }

    @Test
    void formatDateHandlesNullAndValue() {
        runOnFxThread(() -> {
            assertEquals("Unknown", invokeMethod(controller, "formatDate",
                    new Class<?>[]{LocalDateTime.class}, new Object[]{null}));
            assertEquals("Jan 02, 2025", invokeMethod(controller, "formatDate",
                    new Class<?>[]{LocalDateTime.class}, LocalDateTime.of(2025, 1, 2, 0, 0)));
        });
    }

    @Test
    void capitalizeRoleHandlesNullEmptyAndMixedCase() {
        runOnFxThread(() -> {
            assertEquals("User", invokeMethod(controller, "capitalizeRole",
                    new Class<?>[]{String.class}, new Object[]{null}));
            assertEquals("User", invokeMethod(controller, "capitalizeRole",
                    new Class<?>[]{String.class}, ""));
            assertEquals("Admin", invokeMethod(controller, "capitalizeRole",
                    new Class<?>[]{String.class}, "ADMIN"));
            assertEquals("Customer", invokeMethod(controller, "capitalizeRole",
                    new Class<?>[]{String.class}, "customer"));
        });
    }
}

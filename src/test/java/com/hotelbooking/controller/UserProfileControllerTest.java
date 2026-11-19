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
 * UserProfileController 的资料展示格式化测试。
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
    void initializePopulatesAllLabelsFromCurrentUser() {
        User user = new User("alice", "alice@example.com", "secret1");
        user.setFullName("Alice Chen");
        user.setRole("ADMIN");
        user.setCreatedAt(LocalDateTime.of(2025, 3, 4, 10, 0));
        SessionManager.setCurrentUser(user);

        runOnFxThread(controller::initialize);

        runOnFxThread(() -> {
            assertEquals("alice", usernameLabel.getText());
            assertEquals("alice@example.com", emailLabel.getText());
            assertEquals("Alice Chen", fullNameLabel.getText());
            assertEquals("Admin", roleLabel.getText());
            assertEquals("Mar 04, 2025", memberSinceLabel.getText());
        });
    }

    @Test
    void initializeFallsBackWhenOptionalFieldsAreMissing() {
        User user = new User("bob", "bob@example.com", "secret1");
        user.setFullName(null);
        user.setRole(null);
        user.setCreatedAt(null);
        SessionManager.setCurrentUser(user);

        runOnFxThread(controller::initialize);

        runOnFxThread(() -> {
            assertEquals("Not set", fullNameLabel.getText());
            assertEquals("User", roleLabel.getText());
            assertEquals("Unknown", memberSinceLabel.getText());
        });
    }

    @Test
    void initializeTreatsBlankFullNameAsNotSet() {
        User user = new User("carol", "carol@example.com", "secret1");
        user.setFullName("");
        SessionManager.setCurrentUser(user);

        runOnFxThread(controller::initialize);

        runOnFxThread(() -> assertEquals("Not set", fullNameLabel.getText()));
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

package com.hotelbooking.controller;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import com.hotelbooking.test.JavaFxTestSupport;
import com.hotelbooking.util.SessionManager;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * EditProfileController 的输入校验与保存提示逻辑测试。
 */
class EditProfileControllerTest {

    private EditProfileController controller;

    private TextField usernameField;
    private TextField emailField;
    private TextField fullNameField;
    private Label errorLabel;
    private Label successLabel;

    private User user;
    private UserDAO userDAO;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new EditProfileController();
        user = new User(9, "alice", "alice@example.com", "pw");
        user.setFullName("Alice Chen");
        userDAO = mock(UserDAO.class);

        runOnFxThread(() -> {
            usernameField = new TextField();
            emailField = new TextField();
            fullNameField = new TextField();
            errorLabel = new Label();
            successLabel = new Label();
            setField(controller, "usernameField", usernameField);
            setField(controller, "emailField", emailField);
            setField(controller, "fullNameField", fullNameField);
            setField(controller, "errorLabel", errorLabel);
            setField(controller, "successLabel", successLabel);
            setField(controller, "userDAO", userDAO);
        });
    }

    @AfterEach
    void tearDown() {
        SessionManager.logout();
    }

    private void loadUser() {
        SessionManager.setCurrentUser(user);
        controller.initialize();
        setField(controller, "userDAO", userDAO);
    }

    private String validate() {
        return (String) invokeMethod(controller, "validateInput", new Class<?>[]{});
    }

    @Test
    void initializeLoadsUserIntoFields() {
        runOnFxThread(() -> {
            loadUser();

            assertEquals("alice", usernameField.getText());
            assertEquals("alice@example.com", emailField.getText());
            assertEquals("Alice Chen", fullNameField.getText());
            assertEquals("alice", fieldValue("originalUsername"));
            assertEquals("alice@example.com", fieldValue("originalEmail"));
        });
    }

    private String fieldValue(String name) {
        return (String) JavaFxTestSupport.getField(controller, name);
    }

    @Test
    void initializeWithoutUserShowsError() {
        runOnFxThread(() -> {
            SessionManager.logout();
            controller.initialize();
            assertEquals("No user logged in!", errorLabel.getText());
        });
    }

    @Test
    void validateInputRejectsInvalidUsername() {
        runOnFxThread(() -> {
            loadUser();

            usernameField.setText("");
            assertEquals("Username cannot be empty", validate());

            usernameField.setText("ab");
            assertEquals("Username must be at least 3 characters", validate());

            usernameField.setText("a".repeat(51));
            assertEquals("Username is too long (max 50 characters)", validate());

            usernameField.setText("bad name");
            assertEquals("Username can only contain letters, numbers, and underscores", validate());
        });
    }

    @Test
    void validateInputRejectsInvalidEmail() {
        runOnFxThread(() -> {
            loadUser();

            emailField.setText("");
            assertEquals("Email address cannot be empty", validate());

            emailField.setText("a".repeat(95) + "@example.com");
            assertEquals("Email address is too long (max 100 characters)", validate());

            emailField.setText("not-an-email");
            assertEquals("Please enter a valid email address", validate());
        });
    }

    @Test
    void validateInputRejectsInvalidFullName() {
        runOnFxThread(() -> {
            loadUser();

            fullNameField.setText("");
            assertEquals("Full name cannot be empty", validate());

            fullNameField.setText("A");
            assertEquals("Full name must be at least 2 characters", validate());

            fullNameField.setText("A".repeat(101));
            assertEquals("Full name is too long (max 100 characters)", validate());

            fullNameField.setText("Alice123");
            assertEquals("Full name can only contain letters, spaces, hyphens, and apostrophes", validate());
        });
    }

    @Test
    void validateInputAcceptsValidData() {
        runOnFxThread(() -> {
            loadUser();
            assertEquals("", validate());
        });
    }

    @Test
    void handleSaveRejectsInvalidInput() {
        runOnFxThread(() -> {
            loadUser();
            usernameField.setText("");

            invokeMethod(controller, "handleSave", new Class<?>[]{});
            assertEquals("Username cannot be empty", errorLabel.getText());
        });
    }

    @Test
    void handleSaveRejectsExistingUsername() {
        runOnFxThread(() -> {
            loadUser();
            usernameField.setText("bob");
            when(userDAO.isUsernameExists("bob")).thenReturn(true);

            invokeMethod(controller, "handleSave", new Class<?>[]{});
            assertEquals("❌ This username is already taken", errorLabel.getText());
        });
    }

    @Test
    void handleSaveRejectsExistingEmail() {
        runOnFxThread(() -> {
            loadUser();
            emailField.setText("bob@example.com");
            when(userDAO.isEmailExists("bob@example.com")).thenReturn(true);

            invokeMethod(controller, "handleSave", new Class<?>[]{});
            assertEquals("❌ This email is already registered", errorLabel.getText());
        });
    }

    @Test
    void handleSaveReportsDatabaseFailure() {
        runOnFxThread(() -> {
            loadUser();
            usernameField.setText("alice2");
            when(userDAO.isUsernameExists("alice2")).thenReturn(false);
            when(userDAO.updateUserProfile(anyInt(), anyString(), anyString())).thenReturn(false);

            invokeMethod(controller, "handleSave", new Class<?>[]{});
            assertEquals("Failed to update profile in database", errorLabel.getText());
            assertEquals("", successLabel.getText());
        });
    }

    @Test
    void setOnSaveCallbackStoresHandler() {
        runOnFxThread(() -> {
            EditProfileController.OnSaveCallback callback = () -> {
            };
            controller.setOnSaveCallback(callback);
            assertNotNull(JavaFxTestSupport.getField(controller, "onSaveCallback"));
        });
    }
}

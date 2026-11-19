package com.hotelbooking.controller;

import com.hotelbooking.dao.UserDAO;
import com.hotelbooking.entity.User;
import com.hotelbooking.test.JavaFxTestSupport;
import com.hotelbooking.util.SessionManager;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * ChangePasswordController 的密码校验与提示逻辑测试。
 */
class ChangePasswordControllerTest {

    private ChangePasswordController controller;

    private PasswordField currentPasswordField;
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;
    private ProgressBar passwordStrengthBar;
    private Label strengthLabel;
    private Label req1;
    private Label req2;
    private Label req3;
    private Label req4;
    private Label matchLabel;
    private Label errorLabel;
    private Label successLabel;

    private User user;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new ChangePasswordController();
        user = new User(3, "alice", "alice@example.com", "OldPass1");
        runOnFxThread(() -> {
            currentPasswordField = new PasswordField();
            newPasswordField = new PasswordField();
            confirmPasswordField = new PasswordField();
            passwordStrengthBar = new ProgressBar();
            strengthLabel = new Label();
            req1 = new Label();
            req2 = new Label();
            req3 = new Label();
            req4 = new Label();
            matchLabel = new Label();
            errorLabel = new Label();
            successLabel = new Label();

            setField(controller, "currentPasswordField", currentPasswordField);
            setField(controller, "newPasswordField", newPasswordField);
            setField(controller, "confirmPasswordField", confirmPasswordField);
            setField(controller, "passwordStrengthBar", passwordStrengthBar);
            setField(controller, "strengthLabel", strengthLabel);
            setField(controller, "req1", req1);
            setField(controller, "req2", req2);
            setField(controller, "req3", req3);
            setField(controller, "req4", req4);
            setField(controller, "matchLabel", matchLabel);
            setField(controller, "errorLabel", errorLabel);
            setField(controller, "successLabel", successLabel);
        });
    }

    @AfterEach
    void tearDown() {
        SessionManager.logout();
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
    void initializeWithUserClearsMessagesAndRegistersListeners() {
        runOnFxThread(() -> {
            SessionManager.setCurrentUser(user);
            errorLabel.setText("x");
            successLabel.setText("y");

            controller.initialize();

            assertEquals("", errorLabel.getText());
            assertEquals("", successLabel.getText());
            assertEquals("", matchLabel.getText());
            assertNotNull(newPasswordField.getOnKeyReleased());
            assertNotNull(confirmPasswordField.getOnKeyReleased());
        });
    }

    @Test
    void validateInputRejectsEmptyFields() {
        runOnFxThread(() -> {
            assertEquals("Current password cannot be empty", validate("", "NewPass1", "NewPass1"));
            assertEquals("New password cannot be empty", validate("OldPass1", "", ""));
            assertEquals("Please confirm your new password", validate("OldPass1", "NewPass1", ""));
        });
    }

    @Test
    void validateInputChecksLengthMismatchAndReuse() {
        runOnFxThread(() -> {
            assertEquals("New password must be at least 8 characters",
                    validate("OldPass1", "Ab1", "Ab1"));
            assertEquals("New passwords do not match",
                    validate("OldPass1", "NewPass11", "NewPass22"));
            assertEquals("New password must be different from current password",
                    validate("OldPass1", "OldPass1", "OldPass1"));
        });
    }

    @Test
    void validateInputRejectsWeakPasswordAndAcceptsStrongPassword() {
        runOnFxThread(() -> {
            assertEquals("Password does not meet strength requirements",
                    validate("OldPass1", "abcdefgh", "abcdefgh"));
            assertEquals("", validate("OldPass1", "NewPass11", "NewPass11"));
        });
    }

    private String validate(String current, String newPassword, String confirm) {
        return (String) invokeMethod(controller, "validateInput",
                new Class<?>[]{String.class, String.class, String.class},
                current, newPassword, confirm);
    }

    @Test
    void isPasswordStrongChecksAllRules() {
        runOnFxThread(() -> {
            assertEquals(Boolean.TRUE, invokeMethod(controller, "isPasswordStrong",
                    new Class<?>[]{String.class}, "Abcdefg1"));
            assertEquals(Boolean.FALSE, invokeMethod(controller, "isPasswordStrong",
                    new Class<?>[]{String.class}, "abcdefg1"));
            assertEquals(Boolean.FALSE, invokeMethod(controller, "isPasswordStrong",
                    new Class<?>[]{String.class}, "ABCDEFG1"));
            assertEquals(Boolean.FALSE, invokeMethod(controller, "isPasswordStrong",
                    new Class<?>[]{String.class}, "Abcdefgh"));
            assertEquals(Boolean.FALSE, invokeMethod(controller, "isPasswordStrong",
                    new Class<?>[]{String.class}, "Abc1"));
        });
    }

    @Test
    void validatePasswordRequirementsUpdatesLabelsAndStrength() {
        runOnFxThread(() -> {
            newPasswordField.setText("Abcdefg1");
            invokeMethod(controller, "validatePasswordRequirements", new Class<?>[]{});

            assertTrue(req1.getText().startsWith("✓"));
            assertTrue(req2.getText().startsWith("✓"));
            assertTrue(req3.getText().startsWith("✓"));
            assertTrue(req4.getText().startsWith("✓"));
            assertEquals("Strength: Strong", strengthLabel.getText());
            assertEquals(1.0, passwordStrengthBar.getProgress(), 0.0001);

            newPasswordField.setText("abc");
            invokeMethod(controller, "validatePasswordRequirements", new Class<?>[]{});
            assertTrue(req1.getText().startsWith("✗"));
            assertEquals("Strength: Weak", strengthLabel.getText());
            assertEquals(0.25, passwordStrengthBar.getProgress(), 0.0001);

            newPasswordField.setText("");
            invokeMethod(controller, "validatePasswordRequirements", new Class<?>[]{});
            assertEquals("Strength: None", strengthLabel.getText());
            assertEquals(0.0, passwordStrengthBar.getProgress(), 0.0001);
        });
    }

    @Test
    void validatePasswordRequirementsShowsFairAndGoodStrength() {
        runOnFxThread(() -> {
            newPasswordField.setText("abcdef");
            invokeMethod(controller, "validatePasswordRequirements", new Class<?>[]{});
            assertEquals("Strength: Weak", strengthLabel.getText());

            newPasswordField.setText("abcdefgh");
            invokeMethod(controller, "validatePasswordRequirements", new Class<?>[]{});
            assertEquals("Strength: Fair", strengthLabel.getText());

            newPasswordField.setText("Abcdefgh");
            invokeMethod(controller, "validatePasswordRequirements", new Class<?>[]{});
            assertEquals("Strength: Good", strengthLabel.getText());
        });
    }

    @Test
    void checkPasswordMatchHandlesAllCases() {
        runOnFxThread(() -> {
            invokeMethod(controller, "checkPasswordMatch", new Class<?>[]{});
            assertEquals("", matchLabel.getText());

            newPasswordField.setText("NewPass11");
            confirmPasswordField.setText("NewPass11");
            invokeMethod(controller, "checkPasswordMatch", new Class<?>[]{});
            assertEquals("✓ Passwords match", matchLabel.getText());

            confirmPasswordField.setText("OtherPass1");
            invokeMethod(controller, "checkPasswordMatch", new Class<?>[]{});
            assertEquals("✗ Passwords do not match", matchLabel.getText());
        });
    }

    @Test
    void handleChangePasswordReportsEmptyInput() {
        runOnFxThread(() -> {
            SessionManager.setCurrentUser(user);
            controller.initialize();

            invokeMethod(controller, "handleChangePassword", new Class<?>[]{});
            assertEquals("Current password cannot be empty", errorLabel.getText());
        });
    }

    @Test
    void handleChangePasswordRejectsWrongCurrentPassword() {
        runOnFxThread(() -> {
            SessionManager.setCurrentUser(user);
            controller.initialize();
            currentPasswordField.setText("WrongPass1");
            newPasswordField.setText("NewPass11");
            confirmPasswordField.setText("NewPass11");

            invokeMethod(controller, "handleChangePassword", new Class<?>[]{});
            assertEquals("❌ Current password is incorrect", errorLabel.getText());
        });
    }

    @Test
    void handleChangePasswordReportsDatabaseFailure() {
        runOnFxThread(() -> {
            SessionManager.setCurrentUser(user);
            controller.initialize();
            UserDAO userDAO = mock(UserDAO.class);
            when(userDAO.updateUserPassword(anyInt(), anyString())).thenReturn(false);
            setField(controller, "userDAO", userDAO);

            currentPasswordField.setText("OldPass1");
            newPasswordField.setText("NewPass11");
            confirmPasswordField.setText("NewPass11");

            invokeMethod(controller, "handleChangePassword", new Class<?>[]{});
            assertEquals("Failed to update password in database", errorLabel.getText());
            assertEquals("", successLabel.getText());
        });
    }
}

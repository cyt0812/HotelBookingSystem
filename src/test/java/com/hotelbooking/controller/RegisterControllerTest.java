package com.hotelbooking.controller;

import com.hotelbooking.service.UserService;
import com.hotelbooking.test.JavaFxTestSupport;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.hotelbooking.test.JavaFxTestSupport.invokeMethod;
import static com.hotelbooking.test.JavaFxTestSupport.runOnFxThread;
import static com.hotelbooking.test.JavaFxTestSupport.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * RegisterController 的表单校验与失败提示测试。
 * 成功注册分支会弹出 Alert 阻塞测试线程，因此不在单元测试范围内。
 */
class RegisterControllerTest {

    private RegisterController controller;

    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private CheckBox termsCheckbox;
    private Label errorLabel;

    @BeforeAll
    static void initJavaFx() {
        JavaFxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUp() {
        controller = new RegisterController();
        runOnFxThread(() -> {
            usernameField = new TextField();
            emailField = new TextField();
            passwordField = new PasswordField();
            confirmPasswordField = new PasswordField();
            termsCheckbox = new CheckBox();
            errorLabel = new Label();

            setField(controller, "usernameField", usernameField);
            setField(controller, "emailField", emailField);
            setField(controller, "passwordField", passwordField);
            setField(controller, "confirmPasswordField", confirmPasswordField);
            setField(controller, "termsCheckbox", termsCheckbox);
            setField(controller, "errorLabel", errorLabel);
        });
    }

    @Test
    void initializeClearsPreviousError() {
        runOnFxThread(() -> {
            errorLabel.setText("old error");
            controller.initialize();
            assertEquals("", errorLabel.getText());
        });
    }

    @Test
    void registerRejectsEmptyFields() {
        runOnFxThread(() -> {
            invokeMethod(controller, "handleRegister", new Class<?>[]{});
            assertEquals("All fields must be filled", errorLabel.getText());
        });
    }

    @Test
    void registerRejectsInvalidEmail() {
        runOnFxThread(() -> {
            fill("alice", "alice.example.com", "secret1", "secret1", true);
            invokeMethod(controller, "handleRegister", new Class<?>[]{});
            assertEquals("Please enter a valid email address", errorLabel.getText());
        });
    }

    @Test
    void registerRejectsShortPassword() {
        runOnFxThread(() -> {
            fill("alice", "alice@example.com", "123", "123", true);
            invokeMethod(controller, "handleRegister", new Class<?>[]{});
            assertEquals("Password must be at least 6 characters", errorLabel.getText());
        });
    }

    @Test
    void registerRejectsMismatchedPasswords() {
        runOnFxThread(() -> {
            fill("alice", "alice@example.com", "secret1", "secret2", true);
            invokeMethod(controller, "handleRegister", new Class<?>[]{});
            assertEquals("Passwords do not match", errorLabel.getText());
        });
    }

    @Test
    void registerRequiresAcceptingTerms() {
        runOnFxThread(() -> {
            fill("alice", "alice@example.com", "secret1", "secret1", false);
            invokeMethod(controller, "handleRegister", new Class<?>[]{});
            assertEquals("Please agree to the terms and conditions", errorLabel.getText());
        });
    }

    @Test
    void registerReportsServiceFailure() {
        runOnFxThread(() -> {
            UserService failingService = mock(UserService.class);
            when(failingService.registerUser(anyString(), anyString(), anyString(), anyString()))
                    .thenThrow(new IllegalStateException("db down"));
            setField(controller, "userService", failingService);

            fill("alice", "alice@example.com", "secret1", "secret1", true);
            invokeMethod(controller, "handleRegister", new Class<?>[]{});

            assertEquals("Registration failed: db down", errorLabel.getText());
        });
    }

    private void fill(String username, String email, String password, String confirmPassword, boolean accepted) {
        usernameField.setText(username);
        emailField.setText(email);
        passwordField.setText(password);
        confirmPasswordField.setText(confirmPassword);
        termsCheckbox.setSelected(accepted);
    }
}

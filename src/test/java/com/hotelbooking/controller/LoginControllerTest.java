package com.hotelbooking.controller;

import com.hotelbooking.entity.User;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.service.UserService;
import com.hotelbooking.util.SessionManager;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Test
    void loginWithInvalidCredentials_showsErrorAndDoesNotCreateSession() {
        UserService fakeUserService = mock(UserService.class);
        when(fakeUserService.loginUser(anyString(), anyString()))
                .thenThrow(new BusinessException(ErrorType.INVALID_CREDENTIALS, "Incorrect username or password"));

        LoginController controller = new LoginController(fakeUserService);

        try (MockedStatic<SessionManager> sessionManager = mockStatic(SessionManager.class)) {
            boolean success = controller.performLogin("wrongUser", "wrongPassword");

            assertFalse(success, "无效凭据应该返回失败状态");
            assertEquals("Incorrect username or password", controller.lastErrorMessage,
                    "无效凭据应该显示错误提示");
            sessionManager.verify(() -> SessionManager.login(any(User.class)), org.mockito.Mockito.never());
        }
    }

    @Test
    void loginWithValidCredentials_returnsSuccessAndCreatesSession() {
        UserService fakeUserService = mock(UserService.class);
        User user = new User("alice", "alice@example.com", "secret123");
        when(fakeUserService.loginUser("alice", "secret123")).thenReturn(user);

        LoginController controller = new LoginController(fakeUserService);

        try (MockedStatic<SessionManager> sessionManager = mockStatic(SessionManager.class)) {
            boolean success = controller.performLogin("alice", "secret123");

            assertTrue(success, "有效凭据应该返回成功状态");
            assertNull(controller.lastErrorMessage, "登录成功时不应显示错误提示");
            sessionManager.verify(() -> SessionManager.login(user));
        }
    }
}

package com.hotelbooking.controller;

import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Test
    void loginWithInvalidCredentials_returnsError() {
        // 1. 用 Mockito 造假 UserService
        UserService fakeUserService = mock(UserService.class);

        // 2. 规定假对象遇到无效凭据时，抛出错误（或者返回 null 也行）
        // 假设 BusinessException 的构造器需要 ErrorType
        when(fakeUserService.loginUser(anyString(), anyString()))
                .thenThrow(new BusinessException(ErrorType.INVALID_CREDENTIALS, "Incorrect username or password"));

        // 如果你们的 loginUser 无效时直接返回 null，就用这句：
        // when(fakeUserService.loginUser(anyString(), anyString())).thenReturn(null);

        // 3. 用包可见构造器塞入假 Service
        LoginController controller = new LoginController(fakeUserService);

        // 4. 调用我们刚才抽出来的方法（绕过 JavaFX）
        boolean success = controller.performLogin("wrongUser", "wrongPassword");

        // 5. 断言：必须返回 false（代表返回了错误状态）
        assertFalse(success, "无效凭据应该返回失败状态");
    }
}
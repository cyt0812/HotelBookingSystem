package com.hotelbooking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainTest {

    @Test
    void mainMethodHasExpectedEntryPointSignature() throws NoSuchMethodException {
        // JavaFX 应用无法在单元测试中真正启动，这里验证入口方法签名存在且可被 JVM 调用
        assertNotNull(Main.class.getMethod("main", String[].class));
    }
}

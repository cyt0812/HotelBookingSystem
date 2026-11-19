package com.hotelbooking.test;

import javafx.application.Platform;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * JavaFX 测试支持工具。
 * <p>
 * 保证整个测试 JVM 内 {@link Platform#startup(Runnable)} 只被调用一次，
 * 并提供在 FX 线程上执行代码、反射注入私有字段 / 调用私有方法的辅助方法。
 * 测试中不创建真实 Stage / 不显示窗口，以便在 xvfb 无头环境下也能稳定运行。
 */
public final class JavaFxTestSupport {

    private static final AtomicBoolean TOOLKIT_STARTED = new AtomicBoolean(false);
    private static final long DEFAULT_TIMEOUT_SECONDS = 20L;

    private JavaFxTestSupport() {
    }

    /**
     * 初始化 JavaFX 工具包（多线程安全，只会真正执行一次）。
     */
    public static void initToolkit() {
        if (!TOOLKIT_STARTED.compareAndSet(false, true)) {
            return;
        }
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            if (!latch.await(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IllegalStateException("JavaFX 工具包初始化超时");
            }
        } catch (IllegalStateException alreadyStarted) {
            // 其他测试已经启动过工具包，忽略
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("JavaFX 工具包初始化被中断", e);
        }
    }

    /**
     * 在 JavaFX 应用线程上执行一段代码并等待其完成。
     */
    public static void runOnFxThread(Runnable action) {
        runOnFxThread(() -> {
            action.run();
            return null;
        });
    }

    /**
     * 在 JavaFX 应用线程上执行一段代码并返回结果。
     */
    public static <T> T runOnFxThread(Supplier<T> action) {
        initToolkit();
        if (Platform.isFxApplicationThread()) {
            return action.get();
        }
        AtomicReference<T> result = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                result.set(action.get());
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });
        try {
            if (!latch.await(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IllegalStateException("等待 JavaFX 线程执行超时");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("等待 JavaFX 线程执行被中断", e);
        }
        Throwable throwable = error.get();
        if (throwable != null) {
            if (throwable instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            if (throwable instanceof Error e) {
                throw e;
            }
            throw new IllegalStateException("JavaFX 线程执行失败", throwable);
        }
        return result.get();
    }

    /**
     * 反射设置字段值（会沿父类链查找）。
     */
    public static void setField(Object target, String name, Object value) {
        Field field = findField(target.getClass(), name);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("无法设置字段: " + name, e);
        }
    }

    /**
     * 反射读取字段值（会沿父类链查找）。
     */
    public static Object getField(Object target, String name) {
        Field field = findField(target.getClass(), name);
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("无法读取字段: " + name, e);
        }
    }

    /**
     * 反射调用私有 / 包可见方法。
     */
    public static Object invokeMethod(Object target, String name, Class<?>[] parameterTypes, Object... args) {
        Method method = findMethod(target.getClass(), name, parameterTypes);
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw new IllegalStateException("方法调用失败: " + name, cause);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("无法访问方法: " + name, e);
        }
    }

    private static Field findField(Class<?> type, String name) {
        Class<?> current = type;
        while (current != null) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new IllegalStateException("未找到字段: " + name + " in " + type.getName());
    }

    private static Method findMethod(Class<?> type, String name, Class<?>[] parameterTypes) {
        Class<?> current = type;
        while (current != null) {
            try {
                Method method = current.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new IllegalStateException("未找到方法: " + name + " in " + type.getName());
    }
}

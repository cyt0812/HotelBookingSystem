package com.hotelbooking.dao;

import com.hotelbooking.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private final UserDAO userDAO = new UserDAO(); // 实例化待测试的DAO类

    // 测试1：创建新用户（数据库操作成功，应返回true）
    @Test
    void createUser_shouldReturnTrueWhenDataValid() {
        // 准备测试数据（使用唯一用户名，避免冲突）
        User newUser = new User();
        newUser.setUsername("daoTestUser_" + System.currentTimeMillis()); // 用时间戳保证唯一
        newUser.setPassword("testPass123");
        newUser.setEmail("test@example.com");
        newUser.setFullName("Test User");
        newUser.setRole("user");

        // 执行创建用户方法
        boolean result = userDAO.createUser(newUser);

        // 验证结果：创建成功
        assertTrue(result, "有效用户数据应创建成功");
    }

    // 测试2：创建用户失败（模拟数据库异常，如重复主键，应返回false）
    @Test
    void createUser_shouldReturnFalseWhenDuplicateUsername() {
        // 1. 先创建一个用户
        User user = new User();
        user.setUsername("duplicateUser");
        user.setPassword("pass");
        user.setEmail("dup@test.com");
        user.setFullName("Dup User");
        user.setRole("user");
        userDAO.createUser(user); // 首次创建成功

        // 2. 用相同用户名再次创建（触发唯一键冲突）
        User duplicateUser = new User();
        duplicateUser.setUsername("duplicateUser"); // 重复用户名
        duplicateUser.setPassword("anotherPass");
        boolean result = userDAO.createUser(duplicateUser);

        // 3. 验证结果：创建失败
        assertFalse(result, "重复用户名应创建失败");
    }

    // 测试3：查询存在的用户（应返回非空User对象）
    @Test
    void getUserByUsername_shouldReturnUserWhenExists() {
        // 1. 先创建一个用户用于查询
        String testUsername = "queryTestUser";
        User user = new User();
        user.setUsername(testUsername);
        user.setPassword("queryPass");
        user.setEmail("query@test.com");
        user.setFullName("Query User");
        user.setRole("user");
        userDAO.createUser(user);

        // 2. 查询该用户
        User foundUser = userDAO.getUserByUsername(testUsername);

        // 3. 验证结果：用户存在且字段匹配
        assertNotNull(foundUser, "存在的用户应被查询到");
        assertEquals(testUsername, foundUser.getUsername(), "查询到的用户名应匹配");
        assertEquals("query@test.com", foundUser.getEmail(), "查询到的邮箱应匹配");
    }

    // 测试4：查询不存在的用户（应返回null）
    @Test
    void getUserByUsername_shouldReturnNullWhenNotExists() {
        // 查询一个不存在的用户名
        String nonExistentUsername = "nonExistent_" + System.currentTimeMillis();
        User foundUser = userDAO.getUserByUsername(nonExistentUsername);

        // 验证结果：返回null
        assertNull(foundUser, "不存在的用户应返回null");
    }
}
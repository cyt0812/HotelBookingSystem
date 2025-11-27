package com.hotelbooking.dao;

import com.hotelbooking.entity.User;
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        // 先清空数据避免冲突
        DatabaseInitializer.clearTestData();
        DatabaseInitializer.initializeDatabase();
        userDAO = new UserDAO();
    }

    @Test
    void createUser_WithValidUser_ShouldReturnUserWithId() {
        // 准备 - 使用唯一的数据
        User user = new User("unique_user", "unique@test.com", "password", "CUSTOMER");
        
        // 执行
        User result = userDAO.createUser(user);
        
        // 验证
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("unique_user", result.getUsername());
        assertEquals("unique@test.com", result.getEmail());
    }

    @Test
    void getUserById_WithExistingId_ShouldReturnUser() {
        // 先创建用户
        User user = userDAO.createUser(new User("existing_user", "existing@test.com", "pass", "USER"));
        
        // 执行
        Optional<User> result = userDAO.getUserById(user.getId());
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        assertEquals("existing_user", result.get().getUsername());
    }

    @Test
    void getUserById_WithNonExistingId_ShouldReturnEmpty() {
        // 执行
        Optional<User> result = userDAO.getUserById(999);
        
        // 验证
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserByUsername_WithExistingUsername_ShouldReturnUser() {
        // 准备
        userDAO.createUser(new User("unique_username", "unique@test.com", "pass", "USER"));
        
        // 执行
        Optional<User> result = userDAO.getUserByUsername("unique_username");
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals("unique_username", result.get().getUsername());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // 准备 - 创建几个测试用户
        userDAO.createUser(new User("user1_list", "user1@test.com", "pass", "USER"));
        userDAO.createUser(new User("user2_list", "user2@test.com", "pass", "ADMIN"));
        
        // 执行
        List<User> users = userDAO.getAllUsers();
        
        // 验证
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    void updateUser_ShouldUpdateUserData() {
        // 准备
        User user = userDAO.createUser(new User("oldname", "old@test.com", "pass", "USER"));
        
        // 执行 - 更新用户信息
        user.setUsername("newname");
        user.setEmail("new@test.com");
        boolean updated = userDAO.updateUser(user);
        
        // 验证
        assertTrue(updated);
        Optional<User> retrievedUser = userDAO.getUserById(user.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("newname", retrievedUser.get().getUsername());
        assertEquals("new@test.com", retrievedUser.get().getEmail());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        // 准备
        User user = userDAO.createUser(new User("todelete", "delete@test.com", "pass", "USER"));
        
        // 执行
        boolean deleted = userDAO.deleteUser(user.getId());
        
        // 验证
        assertTrue(deleted);
        Optional<User> retrievedUser = userDAO.getUserById(user.getId());
        assertTrue(retrievedUser.isEmpty());
    }
}
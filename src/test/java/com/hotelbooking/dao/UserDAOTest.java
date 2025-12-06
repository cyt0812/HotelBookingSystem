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
        // Generate unique database name for each test
        String uniqueDbName = "memory:testdb_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
        System.setProperty("test.derby.url", "jdbc:derby:" + uniqueDbName + ";create=true");
        
        System.out.println("Setting up test database: " + System.getProperty("test.derby.url"));
        
        // Initialize database
        DatabaseInitializer.clearTestData();
        DatabaseInitializer.initializeDatabase();
        userDAO = new UserDAO();
    }

    @Test
    void createUser_WithValidUser_ShouldReturnUserWithId() {
        // Arrange - Use unique data
        User user = new User("unique_user", "unique@test.com", "password", "CUSTOMER");
        
        // Act
        User result = userDAO.createUser(user);
        
        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("unique_user", result.getUsername());
        assertEquals("unique@test.com", result.getEmail());
    }

    @Test
    void getUserById_WithExistingId_ShouldReturnUser() {
        // First create a user
        User user = userDAO.createUser(new User("existing_user", "existing@test.com", "pass", "USER"));
        
        // Act
        Optional<User> result = userDAO.getUserById(user.getId());
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        assertEquals("existing_user", result.get().getUsername());
    }

    @Test
    void getUserById_WithNonExistingId_ShouldReturnEmpty() {
        // Act
        Optional<User> result = userDAO.getUserById(999);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserByUsername_WithExistingUsername_ShouldReturnUser() {
        // Arrange
        userDAO.createUser(new User("unique_username", "unique@test.com", "pass", "USER"));
        
        // Act
        Optional<User> result = userDAO.getUserByUsername("unique_username");
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("unique_username", result.get().getUsername());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange - Create several test users
        userDAO.createUser(new User("user1_list", "user1@test.com", "pass", "USER"));
        userDAO.createUser(new User("user2_list", "user2@test.com", "pass", "ADMIN"));
        
        // Act
        List<User> users = userDAO.getAllUsers();
        
        // Assert
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    void updateUser_ShouldUpdateUserData() {
        // Arrange
        User user = userDAO.createUser(new User("oldname", "old@test.com", "pass", "USER"));
        
        // Act - Update user information
        user.setUsername("newname");
        user.setEmail("new@test.com");
        boolean updated = userDAO.updateUser(user);
        
        // Assert
        assertTrue(updated);
        Optional<User> retrievedUser = userDAO.getUserById(user.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("newname", retrievedUser.get().getUsername());
        assertEquals("new@test.com", retrievedUser.get().getEmail());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        // Arrange
        User user = userDAO.createUser(new User("todelete", "delete@test.com", "pass", "USER"));
        
        // Act
        boolean deleted = userDAO.deleteUser(user.getId());
        
        // Assert
        assertTrue(deleted);
        Optional<User> retrievedUser = userDAO.getUserById(user.getId());
        assertTrue(retrievedUser.isEmpty());
    }
}
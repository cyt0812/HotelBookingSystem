package com.hotelbooking.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NavigationManagerTest {
    private NavigationManager navigationManager;

    @BeforeEach
    void setUp() {
        // Since it's a singleton, we need to clear the instance first
        // Here we need to reset the singleton instance through reflection because getInstance is private
        try {
            java.lang.reflect.Field instanceField = NavigationManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Get new instance
        navigationManager = NavigationManager.getInstance();
    }

    @Test
    void testGetInstance_ShouldReturnSameInstance() {
        // Test singleton pattern
        NavigationManager instance1 = NavigationManager.getInstance();
        NavigationManager instance2 = NavigationManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testPushAndHasPrevious() {
        // Add first page
        navigationManager.push("/fxml/Home.fxml", "Home Page");
        assertFalse(navigationManager.hasPrevious());
        
        // Add second page
        navigationManager.push("/fxml/Login.fxml", "Login Page");
        assertTrue(navigationManager.hasPrevious());
        
        // Add third page
        navigationManager.push("/fxml/Register.fxml", "Register Page");
        assertTrue(navigationManager.hasPrevious());
    }

    @Test
    void testGetPrevious() {
        // Add pages
        navigationManager.push("/fxml/Home.fxml", "Home Page");
        navigationManager.push("/fxml/Login.fxml", "Login Page");
        navigationManager.push("/fxml/Register.fxml", "Register Page");
        
        // Get previous page
        NavigationManager.NavigationHistory previous = navigationManager.getPrevious();
        assertNotNull(previous);
        assertEquals("Login Page", previous.title);
        assertEquals("/fxml/Login.fxml", previous.fxmlPath);
    }

    @Test
    void testPopCurrent() {
        // Add pages
        navigationManager.push("/fxml/Home.fxml", "Home Page");
        navigationManager.push("/fxml/Login.fxml", "Login Page");
        navigationManager.push("/fxml/Register.fxml", "Register Page");
        
        // Pop current page
        navigationManager.popCurrent();
        assertTrue(navigationManager.hasPrevious());
        
        // Pop again
        navigationManager.popCurrent();
        assertFalse(navigationManager.hasPrevious());
        
        // Getting previous page should be null
        assertNull(navigationManager.getPrevious());
    }

    @Test
    void testClear() {
        // Add pages
        navigationManager.push("/fxml/Home.fxml", "Home Page");
        navigationManager.push("/fxml/Login.fxml", "Login Page");
        
        // Clear history
        navigationManager.clear();
        assertFalse(navigationManager.hasPrevious());
        assertNull(navigationManager.getPrevious());
    }

    @Test
    void testGoHome() {
        // Add pages
        navigationManager.push("/fxml/Home.fxml", "Home Page");
        navigationManager.push("/fxml/Login.fxml", "Login Page");
        navigationManager.push("/fxml/Register.fxml", "Register Page");
        
        // Go home
        navigationManager.goHome("/fxml/Home.fxml", "Home Page");
        
        // Verify only home page remains
        assertFalse(navigationManager.hasPrevious());
        NavigationManager.NavigationHistory previous = navigationManager.getPrevious();
        assertNull(previous);
    }

    @Test
    void testPushWithController() {
        // Test push method with controller
        Object controller = new Object();
        navigationManager.push("/fxml/HotelDetail.fxml", "Hotel Detail", controller);
        
        // Pop current page and verify controller
        navigationManager.popCurrent();
        
        // Add new page and get previous page
        navigationManager.push("/fxml/Home.fxml", "Home Page");
        navigationManager.push("/fxml/HotelDetail.fxml", "Hotel Detail", controller);
        
        NavigationManager.NavigationHistory previous = navigationManager.getPrevious();
        assertNotNull(previous);
        assertEquals("Home Page", previous.title);
    }
}

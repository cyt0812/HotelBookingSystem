package com.hotelbooking;

import com.hotelbooking.controller.AdminController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {

    @Test
    public void testAdminControllerInitialization() {
        try {
            AdminController adminController = new AdminController();
            assertNotNull(adminController, "AdminController should be instantiated normally");
            System.out.println("✅ AdminController instantiation test passed");
        } catch (Exception e) {
            fail("AdminController instantiation failed: " + e.getMessage());
        }
    }
}
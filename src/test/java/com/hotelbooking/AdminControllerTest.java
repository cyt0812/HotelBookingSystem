package com.hotelbooking;

import com.hotelbooking.controller.AdminController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {

    @Test
    public void testAdminControllerInitialization() {
        try {
            AdminController adminController = new AdminController();
<<<<<<< HEAD
            assertNotNull(adminController, "AdminController should be instantiated normally");
            System.out.println("✅ AdminController instantiation test passed");
        } catch (Exception e) {
            fail("AdminController instantiation failed: " + e.getMessage());
=======
            assertNotNull(adminController, "AdminController 应该能正常实例化");
            System.out.println("✅ AdminController 实例化测试通过");
        } catch (Exception e) {
            fail("AdminController 实例化失败: " + e.getMessage());
>>>>>>> gui-dashboard/master
        }
    }
}
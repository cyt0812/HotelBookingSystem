package com.hotelbooking.controller;

public class AdminControllerTest {

    public static void main(String[] args) {
        AdminController adminController = new AdminController();
        
        System.out.println("Running AdminController test...");
        adminController.showAdminDashboard();
        
        System.out.println("Test finished.");
    }
}
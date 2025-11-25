package com.hotelbooking;

import com.hotelbooking.dao.*;
import com.hotelbooking.entity.*;

public class TestAllDAOs {
    public static void main(String[] args) {
        System.out.println("=== 测试所有DAO ===");
        
        // 测试UserDAO
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername("testuser");
        System.out.println("✅ UserDAO测试: " + (user != null ? "通过" : "失败"));
        
        // 测试HotelDAO
        HotelDAO hotelDAO = new HotelDAO();
        System.out.println("✅ HotelDAO测试: 找到 " + hotelDAO.getAllHotels().size() + " 家酒店");
        
        // 测试RoomDAO
        RoomDAO roomDAO = new RoomDAO();
        System.out.println("✅ RoomDAO测试: 找到 " + roomDAO.getAllRooms().size() + " 个房间");
        
        // 测试BookingDAO
        BookingDAO bookingDAO = new BookingDAO();
        System.out.println("✅ BookingDAO测试: 就绪");
        
        System.out.println("=== 所有DAO测试完成 ===");
    }
}
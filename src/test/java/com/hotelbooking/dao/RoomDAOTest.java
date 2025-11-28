package com.hotelbooking.dao;

import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.Hotel; // 添加这行导入
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class RoomDAOTest {

    private RoomDAO roomDAO;
    private HotelDAO hotelDAO;
    private Integer testHotelId;

    @BeforeEach
    void setUp() {
        DatabaseInitializer.initializeDatabase();
        roomDAO = new RoomDAO();
        hotelDAO = new HotelDAO();
        
        // 创建一个测试酒店 - 修正这里的语法
        Hotel hotel = hotelDAO.createHotel(new Hotel("Test Hotel", "Test City", "Test Desc", 10));
        testHotelId = hotel.getId();
    }

    @Test
    void createRoom_WithValidRoom_ShouldReturnRoomWithId() {
        // 准备
        Room room = new Room(testHotelId, "101", "SINGLE", new BigDecimal("99.99"), true);
        
        // 执行
        Room result = roomDAO.createRoom(room);
        
        // 验证
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testHotelId, result.getHotelId());
        assertEquals("101", result.getRoomNumber());
        assertEquals("SINGLE", result.getRoomType());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        assertTrue(result.isAvailable());
    }

    // ... 其他测试方法保持不变
}
package com.hotelbooking.dao;

import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.util.DatabaseConnection;
import com.hotelbooking.util.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoomDAOTest {

    private RoomDAO roomDAO;
    private HotelDAO hotelDAO;
    private Integer testHotelId;
    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        // 清理并重新初始化数据库（Derby 需要）
        DatabaseInitializer.resetDatabase();
        
        // 获取数据库连接
        connection = DatabaseConnection.getConnection();
        roomDAO = new RoomDAO();
        hotelDAO = new HotelDAO();
        
        // 创建一个测试酒店（注意：需要先创建酒店，因为房间有外键约束）
     Hotel hotel = new Hotel("Test Hotel", "Test City", "Test Description", "Pool,Gym", 10);
        Hotel createdHotel = hotelDAO.createHotel(hotel);
        
        if (createdHotel != null) {
            testHotelId = createdHotel.getId();
        } else {
            throw new RuntimeException("Failed to create test hotel");
        }
    }
    
    @AfterEach
    void tearDown() throws Exception {
        // 清理测试数据
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                // Derby 需要先禁用外键约束
                try {
                    stmt.execute("SET CONSTRAINTS ALL DEFERRED");
                } catch (Exception e) {
                    // 忽略错误，某些 Derby 版本不需要
                }
                
                // 删除测试数据
                try {
                    stmt.execute("DELETE FROM rooms WHERE hotel_id = " + testHotelId);
                } catch (Exception e) {}
                
                try {
                    stmt.execute("DELETE FROM hotels WHERE id = " + testHotelId);
                } catch (Exception e) {}
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void createRoom_WithValidRoom_ShouldReturnRoomWithId() {
        // 准备 - 使用 7 个参数的构造函数
        Room room = new Room(testHotelId, "101", "STANDARD", 99.99, 2, true, "A comfortable standard room");
        
        // 执行
        Room result = roomDAO.createRoom(room);
        
        // 验证
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testHotelId, result.getHotelId());
        assertEquals("101", result.getRoomNumber());
        assertEquals("STANDARD", result.getRoomType());
        assertEquals(99.99, result.getPricePerNight(), 0.001);
        assertEquals(2, result.getMaxOccupancy());
        assertEquals("A comfortable standard room", result.getDescription());
        assertTrue(result.isAvailable());
    }
    
    @Test
    void getRoomById_WithExistingId_ShouldReturnRoom() {
        // 先创建一个房间
        Room room = new Room(testHotelId, "102", "DELUXE", 149.99, 4, true, "Double room with two beds");
        Room created = roomDAO.createRoom(room);
        assertNotNull(created);
        
        // 查询这个房间
        Optional<Room> foundOpt = roomDAO.getRoomById(created.getId());
        
        // 验证
        assertTrue(foundOpt.isPresent());
        Room found = foundOpt.get();
        assertEquals(created.getId(), found.getId());
        assertEquals("102", found.getRoomNumber());
        assertEquals("DELUXE", found.getRoomType());
        assertEquals(149.99, found.getPricePerNight(), 0.001);
        assertEquals(4, found.getMaxOccupancy());
        assertEquals("Double room with two beds", found.getDescription());
        assertTrue(found.isAvailable());
    }
    
    @Test
    void getRoomById_WithNonExistingId_ShouldReturnEmpty() {
        // 查询不存在的房间
        Optional<Room> result = roomDAO.getRoomById(9999);
        
        // 验证
        assertFalse(result.isPresent());
    }
    
    @Test
    void updateRoom_ShouldUpdateRoomDetails() {
        // 创建房间
        Room room = new Room(testHotelId, "103", "SUITE", 299.99, 3, true, "Luxury suite");
        Room created = roomDAO.createRoom(room);
        assertNotNull(created);
        
        // 修改信息
        created.setRoomNumber("103A");
        created.setPricePerNight(399.99);
        created.setDescription("Updated luxury suite with view");
        created.setAvailable(false);
        
        // 更新
        boolean updated = roomDAO.updateRoom(created);
        
        // 验证更新成功
        assertTrue(updated);
        
        // 验证更新后的数据
        Optional<Room> foundOpt = roomDAO.getRoomById(created.getId());
        assertTrue(foundOpt.isPresent());
        Room found = foundOpt.get();
        
        assertEquals("103A", found.getRoomNumber());
        assertEquals(399.99, found.getPricePerNight(), 0.001);
        assertEquals("Updated luxury suite with view", found.getDescription());
        assertFalse(found.isAvailable());
        // 其他字段应保持不变
        assertEquals("SUITE", found.getRoomType());
        assertEquals(testHotelId, found.getHotelId());
    }
    
    @Test
    void getRoomsByHotelId_ShouldReturnAllRoomsForHotel() {
        // 为同一个酒店创建多个房间
        Room room1 = new Room(testHotelId, "201", "STANDARD", 99.99, 2, true, "Room 201");
        Room room2 = new Room(testHotelId, "202", "DELUXE", 149.99, 4, true, "Room 202");
        
        roomDAO.createRoom(room1);
        roomDAO.createRoom(room2);
        
        // 执行查询
        List<Room> rooms = roomDAO.getRoomsByHotelId(testHotelId);
        
        // 验证
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().allMatch(r -> r.getHotelId().equals(testHotelId)));
        assertTrue(rooms.stream().anyMatch(r -> r.getRoomNumber().equals("201")));
        assertTrue(rooms.stream().anyMatch(r -> r.getRoomNumber().equals("202")));
    }
    
    @Test
    void getAvailableRoomsByHotelId_ShouldReturnOnlyAvailableRooms() {
        // 创建房间 - 一个可用，一个不可用
        Room availableRoom = new Room(testHotelId, "301", "STANDARD", 99.99, 2, true, "Available room");
        Room unavailableRoom = new Room(testHotelId, "302", "DELUXE", 149.99, 4, false, "Unavailable room");
        
        roomDAO.createRoom(availableRoom);
        roomDAO.createRoom(unavailableRoom);
        
        // 执行查询
        List<Room> rooms = roomDAO.getAvailableRoomsByHotelId(testHotelId);
        
        // 验证
        assertNotNull(rooms);
        assertEquals(1, rooms.size());
        assertEquals("301", rooms.get(0).getRoomNumber());
        assertTrue(rooms.get(0).isAvailable());
    }
    
    @Test
    void getRoomsByType_ShouldReturnRoomsOfSpecifiedType() {
        // 创建不同房型的房间
        Room room1 = new Room(testHotelId, "401", "STANDARD", 99.99, 2, true, "Standard room 1");
        Room room2 = new Room(testHotelId, "402", "STANDARD", 109.99, 2, true, "Standard room 2");
        Room doubleRoom = new Room(testHotelId, "403", "DELUXE", 199.99, 4, true, "Deluxe room");
        
        roomDAO.createRoom(room1);
        roomDAO.createRoom(room2);
        roomDAO.createRoom(doubleRoom);
        
        // 执行查询
        List<Room> standardRooms = roomDAO.getRoomsByType("STANDARD");
        
        // 验证
        assertNotNull(standardRooms);
        assertEquals(2, standardRooms.size());
        assertTrue(standardRooms.stream().allMatch(r -> "STANDARD".equals(r.getRoomType())));
        // 应该按价格升序排列
        assertEquals(99.99, standardRooms.get(0).getPricePerNight(), 0.001);
        assertEquals(109.99, standardRooms.get(1).getPricePerNight(), 0.001);
    }
    
    @Test
    void getRoomsByPriceRange_ShouldReturnRoomsInPriceRange() {
        // 创建不同价格的房间
        Room cheapRoom = new Room(testHotelId, "501", "STANDARD", 50.00, 2, true, "Cheap room");
        Room midRoom = new Room(testHotelId, "502", "DELUXE", 100.00, 4, true, "Mid room");
        Room expensiveRoom = new Room(testHotelId, "503", "SUITE", 200.00, 3, true, "Expensive room");
        
        roomDAO.createRoom(cheapRoom);
        roomDAO.createRoom(midRoom);
        roomDAO.createRoom(expensiveRoom);
        
        // 执行查询 (75-150 价格范围)
        List<Room> rooms = roomDAO.getRoomsByPriceRange(new BigDecimal("75"), new BigDecimal("150"));
        
        // 验证
        assertNotNull(rooms);
        assertEquals(1, rooms.size());  // 只有 midRoom 在范围内
        assertEquals("502", rooms.get(0).getRoomNumber());
        assertEquals(100.00, rooms.get(0).getPricePerNight(), 0.001);
    }
    
    @Test
    void updateRoomAvailability_ShouldUpdateAvailability() {
        // 创建房间
        Room room = new Room(testHotelId, "601", "STANDARD", 99.99, 2, true, "Test room");
        Room created = roomDAO.createRoom(room);
        
        // 更新为不可用 (Derby 使用 1/0 表示布尔值)
        boolean updated = roomDAO.updateRoomAvailability(created.getId(), false);
        assertTrue(updated);
        
        // 验证
        Optional<Room> foundOpt = roomDAO.getRoomById(created.getId());
        assertTrue(foundOpt.isPresent());
        assertFalse(foundOpt.get().isAvailable());
        
        // 再更新为可用
        updated = roomDAO.updateRoomAvailability(created.getId(), true);
        assertTrue(updated);
        
        foundOpt = roomDAO.getRoomById(created.getId());
        assertTrue(foundOpt.isPresent());
        assertTrue(foundOpt.get().isAvailable());
    }
    
    @Test
    void deleteRoom_ShouldRemoveRoom() {
        // 创建房间
        Room room = new Room(testHotelId, "701", "STANDARD", 99.99, 2, true, "Room to delete");
        Room created = roomDAO.createRoom(room);
        
        // 删除房间
        boolean deleted = roomDAO.deleteRoom(created.getId());
        assertTrue(deleted);
        
        // 验证房间已删除
        Optional<Room> foundOpt = roomDAO.getRoomById(created.getId());
        assertFalse(foundOpt.isPresent());
    }
    
    @Test
    void isRoomNumberExists_ShouldCheckRoomNumber() {
        // 创建房间
        Room room = new Room(testHotelId, "801", "STANDARD", 99.99, 2, true, "Test room");
        roomDAO.createRoom(room);
        
        // 验证存在的房间号
        boolean exists = roomDAO.isRoomNumberExists(testHotelId, "801");
        assertTrue(exists);
        
        // 验证不存在的房间号
        exists = roomDAO.isRoomNumberExists(testHotelId, "999");
        assertFalse(exists);
    }
    
    @Test
    void getAllAvailableRooms_ShouldReturnAllAvailableRooms() {
        // 创建另一个酒店
        Hotel hotel2 = new Hotel("Another Hotel", "Another City", "Description", "WiFi", 5);
        Hotel createdHotel2 = hotelDAO.createHotel(hotel2);
        
        // 创建可用和不可用房间
        Room available1 = new Room(testHotelId, "901", "STANDARD", 99.99, 2, true, "Available 1");
        Room available2 = new Room(createdHotel2.getId(), "902", "DELUXE", 149.99, 4, true, "Available 2");
        Room unavailable = new Room(testHotelId, "903", "SUITE", 299.99, 3, false, "Unavailable");
        
        roomDAO.createRoom(available1);
        roomDAO.createRoom(available2);
        roomDAO.createRoom(unavailable);
        
        // 执行查询
        List<Room> allAvailable = roomDAO.getAllAvailableRooms();
        
        // 验证
        assertNotNull(allAvailable);
        assertEquals(2, allAvailable.size());
        assertTrue(allAvailable.stream().allMatch(Room::isAvailable));
    }
}
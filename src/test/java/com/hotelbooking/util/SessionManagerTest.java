package com.hotelbooking.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import com.hotelbooking.entity.User;
import com.hotelbooking.entity.Hotel;

@DisplayName("SessionManager Tests")
class SessionManagerTest {

    @BeforeEach
    void setUp() {
        // 每个测试前清空SessionManager状态
        SessionManager.clear();
        SessionManager.logout();
    }

    // ==================== 用户登录/登出测试 ====================
    
    @Test
    @DisplayName("未登录状态下各种方法返回正确的null或默认值")
    void testNotLoggedInState() {
        assertFalse(SessionManager.isLoggedIn());
        assertNull(SessionManager.getCurrentUser());
        assertNull(SessionManager.getLoggedInUsername());
        assertNull(SessionManager.getLoggedInId());
    }
    
    @Test
    @DisplayName("用户登录后状态正确")
    void testLoginSuccess() {
        // 创建测试用户
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("CUSTOMER");
        
        // 执行登录
        SessionManager.login(user);
        
        // 验证登录状态
        assertTrue(SessionManager.isLoggedIn());
        assertEquals(user, SessionManager.getCurrentUser());
        assertEquals("testuser", SessionManager.getLoggedInUsername());
        assertEquals(1, SessionManager.getLoggedInId());
    }
    
    @Test
    @DisplayName("用户登录后获取用户名正确")
    void testGetLoggedInUsername() {
        User user = new User();
        user.setId(1);
        user.setUsername("john_doe");
        
        SessionManager.login(user);
        assertEquals("john_doe", SessionManager.getLoggedInUsername());
    }
    
    @Test
    @DisplayName("用户登录后获取用户ID正确")
    void testGetLoggedInId() {
        User user = new User();
        user.setId(123);
        user.setUsername("testuser");
        
        SessionManager.login(user);
        assertEquals(123, SessionManager.getLoggedInId());
    }
    
    @Test
    @DisplayName("用户登出后状态恢复为未登录")
    void testLogoutSuccess() {
        // 先登录
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        SessionManager.login(user);
        
        assertTrue(SessionManager.isLoggedIn());
        
        // 登出
        SessionManager.logout();
        
        // 验证已登出
        assertFalse(SessionManager.isLoggedIn());
        assertNull(SessionManager.getCurrentUser());
        assertNull(SessionManager.getLoggedInUsername());
        assertNull(SessionManager.getLoggedInId());
    }
    
    @Test
    @DisplayName("登出时用户信息完全清空")
    void testLogoutClearsAllUserInfo() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        SessionManager.login(user);
        
        SessionManager.logout();
        
        assertNull(SessionManager.getCurrentUser());
        assertNull(SessionManager.getLoggedInUsername());
        assertNull(SessionManager.getLoggedInId());
    }
    
    @Test
    @DisplayName("多次登录不同用户时，后登录的用户覆盖前面的")
    void testMultipleLogins() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");
        
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");
        
        SessionManager.login(user1);
        assertEquals("user1", SessionManager.getLoggedInUsername());
        
        SessionManager.login(user2);
        assertEquals("user2", SessionManager.getLoggedInUsername());
        assertEquals(2, SessionManager.getLoggedInId());
    }
    
    // ==================== 用户设置测试 ====================
    
    @Test
    @DisplayName("setCurrentUser可以设置当前用户")
    void testSetCurrentUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        SessionManager.setCurrentUser(user);
        
        assertEquals(user, SessionManager.getCurrentUser());
        assertEquals("testuser", SessionManager.getLoggedInUsername());
    }
    
    @Test
    @DisplayName("setCurrentUser用于更新用户信息")
    void testSetCurrentUserUpdate() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        SessionManager.login(user);
        
        // 更新用户信息
        user.setEmail("newemail@example.com");
        user.setFullName("New Name");
        SessionManager.setCurrentUser(user);
        
        User current = SessionManager.getCurrentUser();
        assertEquals("newemail@example.com", current.getEmail());
        assertEquals("New Name", current.getFullName());
    }
    
    // ==================== 酒店管理测试 ====================
    
    @Test
    @DisplayName("初始状态下当前酒店为null")
    void testInitialHotelIsNull() {
        assertNull(SessionManager.getCurrentHotel());
    }
    
    @Test
    @DisplayName("设置当前酒店后能正确获取")
    void testSetCurrentHotel() {
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setDescription("Test Description");
        hotel.setAvailableRooms(10);
        
        SessionManager.setCurrentHotel(hotel);
        
        assertEquals(hotel, SessionManager.getCurrentHotel());
        assertEquals("Test Hotel", SessionManager.getCurrentHotel().getName());
        assertEquals("Test Location", SessionManager.getCurrentHotel().getLocation());
    }
    
    @Test
    @DisplayName("多次设置酒店时，后设置的覆盖前面的")
    void testMultipleHotelSettings() {
        Hotel hotel1 = new Hotel();
        hotel1.setId(1);
        hotel1.setName("Hotel 1");
        
        Hotel hotel2 = new Hotel();
        hotel2.setId(2);
        hotel2.setName("Hotel 2");
        
        SessionManager.setCurrentHotel(hotel1);
        assertEquals("Hotel 1", SessionManager.getCurrentHotel().getName());
        
        SessionManager.setCurrentHotel(hotel2);
        assertEquals("Hotel 2", SessionManager.getCurrentHotel().getName());
    }
    
    @Test
    @DisplayName("clear后当前酒店为null")
    void testHotelClearedAfterClear() {
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Test Hotel");
        SessionManager.setCurrentHotel(hotel);
        
        assertNotNull(SessionManager.getCurrentHotel());
        
        SessionManager.clear();
        
        assertNull(SessionManager.getCurrentHotel());
    }
    
    // ==================== 预订参数测试 ====================
    
    @Test
    @DisplayName("初始状态下预订参数为正确的默认值")
    void testInitialBookingParameters() {
        SessionManager.clear();
        
        assertNull(SessionManager.getCheckInDate());
        assertNull(SessionManager.getCheckOutDate());
        assertEquals(1, SessionManager.getRoomCount());
        assertEquals(1, SessionManager.getAdultCount());
        assertEquals(0, SessionManager.getChildCount());
    }
    
    @Test
    @DisplayName("设置入住日期后能正确获取")
    void testSetCheckInDate() {
        LocalDate checkInDate = LocalDate.of(2024, 12, 25);
        
        SessionManager.setCheckInDate(checkInDate);
        
        assertEquals(checkInDate, SessionManager.getCheckInDate());
    }
    
    @Test
    @DisplayName("设置退住日期后能正确获取")
    void testSetCheckOutDate() {
        LocalDate checkOutDate = LocalDate.of(2024, 12, 28);
        
        SessionManager.setCheckOutDate(checkOutDate);
        
        assertEquals(checkOutDate, SessionManager.getCheckOutDate());
    }
    
    @Test
    @DisplayName("设置房间数后能正确获取")
    void testSetRoomCount() {
        SessionManager.setRoomCount(3);
        assertEquals(3, SessionManager.getRoomCount());
        
        SessionManager.setRoomCount(5);
        assertEquals(5, SessionManager.getRoomCount());
    }
    
    @Test
    @DisplayName("设置成人数后能正确获取")
    void testSetAdultCount() {
        SessionManager.setAdultCount(2);
        assertEquals(2, SessionManager.getAdultCount());
        
        SessionManager.setAdultCount(4);
        assertEquals(4, SessionManager.getAdultCount());
    }
    
    @Test
    @DisplayName("设置儿童数后能正确获取")
    void testSetChildCount() {
        SessionManager.setChildCount(2);
        assertEquals(2, SessionManager.getChildCount());
        
        SessionManager.setChildCount(3);
        assertEquals(3, SessionManager.getChildCount());
    }
    
    @Test
    @DisplayName("完整的预订参数设置流程")
    void testCompleteBookingParametersSetup() {
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        // 设置所有参数
        SessionManager.setCheckInDate(checkIn);
        SessionManager.setCheckOutDate(checkOut);
        SessionManager.setRoomCount(2);
        SessionManager.setAdultCount(3);
        SessionManager.setChildCount(1);
        
        // 验证所有参数
        assertEquals(checkIn, SessionManager.getCheckInDate());
        assertEquals(checkOut, SessionManager.getCheckOutDate());
        assertEquals(2, SessionManager.getRoomCount());
        assertEquals(3, SessionManager.getAdultCount());
        assertEquals(1, SessionManager.getChildCount());
    }
    
    @Test
    @DisplayName("修改日期参数后能正确获取新值")
    void testModifyBookingDates() {
        LocalDate date1 = LocalDate.of(2024, 12, 25);
        LocalDate date2 = LocalDate.of(2024, 12, 28);
        
        SessionManager.setCheckInDate(date1);
        assertEquals(date1, SessionManager.getCheckInDate());
        
        SessionManager.setCheckInDate(date2);
        assertEquals(date2, SessionManager.getCheckInDate());
    }
    
    @Test
    @DisplayName("修改数字参数后能正确获取新值")
    void testModifyBookingCounts() {
        SessionManager.setRoomCount(1);
        assertEquals(1, SessionManager.getRoomCount());
        
        SessionManager.setRoomCount(5);
        assertEquals(5, SessionManager.getRoomCount());
        
        SessionManager.setAdultCount(2);
        assertEquals(2, SessionManager.getAdultCount());
    }
    
    // ==================== clear方法测试 ====================
    
    @Test
    @DisplayName("clear方法重置所有预订参数")
    void testClearResetsBookingParameters() {
        // 设置各种参数
        SessionManager.setCheckInDate(LocalDate.now());
        SessionManager.setCheckOutDate(LocalDate.now().plusDays(3));
        SessionManager.setRoomCount(3);
        SessionManager.setAdultCount(4);
        SessionManager.setChildCount(2);
        
        // 验证参数已设置
        assertNotNull(SessionManager.getCheckInDate());
        assertNotNull(SessionManager.getCheckOutDate());
        assertEquals(3, SessionManager.getRoomCount());
        
        // 执行clear
        SessionManager.clear();
        
        // 验证参数已重置
        assertNull(SessionManager.getCheckInDate());
        assertNull(SessionManager.getCheckOutDate());
        assertEquals(1, SessionManager.getRoomCount());
        assertEquals(1, SessionManager.getAdultCount());
        assertEquals(0, SessionManager.getChildCount());
    }
    
    @Test
    @DisplayName("clear方法重置酒店信息")
    void testClearResetsHotel() {
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Test Hotel");
        SessionManager.setCurrentHotel(hotel);
        
        assertNotNull(SessionManager.getCurrentHotel());
        
        SessionManager.clear();
        
        assertNull(SessionManager.getCurrentHotel());
    }
    
    @Test
    @DisplayName("clear方法不会清除登录用户信息")
    void testClearDoesNotClearUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        SessionManager.login(user);
        
        SessionManager.clear();
        
        // 注意：clear不清除用户信息，只清除预订相关信息
        assertTrue(SessionManager.isLoggedIn());
        assertEquals("testuser", SessionManager.getLoggedInUsername());
    }
    
    @Test
    @DisplayName("clear方法重置所有相关状态")
    void testClearComprehensive() {
        // 设置用户
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        SessionManager.login(user);
        
        // 设置酒店
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Test Hotel");
        SessionManager.setCurrentHotel(hotel);
        
        // 设置预订参数
        SessionManager.setCheckInDate(LocalDate.now());
        SessionManager.setCheckOutDate(LocalDate.now().plusDays(3));
        SessionManager.setRoomCount(2);
        SessionManager.setAdultCount(3);
        SessionManager.setChildCount(1);
        
        // 验证所有状态已设置
        assertTrue(SessionManager.isLoggedIn());
        assertNotNull(SessionManager.getCurrentHotel());
        assertNotNull(SessionManager.getCheckInDate());
        assertEquals(2, SessionManager.getRoomCount());
        
        // 执行clear
        SessionManager.clear();
        
        // 验证预订和酒店信息已清除
        assertNull(SessionManager.getCurrentHotel());
        assertNull(SessionManager.getCheckInDate());
        assertNull(SessionManager.getCheckOutDate());
        assertEquals(1, SessionManager.getRoomCount());
        assertEquals(1, SessionManager.getAdultCount());
        assertEquals(0, SessionManager.getChildCount());
        
        // 验证用户信息仍然保留
        assertTrue(SessionManager.isLoggedIn());
        assertEquals("testuser", SessionManager.getLoggedInUsername());
    }
    
    // ==================== 边界条件和异常测试 ====================
    
    @Test
    @DisplayName("设置0个房间")
    void testSetZeroRooms() {
        SessionManager.setRoomCount(0);
        assertEquals(0, SessionManager.getRoomCount());
    }
    
    @Test
    @DisplayName("设置大数量房间")
    void testSetLargeRoomCount() {
        SessionManager.setRoomCount(100);
        assertEquals(100, SessionManager.getRoomCount());
    }
    
    @Test
    @DisplayName("设置相同的日期为入住和退住日期")
    void testSameDayCheckInAndOut() {
        LocalDate sameDay = LocalDate.of(2024, 12, 25);
        
        SessionManager.setCheckInDate(sameDay);
        SessionManager.setCheckOutDate(sameDay);
        
        assertEquals(sameDay, SessionManager.getCheckInDate());
        assertEquals(sameDay, SessionManager.getCheckOutDate());
    }
    
    @Test
    @DisplayName("设置多个儿童")
    void testSetMultipleChildren() {
        SessionManager.setChildCount(5);
        assertEquals(5, SessionManager.getChildCount());
    }
    
    @Test
    @DisplayName("多次logout不会导致错误")
    void testMultipleLogouts() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        SessionManager.login(user);
        
        SessionManager.logout();
        SessionManager.logout(); // 多次logout不应该抛出异常
        
        assertFalse(SessionManager.isLoggedIn());
    }
}
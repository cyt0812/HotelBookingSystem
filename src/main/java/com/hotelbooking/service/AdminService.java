package com.hotelbooking.service;

import com.hotelbooking.entity.User;
import com.hotelbooking.entity.Room;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员服务层
 * 适配你的User实体类（无userId/status的构造器，使用Setter初始化）
 */
public class AdminService {

    /**
     * 查询所有用户（模拟数据，使用Setter方法初始化User对象）
     * @return 用户列表
     */
    public List<User> queryAllUsers() {
        List<User> userList = new ArrayList<>();

        // 初始化第一个用户：使用无参构造器 + Setter方法
        User user1 = new User();
        user1.setUserId(1); // 设置用户ID
        user1.setUsername("testuser1");
        user1.setPassword("123456");
        user1.setEmail("test1@example.com");
        user1.setFullName("测试用户1");
        user1.setRole("CUSTOMER"); // 普通用户
        user1.setStatus(true);     // 启用状态
        userList.add(user1);

        // 初始化第二个用户
        User user2 = new User();
        user2.setUserId(2);
        user2.setUsername("testuser2");
        user2.setPassword("123456");
        user2.setEmail("test2@example.com");
        user2.setFullName("测试用户2");
        user2.setRole("ADMIN");    // 管理员角色
        user2.setStatus(true);     // 启用状态
        userList.add(user2);

        return userList;
    }

    /**
     * 更新用户状态（启用/禁用）
     * @param userId 用户ID
     * @param status 目标状态
     * @return 操作结果
     */
    public boolean updateUserStatus(int userId, boolean status) {
        // 模拟逻辑：仅当用户ID为1/2时返回成功（模拟数据库更新成功）
        if (userId == 1 || userId == 2) {
            // 实际项目中会查询数据库并更新状态，此处简化
            return true;
        }
        return false;
    }

    /**
     * 添加新房间（模拟逻辑）
     * @param room 房间对象
     * @return 操作结果
     */
    public boolean insertRoom(Room room) {
        // 模拟逻辑：房间号以"10"开头时返回成功（避免重复）
        return room.getRoomNumber() != null && room.getRoomNumber().startsWith("10");
    }
}
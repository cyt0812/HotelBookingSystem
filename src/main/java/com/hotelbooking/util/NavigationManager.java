package com.hotelbooking.util;

import java.util.Stack;

/**
 * 导航管理器 - 管理页面导航历史（单例模式）
 * 用于实现返回上一页功能
 */
public class NavigationManager {
    private static NavigationManager instance;
    private Stack<NavigationHistory> history;
    
    private NavigationManager() {
        this.history = new Stack<>();
    }
    
    /**
     * 获取单例实例
     */
    public static synchronized NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }
    
    /**
     * 导航历史记录类
     */
    public static class NavigationHistory {
        public String fxmlPath;  // FXML 文件路径
        public String title;     // 页面标题
        public Object controller; // 页面控制器（用于传递数据）
        
        public NavigationHistory(String fxmlPath, String title) {
            this.fxmlPath = fxmlPath;
            this.title = title;
        }
        
        public NavigationHistory(String fxmlPath, String title, Object controller) {
            this.fxmlPath = fxmlPath;
            this.title = title;
            this.controller = controller;
        }
    }
    
    /**
     * 记录导航记录
     */
    public void push(String fxmlPath, String title) {
        history.push(new NavigationHistory(fxmlPath, title));
        System.out.println("📍 导航记录: " + title + " (" + fxmlPath + ")");
        printHistory();
    }
    
    /**
     * 记录导航记录（带控制器）
     */
    public void push(String fxmlPath, String title, Object controller) {
        history.push(new NavigationHistory(fxmlPath, title, controller));
        System.out.println("📍 导航记录: " + title + " (" + fxmlPath + ")");
        printHistory();
    }
    
    /**
     * 获取上一页信息（不弹出当前页）
     */
    public NavigationHistory getPrevious() {
        if (history.size() > 1) {
            NavigationHistory prev = history.get(history.size() - 2);
            System.out.println("⬅️ 返回到: " + prev.title);
            return prev;
        }
        return null;
    }
    
    /**
     * 弹出当前页（在导航后调用）
     */
    public void popCurrent() {
        if (!history.isEmpty()) {
            NavigationHistory current = history.pop();
            System.out.println("🗑️ 关闭页面: " + current.title);
        }
    }
    
    /**
     * 检查是否有上一页
     */
    public boolean hasPrevious() {
        return history.size() > 1;
    }
    
    /**
     * 清空历史记录
     */
    public void clear() {
        history.clear();
        System.out.println("🧹 导航历史已清空");
    }
    
    /**
     * 返回首页（清空所有历史）
     */
    public void goHome(String homeFxmlPath, String homeTitle) {
        clear();
        push(homeFxmlPath, homeTitle);
    }
    
    /**
     * 打印导航历史（调试用）
     */
    private void printHistory() {
        System.out.println("📊 导航栈 (共 " + history.size() + " 页):");
        for (int i = history.size() - 1; i >= 0; i--) {
            NavigationHistory nav = history.get(i);
            String arrow = (i == history.size() - 1) ? "➜ " : "  ";
            System.out.println(arrow + nav.title);
        }
    }
}
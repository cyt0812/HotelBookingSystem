/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;

public class RoomsGuestsSelectorController {
    
    @FXML private Button btnRoomsGuests;
    @FXML private Label lblRoomsGuestsDisplay;
    @FXML private Label lblArrow;
    @FXML private VBox selectorPanel;
    
    @FXML private Button btnRoomMinus;
    @FXML private Button btnRoomPlus;
    @FXML private Label lblRoomCount;
    
    @FXML private Button btnAdultMinus;
    @FXML private Button btnAdultPlus;
    @FXML private Label lblAdultCount;
    
    @FXML private Button btnChildMinus;
    @FXML private Button btnChildPlus;
    @FXML private Label lblChildCount;
    
    @FXML private VBox childrenAgeContainer;
    
    // 数据存储
    private int roomCount = 2;
    private int adultCount = 3;
    private int childCount = 2;
    private List<Integer> childrenAges = new ArrayList<>();
    
    // 最大最小值限制
    private static final int MAX_ROOMS = 9;
    private static final int MIN_ROOMS = 1;
    private static final int MAX_GUESTS_PER_ROOM = 8;
    private static final int MIN_ADULTS = 1;
    
    @FXML
    public void initialize() {
        System.out.println("✅ RoomsGuestsSelector 初始化");
        
        // 初始化儿童年龄列表
        for (int i = 0; i < childCount; i++) {
            childrenAges.add(0); // 默认 <1 岁
        }
        
        // 更新显示
        updateDisplay();
        updateChildrenAgeSelectors();
        updateButtons();
    }
    
    /**
     * 切换选择器显示/隐藏
     */
    @FXML
    private void toggleSelector() {
        boolean isVisible = selectorPanel.isVisible();
        selectorPanel.setVisible(!isVisible);
        selectorPanel.setManaged(!isVisible);
        
        // 更新箭头方向
        lblArrow.setText(isVisible ? "▼" : "▲");
        
        System.out.println("选择器" + (isVisible ? "关闭" : "打开"));
    }
    
    /**
     * 增加房间数
     */
    @FXML
    private void increaseRooms() {
        if (roomCount < MAX_ROOMS) {
            roomCount++;
            updateDisplay();
            updateButtons();
            System.out.println("房间数: " + roomCount);
        }
    }
    
    /**
     * 减少房间数
     */
    @FXML
    private void decreaseRooms() {
        if (roomCount > MIN_ROOMS) {
            roomCount--;
            updateDisplay();
            updateButtons();
            System.out.println("房间数: " + roomCount);
        }
    }
    
    /**
     * 增加成人数
     */
    @FXML
    private void increaseAdults() {
        int totalGuests = adultCount + childCount;
        int maxGuestsAllowed = roomCount * MAX_GUESTS_PER_ROOM;
        
        if (totalGuests < maxGuestsAllowed) {
            adultCount++;
            updateDisplay();
            updateButtons();
            System.out.println("成人数: " + adultCount);
        }
    }
    
    /**
     * 减少成人数
     */
    @FXML
    private void decreaseAdults() {
        if (adultCount > MIN_ADULTS) {
            adultCount--;
            updateDisplay();
            updateButtons();
            System.out.println("成人数: " + adultCount);
        }
    }
    
    /**
     * 增加儿童数
     */
    @FXML
    private void increaseChildren() {
        int totalGuests = adultCount + childCount;
        int maxGuestsAllowed = roomCount * MAX_GUESTS_PER_ROOM;
        
        if (totalGuests < maxGuestsAllowed) {
            childCount++;
            childrenAges.add(0); // 默认 <1 岁
            updateDisplay();
            updateChildrenAgeSelectors();
            updateButtons();
            System.out.println("儿童数: " + childCount);
        }
    }
    
    /**
     * 减少儿童数
     */
    @FXML
    private void decreaseChildren() {
        if (childCount > 0) {
            childCount--;
            if (!childrenAges.isEmpty()) {
                childrenAges.remove(childrenAges.size() - 1);
            }
            updateDisplay();
            updateChildrenAgeSelectors();
            updateButtons();
            System.out.println("儿童数: " + childCount);
        }
    }
    
    /**
     * 更新儿童年龄选择器
     */
    private void updateChildrenAgeSelectors() {
        childrenAgeContainer.getChildren().clear();
        
        if (childCount == 0) {
            return;
        }
        
        for (int i = 0; i < childCount; i++) {
            final int index = i;
            
            HBox ageSelector = new HBox(15);
            ageSelector.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            // 标签
            Label label = new Label("Child " + (i + 1) + ": Age");
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
            
            // 年龄下拉框
            ComboBox<String> ageComboBox = new ComboBox<>();
            ageComboBox.setItems(FXCollections.observableArrayList(
                "<1", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
                "10", "11", "12", "13", "14", "15", "16", "17"
            ));
            
            // 设置默认值
            if (index < childrenAges.size()) {
                int age = childrenAges.get(index);
                ageComboBox.setValue(age == 0 ? "<1" : String.valueOf(age));
            } else {
                ageComboBox.setValue("<1");
            }
            
            ageComboBox.setStyle("-fx-pref-width: 100px; -fx-font-size: 14px;");
            
            // 监听选择变化
            ageComboBox.setOnAction(e -> {
                String selected = ageComboBox.getValue();
                int age = selected.equals("<1") ? 0 : Integer.parseInt(selected);
                
                // 更新年龄列表
                if (index < childrenAges.size()) {
                    childrenAges.set(index, age);
                }
                
                System.out.println("儿童 " + (index + 1) + " 年龄: " + selected);
            });
            
            ageSelector.getChildren().addAll(label, ageComboBox);
            childrenAgeContainer.getChildren().add(ageSelector);
        }
    }
    
    /**
     * 更新显示文本
     */
    private void updateDisplay() {
        // 更新数字显示
        lblRoomCount.setText(String.valueOf(roomCount));
        lblAdultCount.setText(String.valueOf(adultCount));
        lblChildCount.setText(String.valueOf(childCount));
        
        // 更新按钮上的总显示
        int totalGuests = adultCount + childCount;
        String displayText = roomCount + " Room" + (roomCount > 1 ? "s" : "") + 
                           ", " + totalGuests + " Guest" + (totalGuests > 1 ? "s" : "");
        lblRoomsGuestsDisplay.setText(displayText);
    }
    
    /**
     * 更新按钮启用/禁用状态
     */
    private void updateButtons() {
        // 房间按钮
        btnRoomMinus.setDisable(roomCount <= MIN_ROOMS);
        btnRoomPlus.setDisable(roomCount >= MAX_ROOMS);
        
        // 成人按钮
        btnAdultMinus.setDisable(adultCount <= MIN_ADULTS);
        
        int totalGuests = adultCount + childCount;
        int maxGuestsAllowed = roomCount * MAX_GUESTS_PER_ROOM;
        
        btnAdultPlus.setDisable(totalGuests >= maxGuestsAllowed);
        btnChildPlus.setDisable(totalGuests >= maxGuestsAllowed);
        
        // 儿童按钮
        btnChildMinus.setDisable(childCount <= 0);
        
        // 更新禁用按钮样式
        updateButtonStyle(btnRoomMinus);
        updateButtonStyle(btnRoomPlus);
        updateButtonStyle(btnAdultMinus);
        updateButtonStyle(btnAdultPlus);
        updateButtonStyle(btnChildMinus);
        updateButtonStyle(btnChildPlus);
    }
    
    /**
     * 更新按钮样式
     */
    private void updateButtonStyle(Button button) {
        if (button.isDisabled()) {
            button.setStyle(
                "-fx-background-color: #f5f5f5; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-text-fill: #cccccc; " +
                "-fx-border-radius: 50%; " +
                "-fx-background-radius: 50%; " +
                "-fx-min-width: 40px; " +
                "-fx-min-height: 40px; " +
                "-fx-font-size: 20px;"
            );
        } else {
            button.setStyle(
                "-fx-background-color: white; " +
                "-fx-border-color: #cccccc; " +
                "-fx-border-radius: 50%; " +
                "-fx-background-radius: 50%; " +
                "-fx-min-width: 40px; " +
                "-fx-min-height: 40px; " +
                "-fx-font-size: 20px; " +
                "-fx-cursor: hand;"
            );
        }
    }
    
    /**
     * 重置为默认值
     */
    @FXML
    private void handleReset() {
        System.out.println("🔄 重置选择");
        
        roomCount = 1;
        adultCount = 1;
        childCount = 0;
        childrenAges.clear();
        
        updateDisplay();
        updateChildrenAgeSelectors();
        updateButtons();
    }
    
    /**
     * 完成选择
     */
    @FXML
    private void handleDone() {
        System.out.println("✅ 选择完成");
        System.out.println("房间: " + roomCount);
        System.out.println("成人: " + adultCount);
        System.out.println("儿童: " + childCount);
        
        if (childCount > 0) {
            System.out.println("儿童年龄: " + childrenAges);
        }
        
        // 关闭选择器
        toggleSelector();
    }
    
    /**
     * 获取选择数据的方法（供其他页面调用）
     */
    public int getRoomCount() {
        return roomCount;
    }
    
    public int getAdultCount() {
        return adultCount;
    }
    
    public int getChildCount() {
        return childCount;
    }
    
    public List<Integer> getChildrenAges() {
        return new ArrayList<>(childrenAges);
    }
}

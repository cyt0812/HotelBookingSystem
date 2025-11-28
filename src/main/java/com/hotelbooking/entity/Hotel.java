package com.hotelbooking.entity;

public class Hotel {
    private Integer id;
    private String name;
    private String location;
    private String description;
    private Integer availableRooms;
    
    // 默认构造器
    public Hotel() {}
    
    // 带参数构造器 - 修正了这里的语法错误
    public Hotel(String name, String location, String description, Integer availableRooms) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.availableRooms = availableRooms;
    }
    
    // Getter 和 Setter
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getAvailableRooms() {
        return availableRooms;
    }
    
    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
    }
    
    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", availableRooms=" + availableRooms +
                '}';
    }
}
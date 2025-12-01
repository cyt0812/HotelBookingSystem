/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotelbooking.entity;

/**
 *
 * @author a1-6
 */
public class Hotel {
    private int hotelId;
    private String name;
    private String address;
    private String description;
    private String amenities;

    public Hotel() {
    }

    public Hotel(String name, String address, String description, String amenities) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.amenities = amenities;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
    
    




}

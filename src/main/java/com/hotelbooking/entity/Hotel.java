package com.hotelbooking.entity;

public class Hotel {
    private int hotelId;
    private String name;
    private String address;
    private String description;
    private String amenities;

    public Hotel() {}

    public Hotel(String name, String address, String description, String amenities) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.amenities = amenities;
    }

    // Getter 和 Setter
    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    @Override
    public String toString() {
        return "Hotel{hotelId=" + hotelId + ", name='" + name + "', address='" + address + "'}";
    }
}
package com.hotelbooking.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class PaymentTest {

    @Test
    public void testConstructorAndGetters() {
        Hotel hotel = new Hotel("Sunshine", "123 Street", "Nice place", "Pool, WiFi");

        assertEquals("Sunshine", hotel.getName());
        assertEquals("123 Street", hotel.getAddress());
        assertEquals("Nice place", hotel.getDescription());
        assertEquals("Pool, WiFi", hotel.getAmenities());
    }

    @Test
    public void testSetters() {
        Hotel hotel = new Hotel();
        hotel.setHotelId(5);
        hotel.setName("Ocean View");
        hotel.setAddress("Sea Road 10");
        hotel.setDescription("Beautiful scenery");
        hotel.setAmenities("Gym, Breakfast");

        assertEquals(5, hotel.getHotelId());
        assertEquals("Ocean View", hotel.getName());
        assertEquals("Sea Road 10", hotel.getAddress());
        assertEquals("Beautiful scenery", hotel.getDescription());
        assertEquals("Gym, Breakfast", hotel.getAmenities());
    }
}

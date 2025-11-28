package com.hotelbooking.controller;

import com.hotelbooking.dto.ApiResponse;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.service.RoomService;
import com.hotelbooking.exception.GlobalExceptionHandler;
import java.math.BigDecimal;
import java.util.List;

public class HotelController {
    private final HotelService hotelService;
    private final RoomService roomService;
    
    public HotelController(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }
    
    /**
     * 创建酒店
     */
    public ApiResponse<Object> createHotel(String name, String location, String description, Integer availableRooms) {
        try {
            Hotel hotel = hotelService.createHotel(name, location, description, availableRooms);
            return ApiResponse.success("酒店创建成功", hotel);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据ID获取酒店
     */
    public ApiResponse<Object> getHotelById(Integer hotelId) {
        try {
            Hotel hotel = hotelService.getHotelById(hotelId)
                    .orElseThrow(() -> new RuntimeException("酒店不存在，ID: " + hotelId));
            return ApiResponse.success("获取酒店信息成功", hotel);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取所有酒店
     */
    public ApiResponse<Object> getAllHotels() {
        try {
            List<Hotel> hotels = hotelService.getAllHotels();
            return ApiResponse.success("获取酒店列表成功", hotels);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据位置获取酒店
     */
    public ApiResponse<Object> getHotelsByLocation(String location) {
        try {
            List<Hotel> hotels = hotelService.getHotelsByLocation(location);
            return ApiResponse.success("获取位置酒店列表成功", hotels);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 更新酒店信息
     */
    public ApiResponse<Object> updateHotel(Hotel hotel) {
        try {
            boolean result = hotelService.updateHotel(hotel);
            String message = result ? "酒店信息更新成功" : "酒店信息更新失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 删除酒店
     */
    public ApiResponse<Object> deleteHotel(Integer hotelId) {
        try {
            boolean result = hotelService.deleteHotel(hotelId);
            String message = result ? "酒店删除成功" : "酒店删除失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 创建房间
     */
    public ApiResponse<Object> createRoom(Integer hotelId, String roomNumber, String roomType, 
                                      BigDecimal price, boolean available) {
        try {
            Room room = roomService.createRoom(hotelId, roomNumber, roomType, price, available);
            return ApiResponse.success("房间创建成功", room);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据ID获取房间
     */
    public ApiResponse<Object> getRoomById(Integer roomId) {
        try {
            Room room = roomService.getRoomById(roomId)
                    .orElseThrow(() -> new RuntimeException("房间不存在，ID: " + roomId));
            return ApiResponse.success("获取房间信息成功", room);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取酒店的所有房间
     */
    public ApiResponse<Object> getRoomsByHotel(Integer hotelId) {
        try {
            List<Room> rooms = roomService.getRoomsByHotelId(hotelId);
            return ApiResponse.success("获取酒店房间列表成功", rooms);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 获取酒店的可用房间
     */
    public ApiResponse<Object> getAvailableRoomsByHotel(Integer hotelId) {
        try {
            List<Room> rooms = roomService.getAvailableRoomsByHotelId(hotelId);
            return ApiResponse.success("获取可用房间列表成功", rooms);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 根据房型获取房间
     */
    public ApiResponse<Object> getRoomsByType(String roomType) {
        try {
            List<Room> rooms = roomService.getRoomsByType(roomType);
            return ApiResponse.success("获取房型房间列表成功", rooms);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 更新房间信息
     */
    public ApiResponse<Object> updateRoom(Room room) {
        try {
            boolean result = roomService.updateRoom(room);
            String message = result ? "房间信息更新成功" : "房间信息更新失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 更新房间可用状态
     */
    public ApiResponse<Object> updateRoomAvailability(Integer roomId, boolean available) {
        try {
            boolean result = roomService.updateRoomAvailability(roomId, available);
            String message = result ? "房间状态更新成功" : "房间状态更新失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 删除房间
     */
    public ApiResponse<Object> deleteRoom(Integer roomId) {
        try {
            boolean result = roomService.deleteRoom(roomId);
            String message = result ? "房间删除成功" : "房间删除失败";
            return ApiResponse.success(message, result);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 搜索酒店（根据名称和位置）
     */
    public ApiResponse<Object> searchHotels(String name, String location) {
        try {
            List<Hotel> allHotels = hotelService.getAllHotels();
            
            List<Hotel> filteredHotels = allHotels.stream()
                    .filter(hotel -> 
                        (name == null || hotel.getName().toLowerCase().contains(name.toLowerCase())) &&
                        (location == null || hotel.getLocation().toLowerCase().contains(location.toLowerCase()))
                    )
                    .toList();
            
            return ApiResponse.success("酒店搜索成功", filteredHotels);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
    
    /**
     * 搜索可用房间（根据酒店、房型、价格范围）
     */
    public ApiResponse<Object> searchAvailableRooms(Integer hotelId, String roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            List<Room> availableRooms = roomService.getAvailableRoomsByHotelId(hotelId);
            
            List<Room> filteredRooms = availableRooms.stream()
                    .filter(room -> 
                        (roomType == null || room.getRoomType().equalsIgnoreCase(roomType)) &&
                        (minPrice == null || room.getPrice().compareTo(minPrice) >= 0) &&
                        (maxPrice == null || room.getPrice().compareTo(maxPrice) <= 0)
                    )
                    .toList();
            
            return ApiResponse.success("房间搜索成功", filteredRooms);
        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e);
        }
    }
}
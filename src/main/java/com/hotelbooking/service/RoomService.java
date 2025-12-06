package com.hotelbooking.service;

import com.hotelbooking.dao.RoomDAO;
import com.hotelbooking.entity.Room;
import com.hotelbooking.exception.BusinessException;
import com.hotelbooking.exception.ErrorType;
import com.hotelbooking.exception.ValidationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class RoomService {
    private RoomDAO roomDAO;
    
    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }
    
    /**
     * Create room
     */
    public Room createRoom(Integer hotelId, String roomNumber, String roomType, 
                      BigDecimal price, boolean available) {
    try {
        if (hotelId == null) {
            throw new ValidationException("Hotel ID cannot be empty");
        }
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new ValidationException("Room number cannot be empty");
        }
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new ValidationException("Room type cannot be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be positive");
        }
        
        // Check if room number already exists
        if (roomDAO.isRoomNumberExists(hotelId, roomNumber)) {
            throw new BusinessException(ErrorType.ROOM_ALREADY_EXISTS);
        }
        
        int defaultMaxOccupancy = 2;
        String defaultDescription = roomType.trim() + " room"; // Add default description
        
        // Use 7-parameter constructor
        Room room = new Room(hotelId, roomNumber.trim(), roomType.trim(), 
                            price.doubleValue(), defaultMaxOccupancy, 
                            available, defaultDescription);
        
        return roomDAO.createRoom(room);
    } catch (BusinessException | ValidationException e) {
        throw e;
    } catch (Exception e) {
        throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
            "Failed to create room: " + e.getMessage(), e);
    }
}
    
    /**
     * Get room by ID
     */
    public Optional<Room> getRoomById(Integer id) {
        try {
            return roomDAO.getRoomById(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get room information: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all rooms of a hotel
     */
    public List<Room> getRoomsByHotelId(Integer hotelId) {
        try {
             if (hotelId == null || hotelId <= 0) {
            throw new ValidationException("Hotel ID cannot be empty");
        }
            return roomDAO.getRoomsByHotelId(hotelId);
        } catch (ValidationException e) {
        throw e;
    }catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get hotel rooms: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get available rooms of a hotel
     */
    public List<Room> getAvailableRoomsByHotelId(Integer hotelId) {
        try {
               if (hotelId == null || hotelId <= 0) {
            throw new ValidationException("Hotel ID cannot be empty");
        }
            return roomDAO.getAvailableRoomsByHotelId(hotelId);
        } catch (ValidationException e) {
        throw e;
    } catch (Exception e) {
          throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
            "Failed to get available rooms: " + e.getMessage(), e);
    }
    }
    
    /**
     * Get rooms by type
     */
    public List<Room> getRoomsByType(String roomType) {
        try {
            if (roomType == null || roomType.trim().isEmpty()) {
                return List.of();
            }
            return roomDAO.getRoomsByType(roomType.trim());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get rooms by type: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get rooms by price range
     */
    public List<Room> getRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            if (minPrice == null || maxPrice == null) {
                throw new ValidationException("Price range cannot be empty");
            }
            if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("Price cannot be negative");
            }
            if (minPrice.compareTo(maxPrice) > 0) {
                throw new ValidationException("Minimum price cannot be greater than maximum price");
            }
            
            return roomDAO.getRoomsByPriceRange(minPrice, maxPrice);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get rooms by price range: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get available rooms of a specific type in a hotel
     */
    public List<Room> getAvailableRoomsByHotelAndType(Integer hotelId, String roomType) {
        try {
            if (hotelId == null) {
                throw new ValidationException("Hotel ID cannot be empty");
            }
            if (roomType == null || roomType.trim().isEmpty()) {
                return List.of();
            }
            
            return roomDAO.getAvailableRoomsByHotelAndType(hotelId, roomType.trim());
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get available rooms by type: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update room price
     */
    public boolean updateRoomPrice(Integer roomId, BigDecimal newPrice) {
        try {
            if (roomId == null) {
                throw new ValidationException("Room ID cannot be empty");
            }
            if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Price must be positive");
            }
            
            return roomDAO.updateRoomPrice(roomId, newPrice);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to update room price: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if room number already exists in hotel
     */
    public boolean isRoomNumberExists(Integer hotelId, String roomNumber) {
        try {
            if (hotelId == null) {
                throw new ValidationException("Hotel ID cannot be empty");
            }
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                throw new ValidationException("Room number cannot be empty");
            }
            
            return roomDAO.isRoomNumberExists(hotelId, roomNumber.trim());
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to check room number existence: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all available rooms
     */
    public List<Room> getAllAvailableRooms() {
        try {
            return roomDAO.getAllAvailableRooms();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get all available rooms: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get room count statistics for a hotel
     */
    public int getRoomCountByHotel(Integer hotelId) {
        try {
            if (hotelId == null) {
                throw new ValidationException("Hotel ID cannot be empty");
            }
            return roomDAO.getRoomCountByHotel(hotelId);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get room count statistics: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get available room count of a hotel
     */
    public int getAvailableRoomCountByHotel(Integer hotelId) {
        try {
            if (hotelId == null) {
                throw new ValidationException("Hotel ID cannot be empty");
            }
            return roomDAO.getAvailableRoomCountByHotel(hotelId);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to get available room count: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update room information
     */
    public boolean updateRoom(Room room) {
        try {
            if (room == null || room.getRoomId() == 0) {
                return false;
            }
            return roomDAO.updateRoom(room);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to update room information: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update room availability
     */
    public boolean updateRoomAvailability(Integer roomId, boolean available) {
        try {
            Optional<Room> roomOpt = roomDAO.getRoomById(roomId);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setIsAvailable(available);
                return roomDAO.updateRoom(room);
            }
            return false;
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to update room availability: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete room
     */
    public boolean deleteRoom(Integer roomId) {
        try {
            return roomDAO.deleteRoom(roomId);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to delete room: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if room is available
     */
    public boolean isRoomAvailable(Integer roomId) {
        try {
             if (roomId == null || roomId <= 0) {
            throw new ValidationException("Room ID cannot be empty or invalid");
        }
            Optional<Room> room = roomDAO.getRoomById(roomId);
              if (room.isEmpty()) {
            throw new BusinessException(ErrorType.ROOM_NOT_FOUND);
        }
            return room.isPresent() && room.get().isAvailable();
        } catch (Exception e) {
            throw new BusinessException(ErrorType.INTERNAL_SERVER_ERROR, 
                "Failed to check room availability: " + e.getMessage(), e);
        }
    }
}
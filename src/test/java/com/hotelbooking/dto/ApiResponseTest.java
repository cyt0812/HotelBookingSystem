package com.hotelbooking.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseTest {

    @Test
    public void testConstructor() {
        // Arrange & Act
        ApiResponse<String> response = new ApiResponse<>(true, "Test message", "Test data", "TEST_ERROR");

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
        assertEquals("Test data", response.getData());
        assertEquals("TEST_ERROR", response.getErrorCode());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testSuccessWithData() {
        // Arrange & Act
        String testData = "Success data";
        ApiResponse<String> response = ApiResponse.success(testData);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Operation successful", response.getMessage());
        assertEquals(testData, response.getData());
        assertNull(response.getErrorCode());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testSuccessWithMessageAndData() {
        // Arrange & Act
        String testMessage = "Custom success message";
        Integer testData = 123;
        ApiResponse<Integer> response = ApiResponse.success(testMessage, testData);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals(testMessage, response.getMessage());
        assertEquals(testData, response.getData());
        assertNull(response.getErrorCode());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testErrorWithMessage() {
        // Arrange & Act
        String errorMessage = "Custom error message";
        ApiResponse<Void> response = ApiResponse.error(errorMessage);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        assertNull(response.getErrorCode());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testErrorWithMessageAndCode() {
        // Arrange & Act
        String errorMessage = "Custom error message";
        String errorCode = "ERROR_404";
        ApiResponse<Void> response = ApiResponse.error(errorMessage, errorCode);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        assertEquals(errorCode, response.getErrorCode());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testHasErrorCode() {
        // Arrange
        ApiResponse<String> responseWithErrorCode = new ApiResponse<>(false, "Error", null, "ERROR_CODE");
        ApiResponse<String> responseWithoutErrorCode = new ApiResponse<>(true, "Success", "Data", null);

        // Act & Assert
        assertTrue(responseWithErrorCode.hasErrorCode());
        assertFalse(responseWithoutErrorCode.hasErrorCode());
    }

    @Test
    public void testGenericTypeHandling() {
        // Arrange - Test with different data types
        Integer intData = 123;
        Double doubleData = 45.67;
        String stringData = "test string";
        Object objData = new Object();

        // Act
        ApiResponse<Integer> intResponse = ApiResponse.success(intData);
        ApiResponse<Double> doubleResponse = ApiResponse.success(doubleData);
        ApiResponse<String> stringResponse = ApiResponse.success(stringData);
        ApiResponse<Object> objResponse = ApiResponse.success(objData);

        // Assert
        assertEquals(intData, intResponse.getData());
        assertEquals(doubleData, doubleResponse.getData());
        assertEquals(stringData, stringResponse.getData());
        assertEquals(objData, objResponse.getData());
    }
}

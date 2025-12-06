package com.hotelbooking.validation;

import com.hotelbooking.exception.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserInputValidatorTest {

    // Test validateEmail method
    @Test
    public void testValidateEmail_ValidEmail() {
        // Arrange & Act - Valid email should not throw exception
        assertDoesNotThrow(() -> UserInputValidator.validateEmail("test@example.com"));
        assertDoesNotThrow(() -> UserInputValidator.validateEmail("user.name123@domain.co.uk"));
    }

    @Test
    public void testValidateEmail_NullEmail() {
        // Arrange & Act - Null email should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validateEmail(null));
        // Assert
        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    public void testValidateEmail_EmptyEmail() {
        // Arrange & Act - Empty email should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validateEmail(""));
        // Assert
        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    public void testValidateEmail_InvalidFormat() {
        // Arrange & Act - Invalid email format should throw ValidationException
        ValidationException exception1 = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validateEmail("invalid-email"));
        ValidationException exception2 = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validateEmail("invalid@.com"));
        ValidationException exception3 = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validateEmail("invalid.com"));
        // Assert
        assertEquals("Invalid email format", exception1.getMessage());
        assertEquals("Invalid email format", exception2.getMessage());
        assertEquals("Invalid email format", exception3.getMessage());
    }

    // Test validatePassword method
    @Test
    public void testValidatePassword_ValidPassword() {
        // Arrange & Act - Valid password should not throw exception
        assertDoesNotThrow(() -> UserInputValidator.validatePassword("password123"));
        assertDoesNotThrow(() -> UserInputValidator.validatePassword("123456")); // Minimum length
    }

    @Test
    public void testValidatePassword_NullPassword() {
        // Arrange & Act - Null password should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validatePassword(null));
        // Assert
        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    public void testValidatePassword_EmptyPassword() {
        // Arrange & Act - Empty password should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validatePassword(""));
        // Assert
        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    public void testValidatePassword_TooShort() {
        // Arrange & Act - Password shorter than 6 characters should throw ValidationException
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validatePassword("12345"));
        // Assert
        assertEquals("Password must be at least 6 characters", exception.getMessage());
    }

    // Test validatePhone method
    @Test
    public void testValidatePhone_ValidPhone() {
        // Arrange & Act - Valid phone should not throw exception
        assertDoesNotThrow(() -> UserInputValidator.validatePhone("13812345678"));
        assertDoesNotThrow(() -> UserInputValidator.validatePhone("19912345678"));
    }

    @Test
    public void testValidatePhone_NullPhone() {
        // Arrange & Act - Null phone should not throw exception (optional field)
        assertDoesNotThrow(() -> UserInputValidator.validatePhone(null));
    }

    @Test
    public void testValidatePhone_EmptyPhone() {
        // Arrange & Act - Empty phone should not throw exception (optional field)
        assertDoesNotThrow(() -> UserInputValidator.validatePhone(""));
    }

    @Test
    public void testValidatePhone_InvalidFormat() {
        // Arrange & Act - Invalid phone format should throw ValidationException
        ValidationException exception1 = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validatePhone("1234567890")); // Too short
        ValidationException exception2 = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validatePhone("98765432101")); // Too long
        ValidationException exception3 = assertThrows(ValidationException.class, 
            () -> UserInputValidator.validatePhone("1234567890a")); // Contains letters
        // Assert
        assertEquals("Invalid phone number format", exception1.getMessage());
        assertEquals("Invalid phone number format", exception2.getMessage());
        assertEquals("Invalid phone number format", exception3.getMessage());
    }
}

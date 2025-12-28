package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateTransportTest {

    private FileValidateTransport validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateTransport();
    }

    @Test
    void isValid_shouldReturnTrue_whenTransportIsValid() {
        String[] content = new String[7];
        content[4] = "Bus";

        assertTrue(validator.isValid(content));
    }

    @Test
    void isValid_shouldReturnFalse_whenTransportIsTooLong() {
        String[] content = new String[7];
        content[4] = "A".repeat(100); // exactly 100 chars

        assertFalse(validator.isValid(content));
    }

    @Test
    void isValid_shouldReturnFalse_whenTransportIsNull() {
        String[] content = new String[7];
        content[4] = null;

        assertThrows(NullPointerException.class, () -> validator.isValid(content));
    }

    @Test
    void getErrorCode_shouldReturnInvalidTransport() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();
        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.INVALID_TRANSPORT, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnTransportStrategy() {
        assertEquals("TransportStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn10() {
        assertEquals(10, validator.getValidationPriority());
    }
}

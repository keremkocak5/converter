package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateUUIDTest {

    private FileValidateUUID validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateUUID();
    }

    @Test
    void isValid_shouldReturnTrue_whenUUIDIsValid() {
        String[] content = new String[7];
        content[0] = UUID.randomUUID().toString();

        assertTrue(validator.isValid(content));
    }

    @Test
    void isValid_shouldReturnFalse_whenUUIDIsInvalid() {
        String[] content = new String[7];
        content[0] = "invalid-uuid";

        assertFalse(validator.isValid(content));
    }

    @Test
    void isValid_shouldReturnFalse_whenUUIDIsNull() {
        String[] content = new String[7];
        content[0] = null;

        assertFalse(validator.isValid(content));
    }

    @Test
    void getErrorCode_shouldReturnInvalidUUID() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();
        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.INVALID_UUID, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnUUIDStrategy() {
        assertEquals("UUIDStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn10() {
        assertEquals(10, validator.getValidationPriority());
    }
}

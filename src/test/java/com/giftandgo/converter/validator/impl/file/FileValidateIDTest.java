package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateIDTest {

    private FileValidateID validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateID();
    }

    @Test
    void isValid_shouldReturnTrue_whenIDIsValid() {
        String[] content = new String[]{"any", "123X123D456", "other"};

        boolean result = validator.isValid(content);

        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenIDIsInvalid() {
        String[] content = new String[]{"any", "abcX123D456", "other", null};

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenIDIsEmpty() {
        String[] content = new String[]{"any", "", "other"};

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void getErrorCode_shouldReturnInvalidID() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.INVALID_ID, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnIDStrategy() {
        assertEquals("IDStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn10() {
        assertEquals(10, validator.getValidationPriority());
    }
}

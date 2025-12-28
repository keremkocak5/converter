package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateEmptyLineTest {

    private FileValidateEmptyLine validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateEmptyLine();
    }

    @Test
    void isValid_shouldReturnFalse_whenContentIsEmpty() {
        String[] content = new String[0];

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnTrue_whenContentIsNotEmpty() {
        String[] content = new String[]{"value"};

        boolean result = validator.isValid(content);

        assertTrue(result);
    }

    @Test
    void getErrorCode_shouldReturnEmptyLineErrorCode() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.EMPTY_LINE, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnEmptyLineStrategy() {
        assertEquals("EmptyLineStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturnOne() {
        assertEquals(1, validator.getValidationPriority());
    }
}

package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateDelimiterCountTest {

    private FileValidateDelimiterCount validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateDelimiterCount();
    }

    @Test
    void isValid_shouldReturnTrue_whenContentLengthIsSeven() {
        String[] content = new String[7];

        boolean result = validator.isValid(content);

        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenContentLengthIsLessThanSeven() {
        String[] content = new String[6];

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenContentLengthIsGreaterThanSeven() {
        String[] content = new String[8];

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void getErrorCode_shouldReturnIncorrectDelimiters() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.INCORRECT_DELIMITERS, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnDelimiterCountStrategy() {
        assertEquals("DelimiterCountStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturnTwo() {
        assertEquals(2, validator.getValidationPriority());
    }
}

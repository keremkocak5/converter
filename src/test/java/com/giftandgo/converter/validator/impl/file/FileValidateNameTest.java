package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateNameTest {

    private FileValidateName validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateName();
    }

    @Test
    void isValid_shouldReturnTrue_whenNameIsNotEmptyAndLessThan100() {
        String[] content = new String[]{"a", "b", "Valid Name"};

        boolean result = validator.isValid(content);

        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenNameIsEmpty() {
        String[] content = new String[]{"a", "b", ""};

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenNameIsNull() {
        String[] content = new String[]{"a", "b", null};

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenNameLengthIs100OrMore() {
        String longName = "N".repeat(100);
        String[] content = new String[]{"a", "b", longName};

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void getErrorCode_shouldReturnInvalidName() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.INVALID_NAME, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnNameStrategy() {
        assertEquals("NameStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn10() {
        assertEquals(10, validator.getValidationPriority());
    }
}

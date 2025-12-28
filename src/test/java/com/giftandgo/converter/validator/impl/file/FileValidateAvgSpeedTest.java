package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateAvgSpeedTest {

    private final FileValidateAvgSpeed validator = new FileValidateAvgSpeed();

    @Test
    void isValid_shouldReturnTrue_whenAvgSpeedIsValidNumber() {
        String[] content = new String[7];
        content[5] = "123.45";

        boolean result = validator.isValid(content);

        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenAvgSpeedIsNotANumber() {
        String[] content = new String[7];
        content[5] = "abc";

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenAvgSpeedIsNull() {
        String[] content = new String[7];
        content[5] = null;

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void getErrorCode_shouldReturnInvalidAvgSpeedError() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.INVALID_AVG_SPEED, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnCorrectKey() {
        assertEquals("AvgSpeedStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn10() {
        assertEquals(10, validator.getValidationPriority());
    }
}

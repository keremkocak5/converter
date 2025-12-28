package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateTopSpeedTest {

    private FileValidateTopSpeed validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateTopSpeed();
    }

    @Test
    void isValid_shouldReturnTrue_whenTopSpeedIsValidNumber() {
        String[] content = new String[7];
        content[6] = "120.5";

        assertTrue(validator.isValid(content));
    }

    @Test
    void isValid_shouldReturnFalse_whenTopSpeedIsInvalidNumber() {
        String[] content = new String[7];
        content[6] = "abc";

        assertFalse(validator.isValid(content));
    }

    @Test
    void isValid_shouldReturnFalse_whenTopSpeedIsNull() {
        String[] content = new String[7];
        content[6] = null;

        assertFalse(validator.isValid(content));
    }

    @Test
    void getErrorCode_shouldReturnInvalidTopSpeed() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();
        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.INVALID_TOP_SPEED, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnTopSpeedStrategy() {
        assertEquals("TopSpeedStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn10() {
        assertEquals(10, validator.getValidationPriority());
    }
}

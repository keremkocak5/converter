package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileValidateNothingTest {

    private FileValidateNothing validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateNothing();
    }

    @Test
    void isValid_shouldAlwaysReturnTrue() {
        String[] content1 = {};
        String[] content2 = {"a", "b", "c"};

        assertTrue(validator.isValid(content1));
        assertTrue(validator.isValid(content2));
    }

    @Test
    void getErrorCode_shouldReturnEmptyOptional() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isEmpty());
    }

    @Test
    void getValidationKey_shouldReturnNoValidationStrategy() {
        assertEquals("NoValidationStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn0() {
        assertEquals(0, validator.getValidationPriority());
    }
}

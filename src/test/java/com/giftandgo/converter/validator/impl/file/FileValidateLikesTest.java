package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileValidateLikesTest {

    private FileValidateLikes validator;

    @BeforeEach
    void setUp() {
        validator = new FileValidateLikes();
    }

    @Test
    void isValid_shouldReturnTrue_whenLikesLengthIsLessThan100() {
        String[] content = new String[]{"a", "b", "c", "short likes"};

        boolean result = validator.isValid(content);

        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnFalse_whenLikesLengthIs100OrMore() {
        String longLikes = "L".repeat(100);
        String[] content = new String[]{"a", "b", "c", longLikes};

        boolean result = validator.isValid(content);

        assertFalse(result);
    }

    @Test
    void getErrorCode_shouldReturnInvalidLikes() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.INVALID_LIKES, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnLikesStrategy() {
        assertEquals("LikesStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn10() {
        assertEquals(10, validator.getValidationPriority());
    }
}

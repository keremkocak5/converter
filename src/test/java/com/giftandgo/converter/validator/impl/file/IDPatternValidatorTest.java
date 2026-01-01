package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDPatternValidatorTest {

    private final IDPatternValidator validator = IDPatternValidator.INSTANCE;

    @Test
    void shouldReturnTrueWhenIdMatchesExpectedPattern() {
        // given
        // Pattern: ^(\d+)X\1D\d+$
        // Example: 12X12D345
        String[] content = {"12X12D345"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenNumbersDoNotMatchBackReference() {
        // given
        // 12X13D345 → back reference broken
        String[] content = {"12X13D345"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenFormatIsInvalid() {
        // given
        String[] content = {"ABC123"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenValueIsNull() {
        // given
        String[] content = {null};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenIndexIsOutOfBounds() {
        // given
        String[] content = {"12X12D345"};

        // when / then
        assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> validator.test(content, 5)
        );
    }

    @Test
    void shouldThrowExceptionWhenContentIsNull() {
        // when / then
        assertThrows(
                NullPointerException.class,
                () -> validator.test(null, 0)
        );
    }
}

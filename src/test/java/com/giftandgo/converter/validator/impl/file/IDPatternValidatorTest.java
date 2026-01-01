package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDPatternValidatorTest {

    private final IDPatternValidator validator = IDPatternValidator.INSTANCE;

    @Test
    void shouldReturnTrueWhenIdMatchesExpectedPattern() {
        String[] content = {"12X12D345"};

        boolean result = validator.test(content, 0);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenNumbersDoNotMatchBackReference() {
        String[] content = {"12X13D345"};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenFormatIsInvalid() {
        String[] content = {"ABC123"};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenValueIsNull() {
        String[] content = {null};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenIndexIsOutOfBounds() {
        String[] content = {"12X12D345"};

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> validator.test(content, 5));
    }

    @Test
    void shouldThrowExceptionWhenContentIsNull() {
        assertThrows(NullPointerException.class, () -> validator.test(null, 0));
    }
}

package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleValidatorTest {

    private final DoubleValidator validator = DoubleValidator.INSTANCE;

    @Test
    void shouldReturnTrueWhenValueIsValidDouble() {
        String[] content = {"1.23", "42", "-3.14", "0.0"};

        boolean result = validator.test(content, 0);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenValueIsNotDouble() {
        String[] content = {"abc", "12.3.4", "NaNish"};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueForScientificNotation() {
        String[] content = {"1e10"};

        boolean result = validator.test(content, 0);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenIndexIsOutOfBounds() {
        String[] content = {"1.23"};

        boolean result = validator.test(content, 5);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenContentIsNull() {
        boolean result = validator.test(null, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenValueIsNull() {
        String[] content = {null};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }
}

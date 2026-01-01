package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleValidatorTest {

    private final DoubleValidator validator = DoubleValidator.INSTANCE;

    @Test
    void shouldReturnTrueWhenValueIsValidDouble() {
        // given
        String[] content = {"1.23", "42", "-3.14", "0.0"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenValueIsNotDouble() {
        // given
        String[] content = {"abc", "12.3.4", "NaNish"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueForScientificNotation() {
        // given
        String[] content = {"1e10"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenIndexIsOutOfBounds() {
        // given
        String[] content = {"1.23"};

        // when
        boolean result = validator.test(content, 5);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenContentIsNull() {
        // when
        boolean result = validator.test(null, 0);

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
}

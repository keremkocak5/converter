package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DelimiterCount7ValidatorTest {

    private final DelimiterCount7Validator validator = DelimiterCount7Validator.INSTANCE;

    @Test
    void shouldReturnTrueWhenContentHasExactlySevenElements() {
        String[] content = {"a", "b", "c", "d", "e", "f", "g"};

        boolean result = validator.test(content, 0);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenContentHasLessThanSevenElements() {
        String[] content = {"a", "b", "c"};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenContentHasMoreThanSevenElements() {
        String[] content = {"a", "b", "c", "d", "e", "f", "g", "h"};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenContentIsNull() {
        boolean result = validator.test(null, 0);

        assertFalse(result);
    }

    @Test
    void associatedColumnIsIgnoredButDoesNotBreakValidation() {
        String[] content = {"a", "b", "c", "d", "e", "f", "g"};

        boolean result = validator.test(content, 999);

        assertTrue(result);
    }
}

package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DelimiterCount7ValidatorTest {

    private final DelimiterCount7Validator validator = DelimiterCount7Validator.INSTANCE;

    @Test
    void shouldReturnTrueWhenContentHasExactlySevenElements() {
        // given
        String[] content = {
                "a", "b", "c", "d", "e", "f", "g"
        };

        // when
        boolean result = validator.test(content, 0);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenContentHasLessThanSevenElements() {
        // given
        String[] content = {
                "a", "b", "c"
        };

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenContentHasMoreThanSevenElements() {
        // given
        String[] content = {
                "a", "b", "c", "d", "e", "f", "g", "h"
        };

        // when
        boolean result = validator.test(content, 0);

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
    void associatedColumnIsIgnoredButDoesNotBreakValidation() {
        // given
        String[] content = {
                "a", "b", "c", "d", "e", "f", "g"
        };

        // when
        boolean result = validator.test(content, 999);

        // then
        assertTrue(result);
    }
}

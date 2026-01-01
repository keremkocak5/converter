package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UUIDValidatorTest {

    private final UUIDValidator validator = UUIDValidator.INSTANCE;

    @Test
    void shouldReturnTrueWhenValidUuid() {
        // given
        String[] content = {"550e8400-e29b-41d4-a716-446655440000"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenInvalidUuid() {
        // given
        String[] content = {"not-a-uuid"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenUuidIsNull() {
        // given
        String[] content = {null};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenIndexIsOutOfBounds() {
        // given
        String[] content = {"550e8400-e29b-41d4-a716-446655440000"};

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
}

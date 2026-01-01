package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UUIDValidatorTest {

    private final UUIDValidator validator = UUIDValidator.INSTANCE;

    @Test
    void shouldReturnTrueWhenValidUuid() {
        String[] content = {"550e8400-e29b-41d4-a716-446655440000"};

        boolean result = validator.test(content, 0);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenInvalidUuid() {
        String[] content = {"not-a-uuid"};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenUuidIsNull() {
        String[] content = {null};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenIndexIsOutOfBounds() {
        String[] content = {"550e8400-e29b-41d4-a716-446655440000"};

        boolean result = validator.test(content, 5);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenContentIsNull() {
        boolean result = validator.test(null, 0);

        assertFalse(result);
    }
}

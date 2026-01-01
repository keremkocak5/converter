package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringNotEmptyLessThan100ValidatorTest {

    private final StringNotEmptyLessThan100Validator validator = StringNotEmptyLessThan100Validator.INSTANCE;

    @Test
    void shouldReturnTrueWhenStringIsNotEmptyAndLessThan100Chars() {
        String[] content = {"valid-string"};

        boolean result = validator.test(content, 0);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenStringIsEmpty() {
        String[] content = {""};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenStringIsNull() {
        String[] content = {null};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenStringIsExactly100Characters() {
        String value = "a".repeat(100);
        String[] content = {value};

        boolean result = validator.test(content, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenStringIs99Characters() {
        String value = "a".repeat(99);
        String[] content = {value};

        boolean result = validator.test(content, 0);

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenIndexIsOutOfBounds() {
        String[] content = {"test"};

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> validator.test(content, 5));
    }

    @Test
    void shouldThrowExceptionWhenContentIsNull() {
        assertThrows(NullPointerException.class, () -> validator.test(null, 0));
    }
}

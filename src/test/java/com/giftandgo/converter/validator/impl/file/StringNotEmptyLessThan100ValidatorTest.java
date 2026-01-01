package com.giftandgo.converter.validator.impl.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringNotEmptyLessThan100ValidatorTest {

    private final StringNotEmptyLessThan100Validator validator =
            StringNotEmptyLessThan100Validator.INSTANCE;

    @Test
    void shouldReturnTrueWhenStringIsNotEmptyAndLessThan100Chars() {
        // given
        String[] content = {"valid-string"};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenStringIsEmpty() {
        // given
        String[] content = {""};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenStringIsNull() {
        // given
        String[] content = {null};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenStringIsExactly100Characters() {
        // given
        String value = "a".repeat(100);
        String[] content = {value};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenStringIs99Characters() {
        // given
        String value = "a".repeat(99);
        String[] content = {value};

        // when
        boolean result = validator.test(content, 0);

        // then
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenIndexIsOutOfBounds() {
        // given
        String[] content = {"test"};

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

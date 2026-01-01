package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.validator.Validatable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IpValidatorFactoryTest {

    private Validatable<IpDetails> validatorA;
    private Validatable<IpDetails> validatorB;
    private Validatable<IpDetails> validatorC;

    private IpValidatorFactory factory;

    @BeforeEach
    void setUp() {
        validatorA = mockValidator("A", 5);
        validatorB = mockValidator("B", 20);
        validatorC = mockValidator("C", 10);

        factory = new IpValidatorFactory(
                List.of(validatorA, validatorB, validatorC), null
        );
    }

    @Test
    void shouldReturnOnlyValidatorsMatchingStrategies() {
        // given
        ReflectionTestUtils.setField(factory, "strategies", List.of("A", "C"));

        // when
        List<Validatable<IpDetails>> result = factory.getValidators();

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(validatorA));
        assertTrue(result.contains(validatorC));
        assertFalse(result.contains(validatorB));
    }

    @Test
    void shouldSortValidatorsByPriorityAscending() {
        // given
        ReflectionTestUtils.setField(factory, "strategies", List.of("A", "B", "C"));

        // when
        List<Validatable<IpDetails>> result = factory.getValidators();

        // then
        assertEquals(validatorB, result.get(2)); // priority 20
        assertEquals(validatorC, result.get(1)); // priority 10
        assertEquals(validatorA, result.get(0)); // priority 5
    }

    @Test
    void shouldReturnEmptyListWhenNoStrategiesConfigured() {
        // given
        ReflectionTestUtils.setField(factory, "strategies", List.of());

        // when
        List<Validatable<IpDetails>> result = factory.getValidators();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoValidatorsMatchStrategies() {
        // given
        ReflectionTestUtils.setField(factory, "strategies", List.of("X", "Y"));

        // when
        List<Validatable<IpDetails>> result = factory.getValidators();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ----------------------------------------------------
    // Helper
    // ----------------------------------------------------

    private Validatable<IpDetails> mockValidator(String key, int priority) {
        Validatable<IpDetails> validator = Mockito.mock(Validatable.class);
        Mockito.when(validator.getValidationKey()).thenReturn(key);
        Mockito.when(validator.getValidationPriority()).thenReturn(priority);
        return validator;
    }
}

package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.TransportFileValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransportFileValidatorFactoryTest {

    private TransportFileValidatorFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TransportFileValidatorFactory();
    }

    @Test
    void shouldReturnEmptyListWhenValidationDisabled() {
        // given
        ReflectionTestUtils.setField(factory, "validationEnabled", false);

        // when
        List<TransportFileValidator> validators = factory.getValidators();

        // then
        assertNotNull(validators);
        assertTrue(validators.isEmpty());
    }

    @Test
    void shouldReturnAllValidatorsWhenValidationEnabled() {
        // given
        ReflectionTestUtils.setField(factory, "validationEnabled", true);

        // when
        List<TransportFileValidator> validators = factory.getValidators();

        // then
        assertNotNull(validators);
        assertEquals(
                TransportFileValidator.values().length,
                validators.size()
        );
    }

    @Test
    void shouldReturnValidatorsSortedByPriorityDescending() {
        // given
        ReflectionTestUtils.setField(factory, "validationEnabled", true);

        // when
        List<TransportFileValidator> validators = factory.getValidators();

        // then
        for (int i = 0; i < validators.size() - 1; i++) {
            int currentPriority = validators.get(i).getValidationPriority();
            int nextPriority = validators.get(i + 1).getValidationPriority();

            assertTrue(
                    currentPriority >= nextPriority,
                    "Validators are not sorted by priority descending"
            );
        }
    }
}

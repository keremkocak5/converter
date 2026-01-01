package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.TransportFileValidator;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.TransportOutcomeContent;
import com.giftandgo.converter.validator.impl.file.TransportFileValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static com.giftandgo.converter.enums.ErrorCode.INCORRECT_FILE_FORMAT;
import static org.junit.jupiter.api.Assertions.*;

class TransportFileReaderServiceTest {

    private TransportFileValidatorFactory validatorFactory;
    private TransportFileReaderService service;

    @BeforeEach
    void setUp() {
        validatorFactory = Mockito.mock(TransportFileValidatorFactory.class);
        service = new TransportFileReaderService(validatorFactory);
    }

    // ------------------------------------------------------
    // Happy path
    // ------------------------------------------------------

    @Test
    void shouldValidateAndMapSuccessfully() {
        Mockito.when(validatorFactory.getValidators())
                .thenReturn(List.of());

        List<String[]> lines =
                java.util.Collections.singletonList(
                        new String[]{"id", "x", "CAR", "y", "BIKE", "z", "120.5"}
                );


        assertDoesNotThrow(() -> service.validateContent(lines));

        TransportOutcomeContent mapped =
                service.getLineToOutputMapper(lines.get(0));

        assertEquals("CAR", mapped.name());
        assertEquals("BIKE", mapped.transport());
        assertEquals(120.5, mapped.topSpeed());
    }

    // ------------------------------------------------------
    // Validation failure
    // ------------------------------------------------------

    @Test
    void shouldThrowWhenValidatorFails() {
        TransportFileValidator failingValidator =
                Mockito.mock(TransportFileValidator.class);

        Mockito.when(failingValidator.getValidationPriority()).thenReturn(1);
        Mockito.when(failingValidator.getAssociatedColumn()).thenReturn(0);
        Mockito.when(failingValidator.getColumnName()).thenReturn("UUID");
        Mockito.when(failingValidator.getValidator())
                .thenReturn((content, col) -> false);

        Mockito.when(validatorFactory.getValidators())
                .thenReturn(List.of(failingValidator));

        List<String[]> lines =
                java.util.Collections.singletonList(
                        new String[]{"bad"}
                );

        ConverterRuntimeException ex = assertThrows(
                ConverterRuntimeException.class,
                () -> service.validateContent(lines)
        );

        assertEquals(INCORRECT_FILE_FORMAT, ex.getErrorCode());
    }

    // ------------------------------------------------------
    // Multiple validators → lowest priority first
    // ------------------------------------------------------

    @Test
    void shouldApplyValidatorsInPriorityOrder() {
        TransportFileValidator highPriority = Mockito.mock(TransportFileValidator.class);
        TransportFileValidator lowPriority = Mockito.mock(TransportFileValidator.class);

        Mockito.when(lowPriority.getValidationPriority()).thenReturn(5);
        Mockito.when(highPriority.getValidationPriority()).thenReturn(1);

        Mockito.when(highPriority.getAssociatedColumn()).thenReturn(0);
        Mockito.when(highPriority.getColumnName()).thenReturn("UUID");
        Mockito.when(highPriority.getValidator())
                .thenReturn((c, i) -> false);

        Mockito.when(validatorFactory.getValidators())
                .thenReturn(List.of(lowPriority, highPriority));

        List<String[]> lines =
                java.util.Collections.singletonList(
                        new String[]{"x"}
                );

        ConverterRuntimeException ex = assertThrows(
                ConverterRuntimeException.class,
                () -> service.validateContent(lines)
        );

        assertEquals(INCORRECT_FILE_FORMAT, ex.getErrorCode());
    }
}

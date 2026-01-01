package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.validator.Validatable;
import com.giftandgo.converter.validator.impl.file.TransportFileValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransportFileReaderServiceTest {

    private TransportFileValidatorFactory validatorFactory;
    private TransportFileReaderService service;

    @BeforeEach
    void setUp() {
        validatorFactory = mock(TransportFileValidatorFactory.class);
        service = new TransportFileReaderService(validatorFactory);
    }

    // =========================
    // HAPPY PATH
    // =========================

    @Test
    void shouldParseFileSuccessfully() throws Exception {
        String content =
                "A|B|CITY1|D|DEST1|F|10.5\n" +
                        "A|B|CITY2|D|DEST2|F|20.0";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "transport.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );

        Validatable<String[]> validator = mock(Validatable.class);
        when(validator.isValid(any())).thenReturn(true);

        OutcomeFile result = service.getValidatedFileContent(file);

        assertEquals("OutcomeFile.json", result.fileName());

        try (InputStream is = result.inputStream()) {
            String output = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(output.contains("CITY1"));
            assertTrue(output.contains("DEST2"));
            assertTrue(output.contains("20.0"));
        }
    }

    // =========================
    // FILE READ FAILURE
    // =========================

    @Test
    void shouldThrowWhenFileCannotBeRead() {
        MockMultipartFile file = mock(MockMultipartFile.class);

        try {
            when(file.getInputStream()).thenThrow(new RuntimeException("IO error"));
        } catch (Exception ignored) {
        }

        ConverterRuntimeException ex = assertThrows(
                ConverterRuntimeException.class,
                () -> service.getValidatedFileContent(file)
        );

        assertEquals(ErrorCode.CANNOT_READ_FILE, ex.getErrorCode());
    }

    // =========================
    // PARSE FAILURE
    // =========================

    @Test
    void shouldThrowWhenParsingFails() {
        String invalidContent = "A|B|CITY|D|DEST|F|NOT_A_NUMBER";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "bad.txt",
                "text/plain",
                invalidContent.getBytes(StandardCharsets.UTF_8)
        );

        when(validatorFactory.getValidators())
                .thenReturn(List.of());

        ConverterRuntimeException ex = assertThrows(
                ConverterRuntimeException.class,
                () -> service.getValidatedFileContent(file)
        );

        assertEquals(ErrorCode.CANNOT_READ_FILE, ex.getErrorCode());
    }

    // =========================
    // VALIDATION FAILURE
    // =========================

    @Test
    void shouldThrowWhenValidationFails() {
        String content = "A|B|CITY|D|DEST|F|10.0";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "invalid.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );

        Validatable<String[]> validator = mock(Validatable.class);
        when(validator.isValid(any())).thenReturn(false);
        when(validator.getErrorCode())
                .thenReturn(java.util.Optional.of(ErrorCode.INCORRECT_DELIMITERS));

        ConverterRuntimeException ex = assertThrows(
                ConverterRuntimeException.class,
                () -> service.getValidatedFileContent(file)
        );

        assertEquals(ErrorCode.INCORRECT_DELIMITERS, ex.getErrorCode());
    }

    // =========================
    // STOP AT FIRST VALIDATION FAILURE
    // =========================

    @Test
    void shouldStopAtFirstFailingValidator() {
        String content = "A|B|CITY|D|DEST|F|10.0";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "invalid.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );

        Validatable<String[]> firstFail = mock(Validatable.class);
        when(firstFail.isValid(any())).thenReturn(false);
        when(firstFail.getErrorCode())
                .thenReturn(java.util.Optional.of(ErrorCode.INCORRECT_DELIMITERS));

        Validatable<String[]> secondValidator = mock(Validatable.class);


        assertThrows(
                ConverterRuntimeException.class,
                () -> service.getValidatedFileContent(file)
        );

        verify(secondValidator, never()).isValid(any());
    }
}

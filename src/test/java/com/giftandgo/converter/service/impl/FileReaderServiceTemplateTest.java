package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.util.FileReadWriteUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileReaderServiceTemplateTest {

    private final TestFileReaderService service = new TestFileReaderService();

    @Test
    void shouldReadValidateAndReturnOutcomeFile() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

        List<String[]> lines = List.of(
                new String[]{"a", "b"},
                new String[]{"c", "d"}
        );

        try (MockedStatic<FileReadWriteUtil> util = mockStatic(FileReadWriteUtil.class)) {
            util.when(() -> FileReadWriteUtil.read(any(InputStream.class), any()))
                    .thenReturn(lines);

            util.when(() -> FileReadWriteUtil.write(any(), any()))
                    .thenAnswer(inv -> null);

            OutcomeFile result = service.getValidatedFileContent(file);

            assertNotNull(result);
            assertEquals("test.json", result.fileName());
            assertNotNull(result.inputStream());
        }
    }

    @Test
    void shouldThrowWhenReadFails() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new RuntimeException());

        try (MockedStatic<FileReadWriteUtil> util = mockStatic(FileReadWriteUtil.class)) {
            ConverterRuntimeException ex = assertThrows(
                    ConverterRuntimeException.class,
                    () -> service.getValidatedFileContent(file)
            );

            assertEquals(ErrorCode.CANNOT_READ_FILE, ex.getErrorCode());
        }
    }

    @Test
    void shouldThrowWhenValidationFails() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));


        List<String[]> lines = List.of(
                new String[]{"INVALID"},
                new String[]{"INVALID"}
        );

        try (MockedStatic<FileReadWriteUtil> util = mockStatic(FileReadWriteUtil.class)) {
            util.when(() -> FileReadWriteUtil.read(any(), any()))
                    .thenReturn(lines);

            ConverterRuntimeException ex = assertThrows(
                    ConverterRuntimeException.class,
                    () -> service.getValidatedFileContent(file)
            );

            assertEquals(ErrorCode.CANNOT_READ_FILE, ex.getErrorCode());
        }
    }

    @Test
    void shouldThrowWhenParsingFails() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

        List<String[]> lines = List.of(
                new String[]{"OK"},
                new String[]{"BOOM"}
        );

        try (MockedStatic<FileReadWriteUtil> util = mockStatic(FileReadWriteUtil.class)) {
            util.when(() -> FileReadWriteUtil.read(any(), any()))
                    .thenReturn(lines);

            ConverterRuntimeException ex = assertThrows(
                    ConverterRuntimeException.class,
                    () -> service.getValidatedFileContent(file)
            );

            assertEquals(ErrorCode.CANNOT_READ_FILE, ex.getErrorCode());
        }
    }

    // ------------------------------------------------
    // Test-only concrete implementation
    // ------------------------------------------------

    static class TestFileReaderService extends FileReaderServiceTemplate<String> {

        @Override
        void validateContent(List<String[]> lines) {
            if (lines.stream().anyMatch(l -> l.length < 2)) {
                throw new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
            }
        }

        @Override
        Pattern getDelimiterPattern() {
            return Pattern.compile(",");
        }

        @Override
        String getLineToOutputMapper(String[] delimitedPart) {
            if ("BOOM".equals(delimitedPart[0])) {
                throw new RuntimeException("Parse error");
            }
            return delimitedPart[0];
        }

        @Override
        String getFileName() {
            return "test.json";
        }
    }
}

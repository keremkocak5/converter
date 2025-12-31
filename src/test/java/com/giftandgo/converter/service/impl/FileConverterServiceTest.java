package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.OutcomeFile;
import com.giftandgo.converter.service.ConversionLogPersistable;
import com.giftandgo.converter.service.FileReadable;
import com.giftandgo.converter.service.IpValidatable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileConverterServiceTest {

    private ConversionLogPersistable conversionLogService;
    private FileReadable fileReaderService;
    private IpValidatable validateIpService;

    private FileConverterService service;

    private MultipartFile file;
    private String ip;
    private String uri;
    private ConversionLog conversionLog;

    @BeforeEach
    void setUp() {
        conversionLogService = mock(ConversionLogPersistable.class);
        fileReaderService = mock(FileReadable.class);
        validateIpService = mock(IpValidatable.class);

        service = new FileConverterService(
                conversionLogService,
                fileReaderService,
                validateIpService
        );

        file = mock(MultipartFile.class);
        ip = "1.2.3.4";
        uri = "/test-uri";
        conversionLog = new ConversionLog(uri, ip);

        when(conversionLogService.create(uri, ip)).thenReturn(conversionLog);
    }

    // =========================
    // HAPPY PATH
    // =========================

    @Test
    void convertFile_shouldReturnOutcomeFile_andUpdateLogWith200() {
        OutcomeFile outcomeFile =
                new OutcomeFile("output.json", new ByteArrayInputStream(new byte[]{1, 2, 3}));

        when(fileReaderService.getValidatedFileContent(file))
                .thenReturn(outcomeFile);

        OutcomeFile result = service.convertFile(file, ip, uri);

        assertNotNull(result);
        assertEquals("output.json", result.fileName());

        ArgumentCaptor<ConversionLog> logCaptor =
                ArgumentCaptor.forClass(ConversionLog.class);

        verify(conversionLogService).update(logCaptor.capture());

        ConversionLog updatedLog = logCaptor.getValue();
        assertEquals(HttpStatus.OK.value(), updatedLog.getHttpResponseCode());
        assertTrue(updatedLog.getTimeLapsed() >= 0);

        verify(validateIpService)
                .saveIpDetailsAndRunIpValidationRules(conversionLog, ip);
        verify(fileReaderService).getValidatedFileContent(file);
    }

    // =========================
    // IP VALIDATION FAILURE
    // =========================

    @Test
    void convertFile_shouldThrowException_andUpdateLog_whenIpValidationFails() {
        ConverterRuntimeException ex =
                new ConverterRuntimeException(ErrorCode.INCORRECT_DELIMITERS);

        doThrow(ex)
                .when(validateIpService)
                .saveIpDetailsAndRunIpValidationRules(conversionLog, ip);

        ConverterRuntimeException thrown =
                assertThrows(
                        ConverterRuntimeException.class,
                        () -> service.convertFile(file, ip, uri)
                );

        assertSame(ex, thrown);

        verify(conversionLogService).update(conversionLog);
        verifyNoInteractions(fileReaderService);
    }

    // =========================
    // FILE VALIDATION FAILURE
    // =========================

    @Test
    void convertFile_shouldThrowException_andUpdateLog_whenFileValidationFails() {
        ConverterRuntimeException ex =
                new ConverterRuntimeException(ErrorCode.INVALID_UUID);

        doNothing()
                .when(validateIpService)
                .saveIpDetailsAndRunIpValidationRules(conversionLog, ip);

        when(fileReaderService.getValidatedFileContent(file))
                .thenThrow(ex);

        ConverterRuntimeException thrown =
                assertThrows(
                        ConverterRuntimeException.class,
                        () -> service.convertFile(file, ip, uri)
                );

        assertSame(ex, thrown);

        verify(conversionLogService).update(conversionLog);
    }

    // =========================
    // UNEXPECTED RUNTIME ERROR
    // =========================

    @Test
    void convertFile_shouldPropagateRuntimeException_andNotUpdateLog() {
        RuntimeException ex = new RuntimeException("DB down");

        when(conversionLogService.create(uri, ip))
                .thenThrow(ex);

        RuntimeException thrown =
                assertThrows(
                        RuntimeException.class,
                        () -> service.convertFile(file, ip, uri)
                );

        assertSame(ex, thrown);

        verify(conversionLogService, never()).update(any());
    }
}

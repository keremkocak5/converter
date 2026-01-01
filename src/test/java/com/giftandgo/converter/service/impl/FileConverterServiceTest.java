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
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileConverterServiceTest {

    private ConversionLogPersistable conversionLogService;
    private FileReadable fileReaderService;
    private IpValidatable ipValidatable;
    private FileConverterService service;
    private MultipartFile file;
    private ConversionLog conversionLog;

    @BeforeEach
    void setUp() {
        conversionLogService = Mockito.mock(ConversionLogPersistable.class);
        fileReaderService = Mockito.mock(FileReadable.class);
        ipValidatable = Mockito.mock(IpValidatable.class);

        service = new FileConverterService(conversionLogService, fileReaderService, ipValidatable);

        file = Mockito.mock(MultipartFile.class);
        conversionLog = Mockito.mock(ConversionLog.class);

        Mockito.when(conversionLogService.create("uri", "1.2.3.4")).thenReturn(conversionLog);
        Mockito.when(conversionLog.setExecutionResults(Mockito.anyLong(), Mockito.anyInt())).thenReturn(conversionLog);
    }

    // ------------------------------------------------------
    // 1. Happy path
    // ------------------------------------------------------

    @Test
    void shouldConvertFileSuccessfully() {
        OutcomeFile outcomeFile = new OutcomeFile("result.json", new ByteArrayInputStream(new byte[]{1, 2}));

        Mockito.when(fileReaderService.getValidatedFileContent(file)).thenReturn(outcomeFile);

        OutcomeFile result = service.convertFile(file, "1.2.3.4", "uri");

        assertNotNull(result);
        assertEquals("result.json", result.fileName());
        InOrder inOrder = Mockito.inOrder(conversionLogService, ipValidatable, fileReaderService);
        inOrder.verify(conversionLogService).create("uri", "1.2.3.4");
        inOrder.verify(ipValidatable).saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4");
        inOrder.verify(fileReaderService).getValidatedFileContent(file);
        inOrder.verify(conversionLogService).update(conversionLog);
    }

    // ------------------------------------------------------
    // 2. IP validation fails
    // ------------------------------------------------------

    @Test
    void shouldUpdateLogAndRethrowWhenIpValidationFails() {
        ConverterRuntimeException exception = new ConverterRuntimeException(ErrorCode.RESTRICTED_ISP);
        Mockito.doThrow(exception).when(ipValidatable).saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4");

        ConverterRuntimeException thrown = assertThrows(ConverterRuntimeException.class, () -> service.convertFile(file, "1.2.3.4", "uri"));

        assertEquals(ErrorCode.RESTRICTED_ISP, thrown.getErrorCode());
        Mockito.verify(conversionLogService).update(conversionLog);
        Mockito.verifyNoInteractions(fileReaderService);
    }

    // ------------------------------------------------------
    // 3. File reading fails
    // ------------------------------------------------------

    @Test
    void shouldUpdateLogAndRethrowWhenFileReadFails() {
        ConverterRuntimeException exception = new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE);
        Mockito.when(fileReaderService.getValidatedFileContent(file)).thenThrow(exception);

        ConverterRuntimeException thrown = assertThrows(ConverterRuntimeException.class, () -> service.convertFile(file, "1.2.3.4", "uri"));

        assertEquals(ErrorCode.CANNOT_READ_FILE, thrown.getErrorCode());
        Mockito.verify(ipValidatable).saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4");
        Mockito.verify(conversionLogService).update(conversionLog);
    }

    // ------------------------------------------------------
    // 4. Log is always created
    // ------------------------------------------------------

    @Test
    void shouldAlwaysCreateConversionLog() {
        Mockito.when(fileReaderService.getValidatedFileContent(file)).thenThrow(new ConverterRuntimeException(ErrorCode.CANNOT_READ_FILE));

        assertThrows(ConverterRuntimeException.class, () -> service.convertFile(file, "1.2.3.4", "uri"));

        Mockito.verify(conversionLogService).create("uri", "1.2.3.4");
    }
}

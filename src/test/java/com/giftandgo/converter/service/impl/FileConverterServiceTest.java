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
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileConverterServiceTest {

    private ConversionLogPersistable conversionLogService;
    private FileReadable<OutcomeFile> fileReaderService;
    private IpValidatable validateIpService;

    private FileConverterService service;

    private MultipartFile file;
    private String ip;
    private String uri;
    private ConversionLog conversionLog;
    private OutcomeFile outcomeFile;

    @BeforeEach
    void setUp() {
        conversionLogService = Mockito.mock(ConversionLogPersistable.class);
        fileReaderService = Mockito.mock(FileReadable.class);
        validateIpService = Mockito.mock(IpValidatable.class);

        service = new FileConverterService(conversionLogService, fileReaderService, validateIpService);

        file = Mockito.mock(MultipartFile.class);
        ip = "1.2.3.4";
        uri = "/test-uri";
        conversionLog = new ConversionLog(uri, ip);
        outcomeFile = new OutcomeFile("output.json", new ByteArrayInputStream(new byte[]{1, 2, 3}));

        Mockito.when(conversionLogService.create(uri, ip)).thenReturn(conversionLog);
        Mockito.when(fileReaderService.getValidatedFileContent(file)).thenReturn(outcomeFile);
        Mockito.when(conversionLogService.update(conversionLog)).thenReturn(conversionLog);
    }

    @Test
    void convertFileShouldReturnOutcomeFileWhenAllPasses() throws IOException {
        OutcomeFile result = service.convertFile(file, ip, uri);

        assertNotNull(result);
        assertEquals("output.json", result.fileName());

        byte[] actualBytes;
        try (InputStream is = result.inputStream()) {
            actualBytes = is.readAllBytes();
        }
        assertArrayEquals(new byte[]{1, 2, 3}, actualBytes);

        Mockito.verify(conversionLogService).create(uri, ip);
        Mockito.verify(validateIpService).saveIpDetailsAndRunIpValidationRules(conversionLog, ip);
        Mockito.verify(fileReaderService).getValidatedFileContent(file);
        Mockito.verify(conversionLogService).update(conversionLog);
    }

    @Test
    void convertFileShouldThrowConverterRuntimeExceptionAndUpdateLogWhenValidationFails() {
        ConverterRuntimeException ex = new ConverterRuntimeException(ErrorCode.INCORRECT_DELIMITERS);
        Mockito.doThrow(ex).when(validateIpService).saveIpDetailsAndRunIpValidationRules(conversionLog, ip);

        ConverterRuntimeException thrown = assertThrows(
                ConverterRuntimeException.class,
                () -> service.convertFile(file, ip, uri)
        );
        assertEquals(ex, thrown);

        Mockito.verify(conversionLogService).create(uri, ip);
        Mockito.verify(validateIpService).saveIpDetailsAndRunIpValidationRules(conversionLog, ip);
        Mockito.verify(conversionLogService).update(conversionLog);

        Mockito.verifyNoInteractions(fileReaderService);
    }
}

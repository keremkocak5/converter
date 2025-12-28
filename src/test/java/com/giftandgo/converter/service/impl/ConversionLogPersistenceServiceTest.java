package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.repository.ConversionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversionLogPersistenceServiceTest {

    @InjectMocks
    private ConversionLogPersistenceService service;

    @Mock
    private ConversionLogRepository repository;

    private ConversionLog conversionLog;

    @BeforeEach
    void setUp() {
        conversionLog = new ConversionLog("testUri", "127.0.0.1");
    }

    @Test
    void createShouldSaveAndReturnConversionLog() {
        when(repository.save(new ConversionLog("testUri", "127.0.0.1"))).thenReturn(conversionLog);

        ConversionLog result = service.create("testUri", "127.0.0.1");

        assertNotNull(result);
        assertEquals("testUri", result.getUri());
        assertEquals("127.0.0.1", result.getIp());
        verify(repository, times(1)).save(new ConversionLog("testUri", "127.0.0.1"));
    }

    @Test
    void updateShouldSaveAndReturnConversionLog() {
        when(repository.save(new ConversionLog("testUri", "127.0.0.1"))).thenReturn(conversionLog);

        ConversionLog result = service.update(conversionLog);

        assertNotNull(result);
        assertEquals("testUri", result.getUri());
        assertEquals("127.0.0.1", result.getIp());
        verify(repository, times(1)).save(conversionLog);
    }
}

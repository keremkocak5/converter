package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.ConversionLogPersistable;
import com.giftandgo.converter.service.IpDetailsRetrievable;
import com.giftandgo.converter.validator.Validatable;
import com.giftandgo.converter.validator.impl.ip.IpValidateByIsp;
import com.giftandgo.converter.validator.impl.ip.IpValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidateIpServiceTest {

    private IpDetailsRetrievable ipApiClient;
    private ConversionLogPersistable conversionLogService;
    private IpValidatorFactory ipValidatorFactory;
    private ValidateIpService service;
    private ConversionLog conversionLog;

    @BeforeEach
    void setUp() {
        ipApiClient = mock(IpDetailsRetrievable.class);
        conversionLogService = mock(ConversionLogPersistable.class);
        ipValidatorFactory = mock(IpValidatorFactory.class);

        service = new ValidateIpService(ipApiClient, conversionLogService, ipValidatorFactory);

        conversionLog = mock(ConversionLog.class);
        when(conversionLog.setIpDetails(anyString(), anyString())).thenReturn(conversionLog);
    }

    // =========================
    // NO VALIDATORS
    // =========================

    @Test
    void shouldDoNothingWhenNoValidatorsConfigured() {
        when(ipValidatorFactory.getValidators()).thenReturn(List.of());

        assertDoesNotThrow(() -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4"));

        verifyNoInteractions(ipApiClient);
        verifyNoInteractions(conversionLogService);
    }

    // =========================
    // IP API FAILS
    // =========================

    @Test
    void shouldThrowWhenIpApiCannotResolveIp() {
        Validatable<IpDetails> validator = mock(Validatable.class);
        when(ipValidatorFactory.getValidators()).thenReturn(List.of(validator));
        when(ipApiClient.getIpDetails("1.2.3.4")).thenReturn(Optional.empty());

        ConverterRuntimeException ex = assertThrows(ConverterRuntimeException.class, () -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4"));

        assertEquals(ErrorCode.IP_API_RESOLVE_ERROR, ex.getErrorCode());
        verify(conversionLogService, never()).update(any());
    }

    // =========================
    // VALIDATOR FAILS
    // =========================

    @Test
    void shouldThrowWhenIpValidationRuleFails() {
        IpDetails ipDetails = new IpDetails("US", "AWS");
        Validatable<IpDetails> failingValidator = new IpValidateByIsp(Set.of("AWS"));
        when(ipValidatorFactory.getValidators()).thenReturn(List.of(failingValidator));
        when(ipApiClient.getIpDetails("1.2.3.4")).thenReturn(Optional.of(ipDetails));

        ConverterRuntimeException ex = assertThrows(ConverterRuntimeException.class, () -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4"));

        assertEquals(ErrorCode.RESTRICTED_ISP, ex.getErrorCode());
        verify(conversionLogService).update(conversionLog);
    }

    // =========================
    // VALIDATOR FAILS BUT NO ERROR CODE
    // =========================

    @Test
    void shouldNotThrowWhenValidatorFailsWithoutErrorCode() {
        IpDetails ipDetails = new IpDetails("US", "AWS");
        Validatable<IpDetails> validator = mock(Validatable.class);
        when(validator.isValid(ipDetails)).thenReturn(false);
        when(validator.getErrorCode()).thenReturn(Optional.empty());
        when(ipValidatorFactory.getValidators()).thenReturn(List.of(validator));
        when(ipApiClient.getIpDetails("1.2.3.4")).thenReturn(Optional.of(ipDetails));

        assertDoesNotThrow(() -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4"));

        verify(conversionLogService).update(conversionLog);
    }

    // =========================
    // HAPPY PATH
    // =========================

    @Test
    void shouldSaveIpDetailsWhenAllValidatorsPass() {
        IpDetails ipDetails = new IpDetails("DE", "VODAFONE");
        Validatable<IpDetails> validator = mock(Validatable.class);
        when(validator.isValid(ipDetails)).thenReturn(true);
        when(ipValidatorFactory.getValidators()).thenReturn(List.of(validator));
        when(ipApiClient.getIpDetails("1.2.3.4")).thenReturn(Optional.of(ipDetails));

        assertDoesNotThrow(() -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4"));

        verify(conversionLogService).update(conversionLog);
    }

    // =========================
    // STOP AT FIRST FAILURE
    // =========================

    @Test
    void shouldStopAtFirstFailingValidator() {
        IpDetails ipDetails = new IpDetails("US", "AWS");
        Validatable<IpDetails> firstFail = mock(Validatable.class);
        when(firstFail.isValid(ipDetails)).thenReturn(false);
        when(firstFail.getErrorCode()).thenReturn(Optional.of(ErrorCode.RESTRICTED_COUNTRY));
        Validatable<IpDetails> secondValidator = mock(Validatable.class);
        when(ipValidatorFactory.getValidators()).thenReturn(List.of(firstFail, secondValidator));
        when(ipApiClient.getIpDetails("1.2.3.4")).thenReturn(Optional.of(ipDetails));

        ConverterRuntimeException ex = assertThrows(ConverterRuntimeException.class, () -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4"));

        assertEquals(ErrorCode.RESTRICTED_COUNTRY, ex.getErrorCode());
        verify(secondValidator, never()).isValid(ipDetails);
    }
}

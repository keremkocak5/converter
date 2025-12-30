package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.ConversionLog;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.ConversionLogPersistable;
import com.giftandgo.converter.service.IpDetailsRetrievable;
import com.giftandgo.converter.validator.Validatable;
import com.giftandgo.converter.validator.impl.ip.IpValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ValidateIpServiceTest { // kerem bunu elden gecir

    private IpDetailsRetrievable ipApiClient;
    private ConversionLogPersistable conversionLogService;
    private IpValidatorFactory ipValidatorFactory;

    private ValidateIpService service;

    private ConversionLog conversionLog;

    @BeforeEach
    void setUp() {
        ipApiClient = Mockito.mock(IpDetailsRetrievable.class);
        conversionLogService = Mockito.mock(ConversionLogPersistable.class);
        ipValidatorFactory = Mockito.mock(IpValidatorFactory.class);

        service = new ValidateIpService(
                ipApiClient,
                conversionLogService,
                ipValidatorFactory
        );

        conversionLog = Mockito.mock(ConversionLog.class);
        Mockito.when(conversionLog.setIpDetails(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(conversionLog);
    }

    // ------------------------------------------------------
    // 2. IP API returns empty → IP_API_RESOLVE_ERROR
    // ------------------------------------------------------

    @Test
    void shouldThrowWhenIpApiCannotResolveIp() {
        Validatable<IpDetails> validator = Mockito.mock(Validatable.class);
        Mockito.when(validator.getValidationKey()).thenReturn("SomeStrategy");

        Mockito.when(ipValidatorFactory.getValidators())
                .thenReturn(List.of(validator));

        Mockito.when(ipApiClient.getIpDetails("1.2.3.4"))
                .thenReturn(Optional.empty());

        ConverterRuntimeException ex = assertThrows(
                ConverterRuntimeException.class,
                () -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4")
        );

        assertEquals(ErrorCode.IP_API_RESOLVE_ERROR, ex.getErrorCode());
    }

    // ------------------------------------------------------
    // 3. Validator blocks IP → corresponding error thrown
    // ------------------------------------------------------

    @Test
    void shouldThrowWhenIpValidationRuleFails() {
        IpDetails ipDetails = new IpDetails("US", "AWS");

        Validatable<IpDetails> failingValidator = Mockito.mock(Validatable.class);
        Mockito.when(failingValidator.getValidationKey()).thenReturn("ValidateISP");
        Mockito.when(failingValidator.isValid(ipDetails)).thenReturn(false);
        Mockito.when(failingValidator.getErrorCode())
                .thenReturn(Optional.of(ErrorCode.RESTRICTED_ISP));

        Mockito.when(ipValidatorFactory.getValidators())
                .thenReturn(List.of(failingValidator));

        Mockito.when(ipApiClient.getIpDetails("1.2.3.4"))
                .thenReturn(Optional.of(ipDetails));

        ConverterRuntimeException ex = assertThrows(
                ConverterRuntimeException.class,
                () -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4")
        );

        assertEquals(ErrorCode.RESTRICTED_ISP, ex.getErrorCode());

        Mockito.verify(conversionLogService)
                .update(conversionLog);
    }

    // ------------------------------------------------------
    // 4. Happy path → IP saved, no exception
    // ------------------------------------------------------

    @Test
    void shouldSaveIpDetailsWhenAllValidatorsPass() {
        IpDetails ipDetails = new IpDetails("DE", "VODAFONE");

        Validatable<IpDetails> validator = Mockito.mock(Validatable.class);
        Mockito.when(validator.getValidationKey()).thenReturn("ValidateCountry");
        Mockito.when(validator.isValid(ipDetails)).thenReturn(true);

        Mockito.when(ipValidatorFactory.getValidators())
                .thenReturn(List.of(validator));

        Mockito.when(ipApiClient.getIpDetails("1.2.3.4"))
                .thenReturn(Optional.of(ipDetails));

        assertDoesNotThrow(() ->
                service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4")
        );

        Mockito.verify(conversionLogService)
                .update(conversionLog);
    }

    // ------------------------------------------------------
    // 5. Only first failing validator is applied
    // ------------------------------------------------------

    @Test
    void shouldStopAtFirstFailingValidator() {
        IpDetails ipDetails = new IpDetails("US", "AWS");

        Validatable<IpDetails> firstFail = Mockito.mock(Validatable.class);
        Mockito.when(firstFail.isValid(ipDetails)).thenReturn(false);
        Mockito.when(firstFail.getErrorCode())
                .thenReturn(Optional.of(ErrorCode.RESTRICTED_COUNTRY));

        Validatable<IpDetails> secondValidator = Mockito.mock(Validatable.class);

        Mockito.when(ipValidatorFactory.getValidators())
                .thenReturn(List.of(firstFail, secondValidator));

        Mockito.when(ipApiClient.getIpDetails("1.2.3.4"))
                .thenReturn(Optional.of(ipDetails));

        ConverterRuntimeException ex = assertThrows(
                ConverterRuntimeException.class,
                () -> service.saveIpDetailsAndRunIpValidationRules(conversionLog, "1.2.3.4")
        );

        assertEquals(ErrorCode.RESTRICTED_COUNTRY, ex.getErrorCode());

        Mockito.verify(secondValidator, Mockito.never()).isValid(ipDetails);
    }
}

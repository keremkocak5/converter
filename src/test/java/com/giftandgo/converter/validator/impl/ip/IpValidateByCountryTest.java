package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.model.IpDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IpValidateByCountryTest {

    private IpValidateByCountry validator;

    @BeforeEach
    void setUp() {
        Set<String> restrictedCountries = Set.of("US", "CA", "ES");
        validator = new IpValidateByCountry(restrictedCountries);
    }

    @Test
    void isValid_shouldReturnFalse_whenCountryIsRestricted() {
        IpDetails ipDetails = new IpDetails("US", "AWS");

        boolean result = validator.isValid(ipDetails);

        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnTrue_whenCountryIsNotRestricted() {
        IpDetails ipDetails = new IpDetails("DE", "AWS");

        boolean result = validator.isValid(ipDetails);

        assertTrue(result);
    }

    @Test
    void getErrorCode_shouldReturnRestrictedCountryError() {
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.RESTRICTED_COUNTRY, errorCode.get());
    }

    @Test
    void getValidationKey_shouldReturnCorrectKey() {
        assertEquals("ValidateCountryStrategy", validator.getValidationKey());
    }

    @Test
    void getValidationPriority_shouldReturn10() {
        assertEquals(10, validator.getValidationPriority());
    }
}

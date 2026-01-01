package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.model.IpDetails;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IpValidateByIspTest {

    @Test
    void shouldReturnTrueWhenIspIsNotRestricted() {
        Set<String> restrictedIsps = Set.of("AWS", "GCP");
        IpValidateByIsp validator = new IpValidateByIsp(restrictedIsps);
        IpDetails ipDetails = new IpDetails("DE", "VODAFONE");

        boolean result = validator.isValid(ipDetails);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenIspIsRestricted() {
        Set<String> restrictedIsps = Set.of("AWS", "GCP");
        IpValidateByIsp validator = new IpValidateByIsp(restrictedIsps);
        IpDetails ipDetails = new IpDetails("US", "AWS");

        boolean result = validator.isValid(ipDetails);

        assertFalse(result);
    }

    @Test
    void shouldReturnRestrictedIspErrorCode() {
        Set<String> restrictedIsps = Set.of("AWS");
        IpValidateByIsp validator = new IpValidateByIsp(restrictedIsps);

        Optional<ErrorCode> errorCode = validator.getErrorCode();

        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.RESTRICTED_ISP, errorCode.get());
    }

    @Test
    void shouldReturnCorrectValidationKey() {
        IpValidateByIsp validator = new IpValidateByIsp(Set.of());

        String key = validator.getValidationKey();

        assertEquals("IpValidateByIsp", key);
    }

    @Test
    void shouldReturnCorrectValidationPriority() {
        IpValidateByIsp validator = new IpValidateByIsp(Set.of());

        int priority = validator.getValidationPriority();

        assertEquals(10, priority);
    }
}

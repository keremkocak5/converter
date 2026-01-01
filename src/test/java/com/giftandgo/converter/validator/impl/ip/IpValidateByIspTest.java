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
        // given
        Set<String> restrictedIsps = Set.of("AWS", "GCP");
        IpValidateByIsp validator = new IpValidateByIsp(restrictedIsps);

        IpDetails ipDetails = new IpDetails("DE", "VODAFONE");

        // when
        boolean result = validator.isValid(ipDetails);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenIspIsRestricted() {
        // given
        Set<String> restrictedIsps = Set.of("AWS", "GCP");
        IpValidateByIsp validator = new IpValidateByIsp(restrictedIsps);

        IpDetails ipDetails = new IpDetails("US", "AWS");

        // when
        boolean result = validator.isValid(ipDetails);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnRestrictedIspErrorCode() {
        // given
        Set<String> restrictedIsps = Set.of("AWS");
        IpValidateByIsp validator = new IpValidateByIsp(restrictedIsps);

        // when
        Optional<ErrorCode> errorCode = validator.getErrorCode();

        // then
        assertTrue(errorCode.isPresent());
        assertEquals(ErrorCode.RESTRICTED_ISP, errorCode.get());
    }

    @Test
    void shouldReturnCorrectValidationKey() {
        // given
        IpValidateByIsp validator = new IpValidateByIsp(Set.of());

        // when
        String key = validator.getValidationKey();

        // then
        assertEquals("IpValidateByIsp", key);
    }

    @Test
    void shouldReturnCorrectValidationPriority() {
        // given
        IpValidateByIsp validator = new IpValidateByIsp(Set.of());

        // when
        int priority = validator.getValidationPriority();

        // then
        assertEquals(10, priority);
    }
}

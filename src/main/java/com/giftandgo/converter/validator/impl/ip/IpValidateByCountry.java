package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.validator.Validatable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IpValidateByCountry implements Validatable<IpDetails> {

    @Value("${ip.validation.restricted-countries}")
    private final Set<String> restrictedCountries;

    @Override
    public boolean isValid(IpDetails ipDetails) {
        return !restrictedCountries.contains(ipDetails.countryCode());
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.RESTRICTED_COUNTRY);
    }

    @Override
    public String getValidationKey() {
        return "IpValidateByCountry";
    }

    @Override
    public int getValidationPriority() {
        return 10;
    }
}

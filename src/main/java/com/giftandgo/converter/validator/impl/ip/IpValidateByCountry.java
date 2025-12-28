package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.giftandgo.converter.util.Constants.RESTRICTED_COUNTRIES;

@Service
public class IpValidateByCountry implements Validatable<IpDetails> {

    @Override
    public boolean isValid(IpDetails ipDetails) {
        return !RESTRICTED_COUNTRIES.contains(ipDetails.countryCode());
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.RESTRICTED_COUNTRY);
    }

    @Override
    public String getValidationStrategy() {
        return "ValidateCountryStrategy";
    }

    @Override
    public int getRulePriority() {
        return 10;
    }
}

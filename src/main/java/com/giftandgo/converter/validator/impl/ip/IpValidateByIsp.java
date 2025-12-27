package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.giftandgo.converter.util.Constants.RESTRICTED_ISPS;

@Service
public class IpValidateByIsp implements Validatable<IpDetails> {

    @Override
    public boolean isValid(IpDetails ipDetails) {
        if ( RESTRICTED_ISPS.contains(ipDetails.countryCode())) {
            // throw
        };
        return true;

    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.empty();
    }


    @Override
    public String getValidationStrategy() {
        return "ValidateISPStrategy";
    }
}

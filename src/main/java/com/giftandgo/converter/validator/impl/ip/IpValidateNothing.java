package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IpValidateNothing implements Validatable<IpDetails> {

    public static final String VALIDATE_NOTHING_STRATEGY_KEY = "ValidateNothingStrategy";

    @Override
    public boolean isValid(IpDetails ipDetails) {
        return true;
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.empty();
    }

    @Override
    public String getValidationKey() {
        return VALIDATE_NOTHING_STRATEGY_KEY;
    }

    @Override
    public int getValidationPriority() {
        return 0;
    }
}

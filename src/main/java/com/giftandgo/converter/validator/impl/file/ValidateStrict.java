package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidateStrict implements Validatable<List<String[]>> {

    @Override
    public boolean isValid(List content) {
        return true;
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.empty();
    }

    @Override
    public String getValidationStrategy() {
        return "StrictValidationStrategy";
    }
}

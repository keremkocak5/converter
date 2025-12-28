package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileValidateNothing implements Validatable<String[]> {

    @Override
    public boolean isValid(String[] content) {
        return true;
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.empty();
    }

    @Override
    public String getValidationStrategy() {
        return "NoValidationStrategy";
    }

    @Override
    public int getRulePriority() {
        return 0;
    }
}

package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileValidateDelimiterCount implements Validatable<String[]> {

    @Override
    public boolean isValid(String[] content) {
        return content.length == 7;
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.INCORRECT_DELIMITERS);
    }

    @Override
    public String getValidationStrategy() {
        return "DelimiterCountStrategy";
    }
}

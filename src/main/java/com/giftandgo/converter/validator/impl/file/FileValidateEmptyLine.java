package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileValidateEmptyLine implements Validatable<String[]> {

    @Override
    public boolean isValid(String[] content) {
        return content.length != 0;
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.EMPTY_LINE);
    }

    @Override
    public String getValidationKey() {
        return "EmptyLineStrategy";
    }

    @Override
    public int getValidationPriority() {
        return 1;
    }
}

package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class FileValidateID implements Validatable<String[]> {

    private static final Pattern ID_PATTERN = Pattern.compile("^(\\d+)X\\1D\\d+$");

    @Override
    public boolean isValid(String[] content) {
        return ID_PATTERN.matcher(content[1]).matches();
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.INVALID_ID);
    }

    @Override
    public String getValidationStrategy() {
        return "IDStrategy";
    }
}

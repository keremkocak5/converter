package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class FileValidateName implements Validatable<String[]> {

    private static final Pattern ID_PATTERN = Pattern.compile("^(\\d+)X\\1D\\d+$");

    @Override
    public boolean isValid(String[] content) {
        return StringUtils.isNotEmpty(content[2]) && content[2].length() < 100;
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.INVALID_NAME);
    }

    @Override
    public String getValidationStrategy() {
        return "NameStrategy";
    }
}

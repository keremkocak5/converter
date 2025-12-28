package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileValidateTransport implements Validatable<String[]> {

    @Override
    public boolean isValid(String[] content) {
        return content[4].length() < 100;
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.INVALID_TRANSPORT);
    }

    @Override
    public String getValidationStrategy() {
        return "TransportStrategy";
    }
}

package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FileValidateUUID implements Validatable<String[]> {

    @Override
    public boolean isValid(String[] content) {
        try {
            UUID uuid = UUID.fromString(content[0]);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.INVALID_UUID);
    }

    @Override
    public String getValidationStrategy() {
        return "UUIDStrategy";
    }

    @Override
    public int getRulePriority() {
        return 10;
    }
}

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
            UUID.fromString(content[0]);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.INVALID_UUID);
    }

    @Override
    public String getValidationKey() {
        return "UUIDStrategy";
    }

    @Override
    public int getValidationPriority() {
        return 10;
    }
}

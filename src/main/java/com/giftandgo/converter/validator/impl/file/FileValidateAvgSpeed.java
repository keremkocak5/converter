package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.validator.Validatable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileValidateAvgSpeed implements Validatable<String[]> {

    @Override
    public boolean isValid(String[] content) {
        try {
            Double.parseDouble(content[5]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Optional<ErrorCode> getErrorCode() {
        return Optional.of(ErrorCode.INVALID_AVG_SPEED);
    }

    @Override
    public String getValidationKey() {
        return "AvgSpeedStrategy";
    }

    @Override
    public int getValidationPriority() {
        return 10;
    }
}

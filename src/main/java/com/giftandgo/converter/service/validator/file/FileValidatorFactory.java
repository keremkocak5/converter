package com.giftandgo.converter.service.validator.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.service.FileValidateble;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class FileValidatorFactory {

    private final Set<FileValidateble> validators;

    @Value("${feature.flag.validation.strategy}")
    private String validationStrategyFeatureFlag;

    public FileValidateble getValidator() {
        return validators
                .stream()
                .filter(validator -> validator.getValidationStrategy().equals(validationStrategyFeatureFlag))
                .findFirst()
                .orElseThrow(() -> new ConverterRuntimeException(ErrorCode.NO_VALIDATION_STRATEGY_FOUND));
    }

}

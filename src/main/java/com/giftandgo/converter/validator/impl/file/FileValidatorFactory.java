package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.validator.Validatable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class FileValidatorFactory {

    private final Set<Validatable> validators;

    @Value("${feature.flag.validation.strategy}")
    private String validationStrategyFeatureFlag;

    public Validatable getValidator() {
        return validators
                .stream()
                .filter(validator -> validator.getValidationStrategy().equals(validationStrategyFeatureFlag))
                .findFirst()
                .orElseThrow(() -> new ConverterRuntimeException(ErrorCode.NO_FILE_VALIDATION_STRATEGY_FOUND));
    }

}

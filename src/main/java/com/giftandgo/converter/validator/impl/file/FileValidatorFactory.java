package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.validator.Validatable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileValidatorFactory {

    private final List<Validatable<String[]>> validators;

    @Value("${feature.flag.file.validation.strategies:[NoValidationStrategy]}")
    private final List<String> strategies;

    public List<Validatable<String[]>> getValidators() {
        return validators
                .stream()
                .sorted(Comparator.comparing(Validatable::getValidationPriority))
                .filter(validator -> strategies.contains(validator.getValidationKey()))
                .collect(Collectors.toList());
    }

}

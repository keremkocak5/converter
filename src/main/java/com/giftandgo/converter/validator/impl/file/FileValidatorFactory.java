package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.validator.Validatable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileValidatorFactory {

    private final Set<Validatable<String[]>> validators;

    @Value("${feature.flag.file.validation.strategies}")
    private List<String> strategies;

    public Set<Validatable<String[]>> getValidators() {
        return validators
                .stream()
                .filter(validator -> strategies.contains(validator.getValidationStrategy()))
                .collect(Collectors.toSet());
    }

}

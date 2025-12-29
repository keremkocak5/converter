package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.validator.Validatable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.giftandgo.converter.util.ValidatorUtil.filterAndSortValidators;

@RequiredArgsConstructor
@Service
public class TransportFileValidatorFactory {

    private final List<Validatable<String[]>> validators;

    @Value("${feature.flag.file.validation.strategies:[NoValidationStrategy]}") // kerem bunu bos array yap
    private final List<String> strategies;

    public List<Validatable<String[]>> getValidators() {
        return filterAndSortValidators(validators, strategies);
    }

}

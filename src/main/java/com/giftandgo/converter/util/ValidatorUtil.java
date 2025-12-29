package com.giftandgo.converter.util;

import com.giftandgo.converter.validator.Validatable;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ValidatorUtil {

    public static <T, K> List<Validatable<T>> filterAndSortValidators(
            List<Validatable<T>> validators,
            List<K> strategies) {
        return validators.stream()
                .sorted(Comparator.comparing(Validatable::getValidationPriority))
                .filter(validator -> strategies.contains(validator.getValidationKey()))
                .collect(Collectors.toList());
    }

}

package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.validator.Validatable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.giftandgo.converter.validator.impl.ip.IpValidateNothing.VALIDATE_NOTHING_STRATEGY_KEY;

@RequiredArgsConstructor
@Service
public class IpValidatorFactory {

    private final List<Validatable<IpDetails>> validators;

    @Value("${feature.flag.ip.validation.strategies:" + VALIDATE_NOTHING_STRATEGY_KEY + "}")
    private final List<String> strategies;

    public List<Validatable<IpDetails>> getValidators() {

        return validators
                .stream()
                .sorted(Comparator.comparing(Validatable::getValidationPriority))
                .filter(validator -> strategies.contains(validator.getValidationKey()))
                .collect(Collectors.toList());
    }

}

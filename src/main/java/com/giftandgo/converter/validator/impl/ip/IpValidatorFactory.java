package com.giftandgo.converter.validator.impl.ip;

import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.validator.Validatable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IpValidatorFactory {

    private final Set<Validatable<IpDetails>> validators;

    @Value("${feature.flag.ip.validation.strategies}")
    private List<String> strategies;

    public Set<Validatable<IpDetails>> getValidators() {
        return validators
                .stream()
                .filter(validator -> strategies.contains(validator.getValidationStrategy()))
                .collect(Collectors.toSet());
    }

}

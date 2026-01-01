package com.giftandgo.converter.validator.impl.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class TransportFileValidatorFactory {


    public List<Predicate> getValidators() {
        return List.of();
    }
}

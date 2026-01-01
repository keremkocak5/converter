package com.giftandgo.converter.validator.impl.file;

import com.giftandgo.converter.enums.TransportFileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransportFileValidatorFactory {

    @Value("${feature.flag.file.validation.enabled:true}")
    private boolean validationEnabled;

    public List<TransportFileValidator> getValidators() {
        return validationEnabled ? Arrays.stream(TransportFileValidator.values()).toList() : List.of();
    }
}

package com.giftandgo.converter.service.validator.file;

import com.giftandgo.converter.service.FileValidateble;

import java.util.List;

public class StrictValidation implements FileValidateble {

    @Override
    public void validate(List content) {

    }

    @Override
    public String getValidationStrategy() {
        return "StrictValidationStrategy";
    }
}

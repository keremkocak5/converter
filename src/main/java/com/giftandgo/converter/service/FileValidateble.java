package com.giftandgo.converter.service;

import java.util.List;

public interface FileValidateble {

    void validate(List<String[]> content);

    String getValidationStrategy();

}

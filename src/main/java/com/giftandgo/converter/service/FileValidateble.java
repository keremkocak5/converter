package com.giftandgo.converter.service;

import java.util.List;

public interface FileValidateble<T> {

    void validate(List<String[]> content);

    String getValidationStrategy();

}

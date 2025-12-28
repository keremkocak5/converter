package com.giftandgo.converter.validator;

import com.giftandgo.converter.enums.ErrorCode;

import java.util.Optional;

public interface Validatable<T> {

    boolean isValid(T content);

    Optional<ErrorCode> getErrorCode();

    String getValidationKey();

    int getValidationPriority();

}

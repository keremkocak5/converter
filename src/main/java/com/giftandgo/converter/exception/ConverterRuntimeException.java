package com.giftandgo.converter.exception;

import com.giftandgo.converter.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConverterRuntimeException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    public ConverterRuntimeException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getErrorMessage(), args));
        this.errorCode = errorCode;
    }


}

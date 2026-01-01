package com.giftandgo.converter.exception;

import com.giftandgo.converter.enums.ErrorCode;
import lombok.Getter;

public class ConverterRuntimeException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    public ConverterRuntimeException(ErrorCode errorCode, Object... args) {
        super(formatMessage(errorCode, args));
        this.errorCode = errorCode;
    }

    private static String formatMessage(ErrorCode errorCode, Object... args) {
        if (args == null || args.length == 0) {
            return errorCode.getErrorMessage();
        }
        return String.format(errorCode.getErrorMessage(), args);
    }
}


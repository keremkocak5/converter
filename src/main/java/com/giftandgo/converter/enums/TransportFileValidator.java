package com.giftandgo.converter.enums;

import com.giftandgo.converter.validator.impl.file.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiPredicate;

@Getter
@RequiredArgsConstructor
public enum TransportFileValidator {

    UUID(UUIDValidator.INSTANCE, 0, ErrorCode.INVALID_UUID, 10), // kerem sorted olmali
    ID(IDPatternValidator.INSTANCE, 1, ErrorCode.INVALID_ID, 10),
    NAME(StringNotEmptyLessThan100Validator.INSTANCE, 2, ErrorCode.INVALID_NAME, 10),
    LIKES(StringNotEmptyLessThan100Validator.INSTANCE, 3, ErrorCode.INVALID_LIKES, 10),
    TRANSPORT(StringNotEmptyLessThan100Validator.INSTANCE, 4, ErrorCode.INVALID_TRANSPORT, 10),
    AVG_SPEED(DoubleValidator.INSTANCE, 5, ErrorCode.INVALID_AVG_SPEED, 10),
    MAX_SPEED(DoubleValidator.INSTANCE, 6, ErrorCode.INVALID_TOP_SPEED, 10),
    DELIMITER_COUNT(DelimiterCountValidator.INSTANCE, 0, ErrorCode.INVALID_UUID, 0);

    private final BiPredicate validator;
    private final int associatedColumn;
    private final ErrorCode errorCode;
    private final int priority;

}

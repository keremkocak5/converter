package com.giftandgo.converter.enums;

import com.giftandgo.converter.validator.impl.file.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiPredicate;

@Getter
@RequiredArgsConstructor
public enum TransportFileValidator {

    DELIMITER_COUNT(DelimiterCountValidator.INSTANCE, 0, "Delimiter Count", 0),
    UUID(UUIDValidator.INSTANCE, 0, "UUID", 10),
    ID(IDPatternValidator.INSTANCE, 1, "Id", 10),
    NAME(StringNotEmptyLessThan100Validator.INSTANCE, 2, "Name", 10),
    LIKES(StringNotEmptyLessThan100Validator.INSTANCE, 3, "Likes", 10),
    TRANSPORT(StringNotEmptyLessThan100Validator.INSTANCE, 4, "Transport", 10),
    AVG_SPEED(DoubleValidator.INSTANCE, 5, "Average Speed", 10),
    TOP_SPEED(DoubleValidator.INSTANCE, 6, "Top Speed", 10);

    private final BiPredicate validator;
    private final int associatedColumn;
    private final String columnName;
    private final int validationPriority;

}

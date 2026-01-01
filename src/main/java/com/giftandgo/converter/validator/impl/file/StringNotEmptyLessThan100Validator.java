package com.giftandgo.converter.validator.impl.file;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class StringNotEmptyLessThan100Validator implements BiPredicate<String[], Integer> {

    public static final StringNotEmptyLessThan100Validator INSTANCE = new StringNotEmptyLessThan100Validator();

    private StringNotEmptyLessThan100Validator() {
    }

    @Override
    public boolean test(String[] content, Integer associatedColumn) {
        return StringUtils.isNotEmpty(content[associatedColumn]) && content[associatedColumn].length() < 100;
    }
}

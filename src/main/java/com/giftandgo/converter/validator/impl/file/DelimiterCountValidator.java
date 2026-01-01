package com.giftandgo.converter.validator.impl.file;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class DelimiterCountValidator implements BiPredicate<String[], Integer> {

    public static final DelimiterCountValidator INSTANCE = new DelimiterCountValidator();

    private DelimiterCountValidator() {
    }

    @Override
    public boolean test(String[] content, Integer associatedColumn) {
        return content != null && content.length == 7;
    }

}

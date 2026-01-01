package com.giftandgo.converter.validator.impl.file;

import java.util.function.BiPredicate;

public class DelimiterCount7Validator implements BiPredicate<String[], Integer> {

    public static final DelimiterCount7Validator INSTANCE = new DelimiterCount7Validator();

    private DelimiterCount7Validator() {
    }

    @Override
    public boolean test(String[] content, Integer associatedColumn) {
        return content != null && content.length == 7;
    }

}

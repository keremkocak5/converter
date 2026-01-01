package com.giftandgo.converter.validator.impl.file;

import java.util.function.BiPredicate;

public class DoubleValidator implements BiPredicate<String[], Integer> {

    public static final DoubleValidator INSTANCE = new DoubleValidator();

    private DoubleValidator() {
    }

    @Override
    public boolean test(String[] content, Integer associatedColumn) {
        try {
            Double.parseDouble(content[associatedColumn]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

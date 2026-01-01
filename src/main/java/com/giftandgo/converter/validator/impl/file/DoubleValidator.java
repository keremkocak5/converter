package com.giftandgo.converter.validator.impl.file;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

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
        }    }
}

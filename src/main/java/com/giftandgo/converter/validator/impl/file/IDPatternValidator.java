package com.giftandgo.converter.validator.impl.file;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class IDPatternValidator implements BiPredicate<String[], Integer> {

    public static final IDPatternValidator INSTANCE = new IDPatternValidator();

    private IDPatternValidator() {
    }

    private static final Pattern ID_PATTERN = Pattern.compile("^(\\d+)X\\1D\\d+$");

    @Override
    public boolean test(String[] content, Integer associatedColumn) {
        return content[associatedColumn] != null && ID_PATTERN.matcher(content[associatedColumn]).matches();
    }
}

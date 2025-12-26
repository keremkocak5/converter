package com.giftandgo.converter.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.regex.Pattern;

@UtilityClass
public class Constants {

    private static final String DELIMITER = "|";
    public static final Pattern DELIMITER_PATTERN = Pattern.compile(Pattern.quote(DELIMITER));
    public static final List<String> RESTRICTED_COUNTRIES = List.of("Canada");
    public static final List<String> RESTRICTED_ISPS = List.of("AWS");
    public static final String VALID_FILE_FORMAT = "text/plain";

}

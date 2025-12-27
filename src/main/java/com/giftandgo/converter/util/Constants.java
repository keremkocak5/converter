package com.giftandgo.converter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.regex.Pattern;

@UtilityClass
public class Constants {

    private static final String DELIMITER = "|";
    public static final Pattern DELIMITER_PATTERN = Pattern.compile(Pattern.quote(DELIMITER));
    public static final Set<String> RESTRICTED_COUNTRIES = Set.of("CN", "ES", "US");
    public static final Set<String> RESTRICTED_ISPS = Set.of("AWS", "GCP", "AZURE");
    public static final String VALID_FILE_FORMAT = "text/plain";
    public static final ObjectMapper SINGLETON_OBJECT_MAPPER = new ObjectMapper();

}

package com.giftandgo.converter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

@UtilityClass
public class Constants {

    private static final String DELIMITER = "|";
    public static final Pattern DELIMITER_PATTERN = Pattern.compile(Pattern.quote(DELIMITER));
    public static final Set<String> RESTRICTED_COUNTRIES = Set.of("CA", "ES", "US"); // kerem bunlari disari al
    public static final Set<String> RESTRICTED_ISPS = Set.of("AWS", "GCP", "AZURE");
    public static final String VALID_FILE_FORMAT = "text/plain";
    public static final ObjectMapper SINGLETON_OBJECT_MAPPER = new ObjectMapper();
    public static final Function<Long, Long> TIME_LAPSED_MILLIS = (startMoment) -> (System.nanoTime() - startMoment) / 1_000_000;

}

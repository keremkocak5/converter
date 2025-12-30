package com.giftandgo.converter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.regex.Pattern;

@UtilityClass
public class Constants {

    public static final ObjectMapper SINGLETON_OBJECT_MAPPER = new ObjectMapper();
    public static final Function<Long, Long> TIME_LAPSED_MILLIS = (startMoment) -> (System.nanoTime() - startMoment) / 1_000_000;

}

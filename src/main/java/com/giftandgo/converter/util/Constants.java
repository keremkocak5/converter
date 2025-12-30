package com.giftandgo.converter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class Constants {

    public static final ObjectMapper SINGLETON_OBJECT_MAPPER = new ObjectMapper();
    public static final Function<Long, Long> TIME_LAPSED_MILLIS = (startMoment) -> (System.nanoTime() - startMoment) / 1_000_000;
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

}

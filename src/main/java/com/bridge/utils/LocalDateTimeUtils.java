package com.bridge.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtils {

    public static String getStringOfNow() {
        return getStringOfNow("yyyy-MM-dd HH:mm:ss");
    }

    public static String getStringOfNow(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        return localDateTimeNow.format(formatter);
    }
}

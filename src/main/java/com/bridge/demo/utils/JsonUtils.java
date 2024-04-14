package com.bridge.demo.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtils {
    private static final Gson gson = new Gson();

    public static <T> T deserialize(String jsonString, Type type) {
        return gson.fromJson(jsonString, type);
    }
}

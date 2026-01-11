package com.dnikitin.util;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;


public class Json {
    private static final JsonMapper INSTANCE = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .build();


    public static JsonMapper getInstance() {
        return INSTANCE;
    }
}


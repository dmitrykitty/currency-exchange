package com.dnikitin.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class Json {
    private static final JsonMapper INSTANCE = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();


    public static JsonMapper getInstance() {
        return INSTANCE;
    }
}


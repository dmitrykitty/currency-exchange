package com.dnikitin.util;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Utility class providing a singleton instance of the Jackson JsonMapper.
 * Configured for standard application-wide JSON serialization rules.
 */
public class Json {
    private static final JsonMapper INSTANCE = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .build();


    public static JsonMapper getInstance() {
        return INSTANCE;
    }
}


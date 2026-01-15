package com.dnikitin.util;

import com.dnikitin.exceptions.InvalidCurrencyException;
import com.dnikitin.vo.CurrencyPair;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * Utility class for handling common HTTP request processing tasks.
 * Includes methods for parsing request bodies and validating path parameters.
 */
@UtilityClass
public class HttpUtil {
    /**
     * Parses the request body from x-www-form-urlencoded format into a Map.
     *
     * @param reader The BufferedReader containing the request body.
     * @return A map of decoded key-value pairs.
     */
    public Map<String, String> prepareRequestParams(BufferedReader reader) {
        String body = reader.lines().collect(joining());
        if (body.isEmpty()) {
            return Map.of();
        }
        return Arrays.stream(body.strip().split("&"))
                .map(pair -> pair.split("=", 2))
                .collect(toMap(pair -> decode(pair[0]),
                        pair -> pair.length == 1 ? "" : decode(pair[1]),
                        (existingValue, newValue) -> newValue));

    }

    public String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    /**
     * Extracts and validates a 6-character currency pair from path information.
     *
     * @param pathInfo The path string from the request.
     * @return A {@link com.dnikitin.vo.CurrencyPair} object.
     * @throws com.dnikitin.exceptions.InvalidCurrencyException if the format is invalid.
     */
    public CurrencyPair prepareCurrencyPair(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new InvalidCurrencyException("Missing currency pair");
        }

        pathInfo = pathInfo.substring(1); //remove /
        if (!pathInfo.matches("[A-Z]{6}")) {
            throw new InvalidCurrencyException("Invalid currency pair. Correct format <code1><code2>, for example USDEUR");
        }

        String baseCode = pathInfo.substring(0, 3);
        String targetCode = pathInfo.substring(3);
        return new CurrencyPair(baseCode, targetCode);
    }


}

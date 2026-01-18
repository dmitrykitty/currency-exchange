package com.dnikitin.util;

import com.dnikitin.exceptions.InvalidParamsException;
import com.dnikitin.vo.CurrencyPair;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.math.BigDecimal;
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
                        (_, newValue) -> newValue));

    }

    public String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    /**
     * Extracts and validates a 6-character currency pair from path information.
     *
     * @param pathInfo The path string from the request.
     * @return A {@link com.dnikitin.vo.CurrencyPair} object.
     * @throws InvalidParamsException if the format is invalid.
     */
    public CurrencyPair prepareCurrencyPair(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new InvalidParamsException("Missing currency pair");
        }

        pathInfo = pathInfo.substring(1); //remove /
        validateCurrencyPair(pathInfo);

        String baseCode = pathInfo.substring(0, 3);
        String targetCode = pathInfo.substring(3);

        if (baseCode.equals(targetCode)) {
            throw new InvalidParamsException("Currencies codes should be different.");
        }
        return new CurrencyPair(baseCode, targetCode);
    }

    public void validateCurrencyPair(String pair) {
        if (!pair.matches("[A-Z]{6}")) {
            throw new InvalidParamsException("Invalid currency pair. Correct format <code1><code2>, for example USDEUR");
        }
    }

    public void validateCode(String code) {
        if (!code.matches("[A-Z]{3}")) {
            throw new InvalidParamsException("Invalid currency code. Correct format USD.");
        }
    }

    public void validateName(String name) {
        if (!name.matches("[a-zA-Z -]{4,32}")) {
            throw new InvalidParamsException(
                    "Invalid currency name. Correct name is built with [a-zA-Z -] and has name from 4 to 32 symbols.");
        }
    }

    public void validateSign(String sign) {
        if (!sign.matches(".{0,4}")) {
            throw new InvalidParamsException(
                    "Invalid currency sign. Max length is 4.");
        }
    }

    public BigDecimal getBigDecimal(String bd) {
        try {
            return new BigDecimal(bd);
        } catch (NumberFormatException e) {
            throw new InvalidParamsException("Invalid rate or amount format. Should be number.");
        }
    }

    public void validateCodes(String code1, String code2) {
        validateCode(code1);
        validateCode(code2);

        if (code1.equals(code2)) {
            throw new InvalidParamsException("Currencies codes should be different.");
        }
    }


}

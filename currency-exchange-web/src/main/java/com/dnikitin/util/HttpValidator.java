package com.dnikitin.util;

import com.dnikitin.dto.CurrencyPairDto;
import com.dnikitin.exceptions.InvalidParamsException;
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
public class HttpValidator {
    /**
     * Parses the request body from x-www-form-urlencoded format into a Map.
     *
     * @param reader The BufferedReader containing the request body.
     * @return A map of decoded key-value pairs.
     */
    public Map<String, String> getRequestParams(BufferedReader reader) {
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

    /**
     * Extracts and validates a 6-character currency pair from path information.
     *
     * @param pathInfo The path string from the request.
     * @return A {@link com.dnikitin.vo.CurrencyPair} object.
     * @throws InvalidParamsException if the format is invalid.
     */
    public CurrencyPairDto getValidCurrencyPair(String pathInfo) {
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
        return new CurrencyPairDto(baseCode, targetCode);
    }

    public CurrencyPairDto getValidCurrencyPair(String code1, String code2) {
        String validCode1 = getValidCode(code1);
        String validCode2 = getValidCode(code2);

        if (validCode1.equals(validCode2)) {
            throw new InvalidParamsException("Currencies codes should be different.");
        }

        return new CurrencyPairDto(validCode1, validCode2);
    }

    public String getValidName(String name){
        name = name.trim().replaceAll(" +", " ");
        validateName(name);
        return name;
    }

    public String getValidCode(String code) {
        code = code.trim().toUpperCase();
        validateCode(code);
        return code;
    }

    public String getValidSign(String sign) {
        sign = sign.trim().replaceAll(" +", " ");
        validateSign(sign);
        return sign;
    }

    public BigDecimal getBigDecimal(String bd) {
        try {
            return new BigDecimal(bd);
        } catch (NumberFormatException e) {
            throw new InvalidParamsException("Invalid rate or amount format. Should be number.");
        }
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private void validateCurrencyPair(String pair) {
        if (!pair.matches("[A-Z]{6}")) {
            throw new InvalidParamsException(
                    "Invalid currency pair. Correct format <code1><code2>, for example USDEUR.");
        }
    }

    private void validateCode(String code) {
        if (!code.matches("[A-Z]{3}")) {
            throw new InvalidParamsException(
                    "Invalid currency code. Correct code built witch [A-Z] and has exactly 3 symbols.");
        }
    }

    private void validateName(String name) {
        if (!name.matches("[a-zA-Z ]{4,32}")) {
            throw new InvalidParamsException(
                    "Invalid currency name. Correct name is built with [a-zA-Z ] and has name from 4 to 32 symbols.");
        }
    }

    private void validateSign(String sign) {
        if (!sign.matches(".{0,4}")) {
            throw new InvalidParamsException(
                    "Invalid currency sign. Max length is 4.");
        }
    }




}

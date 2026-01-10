package com.dnikitin.exceptions;

public class InvalidCurrencyPairException extends RuntimeException {
    public InvalidCurrencyPairException(String message) {
        super(message);
    }
}

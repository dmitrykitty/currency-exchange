package com.dnikitin.exceptions;

public class DataIntegrityViolationException extends RuntimeException {
    public DataIntegrityViolationException(String message,  Throwable cause) {
        super(message, cause);
    }
}

package com.dnikitin.exceptions;

/**
 * Thrown when a requested data record is not found in the persistence layer.
 */
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}

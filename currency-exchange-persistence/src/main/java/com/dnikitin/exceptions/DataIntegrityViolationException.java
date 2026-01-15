package com.dnikitin.exceptions;

/**
 * Thrown when a database operation fails due to data integrity issues,
 * such as unique constraint violations or nullability issues.
 */
public class DataIntegrityViolationException extends RuntimeException {
    public DataIntegrityViolationException(String message,  Throwable cause) {
        super(message, cause);
    }
}

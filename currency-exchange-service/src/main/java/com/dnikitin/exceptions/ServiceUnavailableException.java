package com.dnikitin.exceptions;

/**
 * Thrown when an external resource (like the database) is unavailable.
 * Maps to HTTP 500 Internal Server Error.
 */
public class ServiceUnavailableException extends ServiceException {
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

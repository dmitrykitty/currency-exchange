package com.dnikitin.exceptions;

/**
 * Base exception class for all business logic errors in the service layer.
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message, Throwable cause) {
        super(message,  cause);
    }
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }
}

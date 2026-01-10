package com.dnikitin.exceptions;

public class ServiceUnavailableException extends ServiceException {
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

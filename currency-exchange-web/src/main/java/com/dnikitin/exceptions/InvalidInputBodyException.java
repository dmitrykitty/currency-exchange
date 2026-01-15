package com.dnikitin.exceptions;

/**
 * Thrown when required fields are missing or invalid in the request body
 * during POST or PATCH operations.
 */
public class InvalidInputBodyException extends RuntimeException{
    public InvalidInputBodyException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidInputBodyException(String message) {
        super(message);
    }
}

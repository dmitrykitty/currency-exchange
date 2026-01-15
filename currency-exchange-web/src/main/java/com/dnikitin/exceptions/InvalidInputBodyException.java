package com.dnikitin.exceptions;

public class InvalidInputBodyException extends RuntimeException{
    public InvalidInputBodyException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidInputBodyException(String message) {
        super(message);
    }
}

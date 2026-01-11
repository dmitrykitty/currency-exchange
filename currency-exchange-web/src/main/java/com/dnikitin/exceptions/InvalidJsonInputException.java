package com.dnikitin.exceptions;

public class InvalidJsonInputException extends RuntimeException{
    public InvalidJsonInputException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidJsonInputException(String message) {
        super(message);
    }
}

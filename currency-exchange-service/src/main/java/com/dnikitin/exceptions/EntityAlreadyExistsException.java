package com.dnikitin.exceptions;

public class EntityAlreadyExistsException extends ServiceException {
    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

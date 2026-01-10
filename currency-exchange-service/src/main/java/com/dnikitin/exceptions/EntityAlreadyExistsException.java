package com.dnikitin.exceptions;

public class EntityAlreadyExistsException extends ServiceException {
    public EntityAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}

package com.dnikitin.exceptions;

/**
 * Thrown when an attempt is made to create a resource that already exists.
 * Maps to HTTP 409 Conflict.
 */
public class EntityAlreadyExistsException extends ServiceException {
    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

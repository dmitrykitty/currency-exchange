package com.dnikitin.exceptions;

/**
 * Thrown when a requested resource (Currency or ExchangeRate) cannot be found.
 * Maps to HTTP 404 Not Found.
 */
public class EntityNotFoundException extends ServiceException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}

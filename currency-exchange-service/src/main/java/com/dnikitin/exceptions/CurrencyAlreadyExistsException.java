package com.dnikitin.exceptions;

public class CurrencyAlreadyExistsException extends ServiceException {
    public CurrencyAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}

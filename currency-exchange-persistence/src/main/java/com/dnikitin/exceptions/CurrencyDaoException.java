package com.dnikitin.exceptions;

public class CurrencyDaoException extends DaoException {
    public CurrencyDaoException(String message) {
        super(message);
    }
    public CurrencyDaoException(String message, Throwable cause) {
        super(message, cause);
    }
    public CurrencyDaoException(Throwable cause) {
        super(cause);
    }
}

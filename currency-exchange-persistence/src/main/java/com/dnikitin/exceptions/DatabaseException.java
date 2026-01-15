package com.dnikitin.exceptions;

/**
 * General runtime exception for errors occurring within the persistence layer
 * that cannot be recovered from (e.g., SQL syntax errors, connectivity loss).
 */
public class DatabaseException extends RuntimeException{
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

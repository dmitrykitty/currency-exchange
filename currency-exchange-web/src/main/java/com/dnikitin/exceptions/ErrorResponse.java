package com.dnikitin.exceptions;

/**
 * Data transfer object representing an error response body.
 *
 * @param message The error message to be returned to the client.
 */
public record ErrorResponse(String message) {
}

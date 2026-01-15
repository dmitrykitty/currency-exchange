package com.dnikitin.entity;

import lombok.Builder;

/**
 * Represents a currency entity within the system.
 * This record maps to the 'currencies' table in the database and holds
 * the essential metadata for any supported currency.
 *
 * @param id   The unique primary key identifier of the currency in the database.
 * @param code The unique ISO 4217 three-letter currency code (e.g., "USD", "EUR").
 * @param name The full descriptive name of the currency (e.g., "US Dollar").
 * @param sign The currency symbol or sign (e.g., "$", "€").
 */
@Builder
public record CurrencyEntity(
        Integer id,
        String code,
        String name,
        String sign) {
}


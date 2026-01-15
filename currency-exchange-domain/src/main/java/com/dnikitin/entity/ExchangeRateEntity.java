package com.dnikitin.entity;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Represents the exchange rate between a base currency and a target currency.
 * This record maps to the 'exchange_rates' table and maintains the integrity
 * between different currency entities.
 *
 * @param id             The unique primary key identifier of the exchange rate.
 * @param baseCurrency   The source currency from which the conversion originates.
 * @param targetCurrency The destination currency into which the base currency is converted.
 * @param rate           The exchange rate value, stored with a precision of 6 decimal places.
 */
@Builder
public record ExchangeRateEntity(
        Integer id,
        CurrencyEntity baseCurrency,
        CurrencyEntity targetCurrency,
        BigDecimal rate) {
}

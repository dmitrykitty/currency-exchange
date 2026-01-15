package com.dnikitin.vo;

/**
 * A Value Object representing a pair of currency codes.
 * Used as a key or identifier when searching for exchange rates or performing
 * calculations between two specific currencies.
 *
 * @param baseCurrency   The three-letter code of the base currency.
 * @param targetCurrency The three-letter code of the target currency.
 */
public record CurrencyPair(String baseCurrency, String targetCurrency) {
    @Override
    public String toString() {
        return baseCurrency + targetCurrency;
    }
}

package com.dnikitin.vo;

public record CurrencyPair(String baseCurrency, String targetCurrency) {
    @Override
    public String toString() {
        return baseCurrency + targetCurrency;
    }
}

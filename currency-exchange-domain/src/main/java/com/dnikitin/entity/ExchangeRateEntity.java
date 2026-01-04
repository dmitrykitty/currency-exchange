package com.dnikitin.entity;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExchangeRateEntity(
        Integer id,
        CurrencyEntity baseCurrency,
        CurrencyEntity targetCurrency,
        BigDecimal rate) {
}

package com.dnikitin.dto;

import com.dnikitin.entity.CurrencyEntity;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExchangeValue(
        CurrencyEntity baseCurrency,
        CurrencyEntity targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount) {
}

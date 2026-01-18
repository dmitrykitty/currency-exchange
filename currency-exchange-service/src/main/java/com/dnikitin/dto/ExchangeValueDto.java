package com.dnikitin.dto;

import com.dnikitin.entity.CurrencyEntity;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExchangeValueDto(
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount) {
}

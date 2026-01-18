package com.dnikitin.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExchangeRateDto(
        Integer id,
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        BigDecimal rate) {

}

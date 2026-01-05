package com.dnikitin.dto.response;

import java.math.BigDecimal;

public record ExchangeRateResponseDto(
        String baseCurrencyCode,
        String targetCurrencyCode,
        BigDecimal rate) {
}

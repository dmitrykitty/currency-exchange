package com.dnikitin.dto;

import lombok.Builder;

@Builder
public record CurrencyDto(
        Integer id,
        String name,
        String code,
        String sign) {
}

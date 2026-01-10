package com.dnikitin.entity;

import lombok.Builder;

@Builder
public record CurrencyEntity(
        Integer id,
        String code,
        String name,
        String sign) {
}


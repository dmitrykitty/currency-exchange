package com.dnikitin.entity;

import lombok.Builder;

@Builder
public record CurrencyEntity(
        Integer id,
        String code,
        String fullName,
        String sign) {
}

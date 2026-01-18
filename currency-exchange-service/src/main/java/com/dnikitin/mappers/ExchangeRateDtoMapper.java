package com.dnikitin.mappers;

import com.dnikitin.dto.CurrencyDto;
import com.dnikitin.dto.ExchangeRateDto;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.entity.ExchangeRateEntity;

public class ExchangeRateDtoMapper implements EntityDtoMapper<ExchangeRateEntity, ExchangeRateDto> {
    private final EntityDtoMapper<CurrencyEntity, CurrencyDto> currencyDtoMapper;

    public ExchangeRateDtoMapper(EntityDtoMapper<CurrencyEntity, CurrencyDto> currencyDtoMapper) {
        this.currencyDtoMapper = currencyDtoMapper;
    }

    @Override
    public ExchangeRateDto mapToDto(ExchangeRateEntity entity) {
        return ExchangeRateDto.builder()
                .id(entity.id())
                .baseCurrency(currencyDtoMapper.mapToDto(entity.baseCurrency()))
                .targetCurrency(currencyDtoMapper.mapToDto(entity.targetCurrency()))
                .rate(entity.rate())
                .build();
    }

    @Override
    public ExchangeRateEntity mapToEntity(ExchangeRateDto dto) {
        return ExchangeRateEntity.builder()
                .id(dto.id())
                .baseCurrency(currencyDtoMapper.mapToEntity(dto.baseCurrency()))
                .targetCurrency(currencyDtoMapper.mapToEntity(dto.targetCurrency()))
                .rate(dto.rate())
                .build();
    }
}

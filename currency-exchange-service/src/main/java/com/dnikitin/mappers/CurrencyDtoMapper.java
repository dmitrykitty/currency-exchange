package com.dnikitin.mappers;

import com.dnikitin.dto.CurrencyDto;
import com.dnikitin.entity.CurrencyEntity;

public class CurrencyDtoMapper implements EntityDtoMapper<CurrencyEntity, CurrencyDto> {

    @Override
    public CurrencyDto mapToDto(CurrencyEntity entity) {
        return CurrencyDto.builder()
                .id(entity.id())
                .name(entity.name())
                .code(entity.code())
                .sign(entity.sign())
                .build();
    }

    @Override
    public CurrencyEntity mapToEntity(CurrencyDto dto) {
        return CurrencyEntity.builder()
                .id(dto.id())
                .name(dto.name())
                .code(dto.code())
                .sign(dto.sign())
                .build();
    }
}

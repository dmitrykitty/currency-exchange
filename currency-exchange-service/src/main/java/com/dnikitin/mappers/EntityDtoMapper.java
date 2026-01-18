package com.dnikitin.mappers;

public interface EntityDtoMapper<E, V> {

    V mapToDto(E entity);
    E mapToEntity(V dto);

}

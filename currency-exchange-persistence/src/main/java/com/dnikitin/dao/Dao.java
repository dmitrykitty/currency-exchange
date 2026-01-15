package com.dnikitin.dao;

import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.vo.CurrencyPair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    List<E> findAll();

    Optional<E> findById(K id);


    E save(E entity);

}

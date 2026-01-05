package com.dnikitin.mappers;

import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.entity.ExchangeRateEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRowMapper implements RowMapper<ExchangeRateEntity> {
    @Override
    public ExchangeRateEntity mapRow(ResultSet rs) throws SQLException {
        CurrencyEntity baseCurrency = CurrencyEntity.builder()
                .id(rs.getInt("base_id"))
                .code(rs.getString("base_code"))
                .fullName(rs.getString("base_name"))
                .sign(rs.getString("base_sign"))
                .build();

        CurrencyEntity targetCurrency = CurrencyEntity.builder()
                .id(rs.getInt("target_id"))
                .code(rs.getString("target_code"))
                .fullName(rs.getString("target_name"))
                .sign(rs.getString("target_sign"))
                .build();

        return ExchangeRateEntity.builder()
                .id(rs.getInt("rate_id"))
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rs.getBigDecimal("rate_val"))
                .build();
    }
}

package com.dnikitin.mappers;

import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.entity.ExchangeRateEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRowMapper implements RowMapper<ExchangeRateEntity>{
    @Override
    public ExchangeRateEntity mapRow(ResultSet rs) throws SQLException {
        return null;
    }
}

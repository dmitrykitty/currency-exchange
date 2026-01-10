package com.dnikitin.mappers;

import com.dnikitin.entity.CurrencyEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyMapper implements RowMapper<CurrencyEntity>{

    @Override
    public CurrencyEntity mapRow(ResultSet rs) throws SQLException {
        return CurrencyEntity.builder()
                .id(rs.getInt("id"))
                .code(rs.getString("code"))
                .name(rs.getString("full_name"))
                .sign(rs.getString("sign"))
                .build();
    }
}

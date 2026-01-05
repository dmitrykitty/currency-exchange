package com.dnikitin.dao;

import com.dnikitin.config.DataSourceHikari;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.DatabaseException;
import com.dnikitin.mappers.ExchangeRowMapper;
import com.dnikitin.mappers.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements Dao<Integer, ExchangeRateEntity> {
    private static final Dao<Integer, ExchangeRateEntity> INSTANCE = new ExchangeRateDao(new ExchangeRowMapper());
    private final RowMapper<ExchangeRateEntity> rowMapper;

    private static final String FIND_BY_SQL = """
             SELECT er.id AS rate_id,
                    er.rate AS rate_val,
                    bc.id AS base_id, bc.code AS base_code, bc.full_name AS base_name, bc.sign AS base_sign,
                    tc.id AS target_id, tc.code AS target_code, tc.full_name AS target_name, tc.sign AS target_sign                                         \s
             from exchange_rates er join currencies bc on er.base_currency_id = bc.id
                                    join currencies tc on er.target_currency_id = tc.id
             where bc.code = ? and tc.code = ?
            """;


    private ExchangeRateDao(RowMapper<ExchangeRateEntity> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<ExchangeRateEntity> findAll() {
        try (Connection connection = DataSourceHikari.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement()) {
        } catch (SQLException e) {
            throw new DatabaseException("Database error during finding all exchange rates", e);
        }
    }

    @Override
    public Optional<ExchangeRateEntity> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public ExchangeRateEntity save(ExchangeRateEntity entity) {
        return null;
    }

    public static Dao<Integer, ExchangeRateEntity> getInstance() {
        return INSTANCE;
    }
}

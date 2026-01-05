package com.dnikitin.dao;

import com.dnikitin.config.DataSourceHikari;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.DataIntegrityViolationException;
import com.dnikitin.exceptions.DatabaseException;
import com.dnikitin.mappers.ExchangeRowMapper;
import com.dnikitin.mappers.RowMapper;
import com.dnikitin.vo.CurrencyPair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements Dao<CurrencyPair, ExchangeRateEntity> {
    private static final Dao<CurrencyPair, ExchangeRateEntity> INSTANCE = new ExchangeRateDao(new ExchangeRowMapper());
    private final RowMapper<ExchangeRateEntity> rowMapper;

    private static final String FIND_ALL_SQL = """
            select er.id rate_id,
                    er.rate rate_val,
                    bc.id base_id, bc.code base_code, bc.full_name base_name, bc.sign base_sign,
                    tc.id target_id, tc.code target_code, tc.full_name target_name, tc.sign target_sign
             from exchange_rates er join currencies bc on er.base_currency_id = bc.id
                                    join currencies tc on er.target_currency_id = tc.id
            """;

    private static final String FIND_BY_CURRENCIES_CODES_SQL = """
             select er.id rate_id,
                    er.rate rate_val,
                    bc.id base_id, bc.code base_code, bc.full_name base_name, bc.sign base_sign,
                    tc.id target_id, tc.code target_code, tc.full_name target_name, tc.sign target_sign
             from exchange_rates er join currencies bc on er.base_currency_id = bc.id
                                    join currencies tc on er.target_currency_id = tc.id
             where bc.code = ? and tc.code = ?
            """;

    private static final String SAVE_SQL = """
            insert into exchange_rates (base_currency_id, target_currency_id, rate)
            values (?, ?, ?)
            """;


    private ExchangeRateDao(RowMapper<ExchangeRateEntity> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<ExchangeRateEntity> findAll() {
        try (Connection connection = DataSourceHikari.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRateEntity> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(rowMapper.mapRow(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DatabaseException("Database error during finding all exchange rates", e);
        }
    }

    @Override
    public Optional<ExchangeRateEntity> findById(CurrencyPair currencies) {
        try (Connection connection = DataSourceHikari.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CURRENCIES_CODES_SQL)) {

            preparedStatement.setString(1, currencies.baseCurrency());
            preparedStatement.setString(2, currencies.targetCurrency());

            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRateEntity exchangeRateEntity = null;
            if (resultSet.next()) {
                exchangeRateEntity = rowMapper.mapRow(resultSet);
            }
            return Optional.ofNullable(exchangeRateEntity);

        } catch (SQLException e) {
            throw new DatabaseException
                    (String.format(
                            "Database error during finding exchange rate for pair (%s, %s)",
                            currencies.baseCurrency(),
                            currencies.targetCurrency()),
                            e);
        }
    }

    @Override
    public ExchangeRateEntity save(ExchangeRateEntity entity) {
        try (Connection connection = DataSourceHikari.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, entity.baseCurrency().id());
            preparedStatement.setInt(2, entity.targetCurrency().id());
            preparedStatement.setBigDecimal(3, entity.rate());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            ExchangeRateEntity newExchangeRateEntity = null;
            if (generatedKeys.next()) {
                newExchangeRateEntity = ExchangeRateEntity.builder()
                        .id(generatedKeys.getInt(1))
                        .baseCurrency(entity.baseCurrency())
                        .targetCurrency(entity.targetCurrency())
                        .rate(entity.rate())
                        .build();
            }
            return newExchangeRateEntity;

        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new DataIntegrityViolationException(
                        String.format("Exchange rate for pair (%s-%s) already exists",
                                entity.baseCurrency().code(),
                                entity.targetCurrency().code()),
                        e);
            }
            throw new DatabaseException("Database error during saving exchange rate", e);
        }
    }

    public static Dao<CurrencyPair, ExchangeRateEntity> getInstance() {
        return INSTANCE;
    }
}

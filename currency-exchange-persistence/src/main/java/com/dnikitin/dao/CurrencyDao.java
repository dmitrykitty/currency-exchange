package com.dnikitin.dao;

import com.dnikitin.config.DataSourceHikari;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.CurrencyDaoException;
import com.dnikitin.mappers.CurrencyMapper;
import com.dnikitin.mappers.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao<String, CurrencyEntity> {
    private static final CurrencyDao INSTANCE = new CurrencyDao(new CurrencyMapper());
    private final RowMapper<CurrencyEntity> rowMapper;

    private static final String FIND_ALL_SQL = """
            select id, code, full_name, sign
            from currencies
            """;

    private static final String FIND_BY_CODE_SQL = """
            select id, code, full_name, sign
            from currencies
            where code = ?
            """;

    private static final String SAVE_SQL = """
                    insert into currencies (code, full_name, sign)
                    values (?, ?, ?)
            """;


    private CurrencyDao(RowMapper<CurrencyEntity> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<CurrencyEntity> findAll() {
        try (Connection connection = DataSourceHikari.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<CurrencyEntity> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(rowMapper.mapRow(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new CurrencyDaoException(e);
        }

    }

    @Override
    public Optional<CurrencyEntity> findById(String code) {
        try (Connection connection = DataSourceHikari.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_SQL)) {

            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            CurrencyEntity currencyEntity = null;
            if (resultSet.next()) {
                currencyEntity = rowMapper.mapRow(resultSet);
            }
            return Optional.ofNullable(currencyEntity);

        } catch (SQLException e) {
            throw new CurrencyDaoException(e);
        }
    }


    @Override
    public CurrencyEntity save(CurrencyEntity entity) {
        try (Connection connection = DataSourceHikari.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.code());
            preparedStatement.setString(2, entity.fullName());
            preparedStatement.setString(3, entity.sign());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            CurrencyEntity newEntity = null;
            if (generatedKeys.next()) {
                newEntity = CurrencyEntity.builder()
                        .id(generatedKeys.getInt(1))
                        .code(entity.code())
                        .fullName(entity.fullName())
                        .sign(entity.sign())
                        .build();
            }
            return newEntity;
        } catch (SQLException e) {
            throw new CurrencyDaoException(e);
        }
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }
}

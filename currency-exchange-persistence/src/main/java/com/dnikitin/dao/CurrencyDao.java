package com.dnikitin.dao;

import com.dnikitin.config.DataSourceHikari;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.CurrencyDaoException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesDao implements Dao<Integer, CurrencyEntity> {
    private static final CurrenciesDao INSTANCE =  new CurrenciesDao();

    private static final String FIND_ALL_SQL = """
            select id, code, full_name, sign
            from currencies
            """;

    private CurrenciesDao() {}
    @Override
    public List<CurrencyEntity> findAll() {
        try (Connection connection = DataSourceHikari.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<CurrencyEntity> currencies = new ArrayList<>();

            while (resultSet.next()) {
                
            }
        } catch (SQLException e) {
            throw new CurrencyDaoException(e);
        }

    }
    @Override
    public Optional<CurrencyEntity> findById(Integer id) {
        return Optional.empty();
    }


    @Override
    public CurrencyEntity save(CurrencyEntity entity) {
        return null;
    }

    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }
}

package com.dnikitin.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@UtilityClass
public class DataSourceHikari {


    private final HikariDataSource DS;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:currency-exhange-db.sqlite");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setUsername("currency-exchange-api");
        DS = new HikariDataSource(config);
    }
    public Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    public DataSource getDataSource() {
        return DS;
    }
}

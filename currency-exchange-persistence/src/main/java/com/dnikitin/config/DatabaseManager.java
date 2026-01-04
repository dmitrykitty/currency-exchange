package com.dnikitin.config;

import lombok.experimental.UtilityClass;
import org.flywaydb.core.Flyway;

@UtilityClass
public class DatabaseManager {
    private final Flyway FLYWAY;

    static {FLYWAY = Flyway.configure()
            .dataSource(DataSourceHikari.getDataSource())
            .locations("db/migration")
            .load(); }

    public void migrate() {
        FLYWAY.migrate();
    }
}

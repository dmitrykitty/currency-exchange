package com.dnikitin.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility class for managing the HikariCP connection pool for the SQLite database.
 * Ensures efficient connection reuse and provides mandatory PRAGMA configurations for SQLite.
 */
@UtilityClass
public class DataSourceHikari {


    private final HikariDataSource DS;
    private final String DB_NAME = "currency-exhange-db.sqlite";

    static {
        try {
            Path dbPath = getValidatedPath().resolve(DB_NAME);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite:" + dbPath.toAbsolutePath());
            config.setDriverClassName("org.sqlite.JDBC");
            config.setUsername("currency-exchange-api");
            config.setConnectionInitSql("PRAGMA foreign_keys = ON;"); //turning on foreign keys recognition
            DS = new HikariDataSource(config);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ExceptionInInitializerError("Failed to initialize Hikari Connection Pool: " + e.getMessage());
        }
    }

    private static Path getValidatedPath() {
        String dbUrl = System.getenv("DB_DIR");
        if (dbUrl == null || dbUrl.isEmpty()) {
            throw new IllegalStateException("Environment variable 'DB_DIR' is missing. " +
                    "Please set it to the target directory (e.g., /var/lib/app/data).");
        }
        Path path = Paths.get(dbUrl);
        if (Files.notExists(path)) {
            throw new IllegalArgumentException(String.format("The directory specified in 'DB_DIR' does not exist: '%s'. " +
                            "Please create the directory or update the environment variable.",
                    path.toAbsolutePath()));
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path specified in 'DB_DIR' is not a directory: " + path.toAbsolutePath());
        }
        return path;
    }

    /**
     * Obtains a connection from the pool.
     *
     * @return A {@link java.sql.Connection} instance.
     * @throws java.sql.SQLException If the pool is exhausted or database is unreachable.
     */
    public Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    public DataSource getDataSource() {
        return DS;
    }

    public void closeConnectionPool() {
        if (DS != null && !DS.isClosed()) {
            DS.close();
        }
    }
}

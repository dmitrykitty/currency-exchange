package com.dnikitin.services;

import com.dnikitin.config.DatabaseManager;

/**
 * Utility service for application lifecycle management.
 * Responsible for startup tasks like database migrations and shutdown tasks like closing connection pools.
 */
public class ApplicationService {
    private static final ApplicationService INSTANCE = new ApplicationService();

    private ApplicationService() {}

    public static ApplicationService getInstance() {
        return INSTANCE;
    }

    /**
     * Initializes the application by running pending database migrations via Flyway.
     */
    public void initializeApplication() {
        DatabaseManager.migrate();
    }

    /**
     * Gracefully shuts down the application by closing the database connection pool.
     */
    public void shutdownApplication() {
        DatabaseManager.close();
    }
}

package com.dnikitin.services;

import com.dnikitin.config.DatabaseManager;

public class ApplicationService {
    private static final ApplicationService INSTANCE = new ApplicationService();

    private ApplicationService() {}

    public static ApplicationService getInstance() {
        return INSTANCE;
    }

    public void initializeApplication() {
        DatabaseManager.migrate();
    }

    public void shutdownApplication() {
        DatabaseManager.close();
    }
}

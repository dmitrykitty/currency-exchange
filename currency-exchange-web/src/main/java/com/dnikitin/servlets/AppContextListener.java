package com.dnikitin.servlets;

import com.dnikitin.config.DataSourceHikari;
import com.dnikitin.config.DatabaseManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseManager.migrate();
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseManager.close();
    }
}

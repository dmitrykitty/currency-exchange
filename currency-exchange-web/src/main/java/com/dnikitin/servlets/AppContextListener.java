package com.dnikitin.servlets;

import com.dnikitin.ApplicationService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationService.getInstance()
                .initializeApplication();
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationService.getInstance()
                .shutdownApplication();
    }
}

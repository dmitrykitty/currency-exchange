package com.dnikitin.controllers;

import com.dnikitin.services.ApplicationService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationService.getInstance()
                .initializeApplication();

        AppContext context = new AppContext();
        sce.getServletContext().setAttribute(AppContext.class.getSimpleName(), context);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationService.getInstance()
                .shutdownApplication();
    }
}

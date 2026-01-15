package com.dnikitin.controllers;

import com.dnikitin.services.ApplicationService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Servlet Context Listener responsible for managing the application lifecycle.
 * It triggers database migrations and initializes the {@link AppContext}
 * when the web application starts.
 */
@WebListener
public class AppContextListener implements ServletContextListener {
    /**
     * Called when the web application is initialized.
     * Performs database migration and sets the {@link AppContext} as a servlet context attribute.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationService.getInstance()
                .initializeApplication();

        AppContext context = new AppContext();
        sce.getServletContext().setAttribute(AppContext.class.getSimpleName(), context);
    }
    /**
     * Called when the web application is destroyed.
     * Shuts down application services and closes database connection pools.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationService.getInstance()
                .shutdownApplication();
    }
}

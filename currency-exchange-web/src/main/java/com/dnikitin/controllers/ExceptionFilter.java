package com.dnikitin.controllers;

import com.dnikitin.exceptions.*;
import com.dnikitin.util.Json;
import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Global filter for handling exceptions and setting standard response headers.
 * Maps various runtime exceptions to appropriate HTTP status codes and
 * returns error messages in JSON format.
 */
@WebFilter("/api/*")
public class ExceptionFilter implements Filter {

    private final JsonMapper jsonMapper = Json.getInstance();

    /**
     * Processes the request and captures any thrown exceptions to return
     * a structured JSON error response.
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            filterChain.doFilter(request, response);
        } catch (Throwable e) {
            Throwable cause = (e instanceof ServletException) ? e.getCause() : e;

            if (cause == null) {
                cause = e;
            }
            // Maps exceptions to HTTP error responses
            switch (cause) {
                case InvalidParamsException _ ->
                        sendError(httpResponse, HttpServletResponse.SC_BAD_REQUEST, cause.getMessage());
                case EntityNotFoundException _ ->
                        sendError(httpResponse, HttpServletResponse.SC_NOT_FOUND, cause.getMessage());
                case EntityAlreadyExistsException _ ->
                        sendError(httpResponse, HttpServletResponse.SC_CONFLICT, cause.getMessage());
                case ServiceUnavailableException _ ->
                        sendError(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, cause.getMessage());
                default ->
                        sendError(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
            }
        }
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        jsonMapper.writeValue(response.getWriter(), new ErrorResponse(message));
    }
}

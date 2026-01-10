package com.dnikitin.servlets;

import com.dnikitin.exceptions.*;
import com.dnikitin.util.Json;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionFilter implements Filter {

    private final JsonMapper jsonMapper = Json.getInstance();

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            filterChain.doFilter(request, response);
        } catch (Throwable e) {
            Throwable cause = (e instanceof ServletException) ? e.getCause() : e;

            if (cause == null) {
                cause = e;
            }
            // Maps exceptions to HTTP error responses
            switch(cause){
                case InvalidCurrencyException _ ->
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

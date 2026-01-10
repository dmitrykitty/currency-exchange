package com.dnikitin.servlets;

import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.util.Json;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final JsonMapper jsonMapper = Json.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CurrencyEntity> currencies = currencyService.getCurrencies();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        jsonMapper.writeValue(response.getWriter(), currencies);

    }
}

package com.dnikitin.servlets;

import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.services.ExchangeRateService;
import com.dnikitin.util.Json;
import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final JsonMapper jsonMapper = Json.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<ExchangeRateEntity> exchangeRates = exchangeRateService.getExchangeRates();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        jsonMapper.writeValue(resp.getWriter(),exchangeRates);
    }
}

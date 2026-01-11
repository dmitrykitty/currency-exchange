package com.dnikitin.servlets;

import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.InvalidCurrencyException;
import com.dnikitin.services.ExchangeRateService;
import com.dnikitin.util.Json;
import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final JsonMapper jsonMapper = Json.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new InvalidCurrencyException("Missing currency pair");
        }

        pathInfo = pathInfo.substring(1); //remove /
        if(!pathInfo.matches("[A-Z]{6}")){
            throw new InvalidCurrencyException("Invalid currency pair. Correct format <code1><code2>, for example USDEUR");
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String baseCode = pathInfo.substring(0, 3);
        String targetCode = pathInfo.substring(3);

        ExchangeRateEntity exchangeRateByCodes = exchangeRateService.getExchangeRateByCodes(baseCode, targetCode);
        jsonMapper.writeValue(resp.getWriter(), exchangeRateByCodes);
    }
}

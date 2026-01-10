package com.dnikitin.servlets;

import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.InvalidJsonInputException;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.util.Json;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.core.JacksonException;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final JsonMapper jsonMapper = Json.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<CurrencyEntity> currencies = currencyService.getCurrencies();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        jsonMapper.writeValue(response.getWriter(), currencies);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CurrencyEntity beforeCurrencyEntity = jsonMapper.readValue(req.getReader(), CurrencyEntity.class);
            CurrencyEntity afterCurrencyEntity = currencyService.saveCurrency(beforeCurrencyEntity);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            jsonMapper.writeValue(resp.getWriter(), afterCurrencyEntity);

        } catch (JacksonException e ) {
            throw new InvalidJsonInputException("""
                    Wrong Json format. Correct format:
                    {
                    "id" : 0,
                    "code" : "USD",
                    "name" : "United States dollar",
                    "sign" : "$"
                    };""", e);
        }
    }
}

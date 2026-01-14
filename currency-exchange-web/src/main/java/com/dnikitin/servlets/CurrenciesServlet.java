package com.dnikitin.servlets;

import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.InvalidInputException;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.util.Json;
import tools.jackson.databind.json.JsonMapper;
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
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (name == null || code == null || sign == null) {
            throw new InvalidInputException("Missing required fields: code, name, or sign");
        }

        CurrencyEntity currencyToSave = CurrencyEntity.builder()
                .id(0)
                .name(name)
                .code(code)
                .sign(sign)
                .build();

        CurrencyEntity currencyEntity = currencyService.saveCurrency(currencyToSave);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);

        jsonMapper.writeValue(resp.getWriter(), currencyEntity);

    }
}

package com.dnikitin.controllers.servlets;

import com.dnikitin.controllers.AppContext;
import com.dnikitin.dto.CurrencyDto;
import com.dnikitin.exceptions.InvalidParamsException;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.util.HttpValidator;

import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * REST endpoint for managing the list of currencies.
 * Supports GET for retrieval and POST for adding new currencies.
 */
@WebServlet("/api/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyService currencyService;
    private JsonMapper jsonMapper;

    @Override
    public void init(){
        AppContext context = (AppContext) getServletContext().getAttribute(AppContext.class.getSimpleName());

        currencyService = context.getCurrencyService();
        jsonMapper = context.getJsonMapper();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<CurrencyDto> currencies = currencyService.getCurrencies();

        jsonMapper.writeValue(response.getWriter(), currencies);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (name == null || code == null || sign == null) {
            throw new InvalidParamsException("Missing required fields: code, name, or sign");
        }

        String validName = HttpValidator.getValidName(name);
        String validCode = HttpValidator.getValidCode(code);
        String validSign = HttpValidator.getValidSign(sign);

        CurrencyDto currencyToSave = CurrencyDto.builder()
                .id(0)
                .name(validName)
                .code(validCode)
                .sign(validSign)
                .build();

        CurrencyDto savedCurrency = currencyService.saveCurrency(currencyToSave);
        resp.setStatus(HttpServletResponse.SC_CREATED);

        jsonMapper.writeValue(resp.getWriter(), savedCurrency);

    }
}

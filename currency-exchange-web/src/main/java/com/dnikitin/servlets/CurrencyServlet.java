package com.dnikitin.servlets;

import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.InvalidCurrencyException;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.util.Json;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final JsonMapper jsonMapper = Json.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new InvalidCurrencyException("Missing currency code");
        }

        pathInfo = pathInfo.substring(1); //remove /
        if(!pathInfo.matches("[A-Z]{3}")){
            throw new InvalidCurrencyException("Invalid currency code. Correct format <code1>, for example USD");
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        CurrencyEntity currencyByCode = currencyService.getCurrencyByCode(pathInfo);
        jsonMapper.writeValue(resp.getWriter(), currencyByCode);
    }
}

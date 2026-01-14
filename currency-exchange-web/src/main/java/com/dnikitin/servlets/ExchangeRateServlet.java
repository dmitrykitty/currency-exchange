package com.dnikitin.servlets;

import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.InvalidCurrencyException;
import com.dnikitin.services.ExchangeRateService;
import com.dnikitin.util.Json;
import com.dnikitin.vo.CurrencyPair;
import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final JsonMapper jsonMapper = Json.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getMethod().equals("PATCH")){
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        CurrencyPair currencyPair = prepareCurrencyPair(pathInfo);


        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ExchangeRateEntity exchangeRateByCodes = exchangeRateService.getExchangeRateByCodes(
                currencyPair.baseCurrency(), currencyPair.targetCurrency());

        jsonMapper.writeValue(resp.getWriter(), exchangeRateByCodes);
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) {
        String rate = req.getParameter("rate");
        if(rate == null){
            throw new InvalidCurrencyException("Missing required fields: rate");
        }

        String pathInfo = req.getPathInfo();
        CurrencyPair currencyPair = prepareCurrencyPair(pathInfo);
        BigDecimal rateDecimal = BigDecimal.valueOf(Double.parseDouble(rate));


    }

    private CurrencyPair prepareCurrencyPair(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new InvalidCurrencyException("Missing currency pair");
        }

        pathInfo = pathInfo.substring(1); //remove /
        if(!pathInfo.matches("[A-Z]{6}")){
            throw new InvalidCurrencyException("Invalid currency pair. Correct format <code1><code2>, for example USDEUR");
        }

        String baseCode = pathInfo.substring(0, 3);
        String targetCode = pathInfo.substring(3);
        return new CurrencyPair(baseCode, targetCode);
    }

}

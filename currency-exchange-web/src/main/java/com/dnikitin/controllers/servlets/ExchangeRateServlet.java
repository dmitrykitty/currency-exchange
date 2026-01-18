package com.dnikitin.controllers.servlets;

import com.dnikitin.controllers.AppContext;
import com.dnikitin.dto.ExchangeRateDto;
import com.dnikitin.exceptions.InvalidParamsException;
import com.dnikitin.services.ExchangeRateService;
import com.dnikitin.util.HttpUtil;
import com.dnikitin.vo.CurrencyPair;

import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * REST endpoint for a specific currency pair's rate.
 * Supports GET for retrieval and PATCH for updating the rate value.
 */
@WebServlet("/api/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private JsonMapper jsonMapper;

    @Override
    public void init() {
        AppContext context = (AppContext) getServletContext().getAttribute(AppContext.class.getSimpleName());

        exchangeRateService = context.getExchangeRateService();
        jsonMapper = context.getJsonMapper();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        CurrencyPair currencyPair = HttpUtil.prepareCurrencyPair(pathInfo);

        ExchangeRateDto exchangeRateByCodes = exchangeRateService.getExchangeRateByCodes(currencyPair);

        jsonMapper.writeValue(resp.getWriter(), exchangeRateByCodes);
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> params = HttpUtil.prepareRequestParams(req.getReader());
        String rate = params.get("rate");
        if (rate == null) {
            throw new InvalidParamsException("Missing required fields: rate");
        }

        String pathInfo = req.getPathInfo();

        CurrencyPair currencyPair = HttpUtil.prepareCurrencyPair(pathInfo);
        BigDecimal rateDecimal = HttpUtil.getBigDecimal(rate);

        ExchangeRateDto exchangeRate = exchangeRateService.updateExchangeRate(currencyPair, rateDecimal);

        jsonMapper.writeValue(resp.getWriter(), exchangeRate);
    }

}

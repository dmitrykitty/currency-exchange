package com.dnikitin.controllers.servlets;

import com.dnikitin.controllers.AppContext;
import com.dnikitin.dto.CurrencyDto;
import com.dnikitin.dto.ExchangeRateDto;
import com.dnikitin.exceptions.InvalidParamsException;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.services.ExchangeRateService;

import com.dnikitin.util.HttpUtil;
import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * REST endpoint for managing the list of exchange rates.
 * Supports GET for retrieval and POST for adding new rate records.
 */
@WebServlet("/api/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private CurrencyService currencyService;
    private JsonMapper jsonMapper;

    @Override
    public void init(){
        AppContext context = (AppContext) getServletContext().getAttribute(AppContext.class.getSimpleName());

        exchangeRateService = context.getExchangeRateService();
        currencyService = context.getCurrencyService();
        jsonMapper = context.getJsonMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {

        List<ExchangeRateDto> exchangeRates = exchangeRateService.getExchangeRates();

        jsonMapper.writeValue(resp.getWriter(),exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if(baseCurrencyCode == null || targetCurrencyCode == null || rate == null){
            throw new InvalidParamsException("Missing required fields: baseCurrencyCode, targetCurrencyCode, rate");
        }

        HttpUtil.validateCodes(baseCurrencyCode, targetCurrencyCode);
        BigDecimal rateDecimal = HttpUtil.getBigDecimal(rate);

        CurrencyDto baseCurrencyByCode = currencyService.getCurrencyByCode(baseCurrencyCode);
        CurrencyDto targetCurrencyByCode = currencyService.getCurrencyByCode(targetCurrencyCode);

        ExchangeRateDto exchangeRateToSave = ExchangeRateDto.builder()
                .id(0)
                .targetCurrency(targetCurrencyByCode)
                .baseCurrency(baseCurrencyByCode)
                .rate(rateDecimal)
                .build();

        ExchangeRateDto savedExchangeRate = exchangeRateService.saveExchangeRate(exchangeRateToSave);

        resp.setStatus(HttpServletResponse.SC_CREATED);

        jsonMapper.writeValue(resp.getWriter(),savedExchangeRate);
    }
}

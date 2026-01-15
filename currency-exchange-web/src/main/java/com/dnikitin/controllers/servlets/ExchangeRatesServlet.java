package com.dnikitin.controllers.servlets;

import com.dnikitin.controllers.AppContext;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.InvalidInputBodyException;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.services.ExchangeRateService;
import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

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

        List<ExchangeRateEntity> exchangeRates = exchangeRateService.getExchangeRates();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        jsonMapper.writeValue(resp.getWriter(),exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if(baseCurrencyCode == null || targetCurrencyCode == null || rate == null){
            throw new InvalidInputBodyException("Missing required fields: baseCurrencyCode, targetCurrencyCode, rate");
        }

        CurrencyEntity baseCurrencyByCode = currencyService.getCurrencyByCode(baseCurrencyCode);
        CurrencyEntity targetCurrencyByCode = currencyService.getCurrencyByCode(targetCurrencyCode);
        BigDecimal rateDecimal = new BigDecimal(rate);

        ExchangeRateEntity exchangeRateToSave = ExchangeRateEntity.builder()
                .id(0)
                .targetCurrency(targetCurrencyByCode)
                .baseCurrency(baseCurrencyByCode)
                .rate(rateDecimal)
                .build();

        ExchangeRateEntity exchangeRateEntity = exchangeRateService.saveExchangeRate(exchangeRateToSave);

        resp.setStatus(HttpServletResponse.SC_CREATED);

        jsonMapper.writeValue(resp.getWriter(),exchangeRateEntity);
    }
}

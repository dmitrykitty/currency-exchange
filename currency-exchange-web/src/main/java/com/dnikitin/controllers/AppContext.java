package com.dnikitin.controllers;

import com.dnikitin.dao.CurrencyDao;
import com.dnikitin.dao.ExchangeRateDao;
import com.dnikitin.mappers.CurrencyMapper;
import com.dnikitin.mappers.ExchangeRowMapper;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.services.ExchangeRateService;
import com.dnikitin.util.Json;
import lombok.Getter;
import tools.jackson.databind.json.JsonMapper;

@Getter
public class AppContext {
    private final CurrencyDao currencyDao;
    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final JsonMapper jsonMapper;

    public AppContext() {
        CurrencyMapper currencyMapper = new CurrencyMapper();
        ExchangeRowMapper exchangeRowMapper = new ExchangeRowMapper();

        currencyDao = new CurrencyDao(currencyMapper);
        exchangeRateDao = new ExchangeRateDao(exchangeRowMapper);

        currencyService = new CurrencyService(currencyDao);
        exchangeRateService = new ExchangeRateService(exchangeRateDao);

        jsonMapper = Json.getInstance();
    }
}

package com.dnikitin.controllers;

import com.dnikitin.dao.CurrencyDao;
import com.dnikitin.dao.ExchangeRateDao;
import com.dnikitin.mappers.CurrencyMapper;
import com.dnikitin.mappers.ExchangeRowMapper;
import com.dnikitin.services.CurrencyService;
import com.dnikitin.services.ExchangeRateService;
import com.dnikitin.services.ExchangeService;
import com.dnikitin.util.Json;
import lombok.Getter;
import tools.jackson.databind.json.JsonMapper;

/**
 * Manual Dependency Injection container for the application.
 * This class is responsible for instantiating and wiring all DAOs, Services,
 * and Mappers in the correct order of dependency.
 */
@Getter
public class AppContext {
    /** The Data Access Object for currency persistence. */
    private final CurrencyDao currencyDao;
    /** The Data Access Object for exchange rate persistence. */
    private final ExchangeRateDao exchangeRateDao;
    /** Service for currency-related business logic. */
    private final CurrencyService currencyService;
    /** Service for managing stored exchange rates. */
    private final ExchangeRateService exchangeRateService;
    /** Service for performing currency conversion calculations. */
    private final ExchangeService exchangeService;
    /** Shared JSON mapper instance. */
    private final JsonMapper jsonMapper;

    /**
     * Initializes the application context by creating instances of all components.
     * Dependencies are injected via constructors.
     */
    public AppContext() {
        CurrencyMapper currencyMapper = new CurrencyMapper();
        ExchangeRowMapper exchangeRowMapper = new ExchangeRowMapper();

        currencyDao = new CurrencyDao(currencyMapper);
        exchangeRateDao = new ExchangeRateDao(exchangeRowMapper);

        currencyService = new CurrencyService(currencyDao);
        exchangeRateService = new ExchangeRateService(exchangeRateDao);
        exchangeService = new ExchangeService(exchangeRateDao, currencyService);

        jsonMapper = Json.getInstance();
    }
}

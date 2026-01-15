package com.dnikitin.services;

import com.dnikitin.dao.Dao;
import com.dnikitin.dao.ExchangeRateDao;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.*;
import com.dnikitin.vo.CurrencyPair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public class ExchangeRateService {
    private final Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao;

    public ExchangeRateService(Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    public List<ExchangeRateEntity> getExchangeRates() {
        try {

            return exchangeRateDao.findAll();
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    public ExchangeRateEntity getExchangeRateByCodes(CurrencyPair currencyPair) {
        try {
            Optional<ExchangeRateEntity> maybeExchangeRate = exchangeRateDao.findById(currencyPair);
            return maybeExchangeRate.orElseThrow(() ->
                    new EntityNotFoundException("No exchange rate for currency pair " + currencyPair + " found"));
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    public ExchangeRateEntity saveExchangeRate(ExchangeRateEntity exchangeRate) {
        try {
            return exchangeRateDao.save(exchangeRate);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(e.getMessage(), e);
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    public ExchangeRateEntity updateExchangeRate(CurrencyPair currencyPair, BigDecimal rate) {
        try {
            ExchangeRateDao exchangeRateDaoNew = (ExchangeRateDao) exchangeRateDao;
            return exchangeRateDaoNew.update(currencyPair, rate);
        } catch (DataNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }
}

package com.dnikitin.services;

import com.dnikitin.dao.Dao;
import com.dnikitin.dao.ExchangeRateDao;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.*;
import com.dnikitin.vo.CurrencyPair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing exchange rates.
 * Provides functionality to retrieve, save, and update direct exchange rates between currency pairs.
 */
public class ExchangeRateService {
    private final Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao;

    public ExchangeRateService(Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    /**
     * Retrieves all exchange rates currently stored in the database.
     *
     * @return A list of {@link ExchangeRateEntity} objects.
     */
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

    /**
     * Updates the rate value for an existing currency pair.
     *
     * @param currencyPair The pair identifying the rate to be updated.
     * @param rate         The new rate value.
     * @return The updated {@link ExchangeRateEntity}.
     * @throws EntityNotFoundException If the currency pair does not exist.
     */
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

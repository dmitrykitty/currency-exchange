package com.dnikitin.services;

import com.dnikitin.dao.CurrencyDao;
import com.dnikitin.dao.Dao;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.*;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing currency-related operations.
 * It acts as an intermediary between the DAO layer and the controllers,
 * handling business logic and exception mapping for currencies.
 */
public class CurrencyService {
    private final Dao<String, CurrencyEntity> currencyDao;

    public CurrencyService(Dao<String, CurrencyEntity> currencyDao) {
        this.currencyDao = currencyDao;
    }

    /**
     * Retrieves a list of all supported currencies from the database.
     *
     * @return A list of {@link CurrencyEntity} objects.
     * @throws ServiceUnavailableException If the database is unreachable.
     */
    public List<CurrencyEntity> getCurrencies() {
        try {
            return currencyDao.findAll();
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    /**
     * Finds a currency by its unique ISO 4217 code.
     *
     * @param code The three-letter currency code (e.g., "USD").
     * @return The found {@link CurrencyEntity}.
     * @throws EntityNotFoundException If no currency with the given code exists.
     * @throws ServiceUnavailableException If a database error occurs.
     */
    public CurrencyEntity getCurrencyByCode(String code) {
        try {
            Optional<CurrencyEntity> maybeCurrency = currencyDao.findById(code);
            return maybeCurrency.orElseThrow(() ->
                    new EntityNotFoundException("No currency with code " + code + " found"));
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    /**
     * Validates and persists a new currency in the system.
     *
     * @param currency The currency entity to save.
     * @return The persisted {@link CurrencyEntity} with its generated ID.
     * @throws EntityAlreadyExistsException If the currency code is already taken.
     * @throws ServiceUnavailableException If a database error occurs.
     */
    public CurrencyEntity saveCurrency(CurrencyEntity currency) {
        try {
            return currencyDao.save(currency);
        }catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(e.getMessage(), e);
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }
}

package com.dnikitin.services;

import com.dnikitin.dao.Dao;
import com.dnikitin.dto.CurrencyDto;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.*;
import com.dnikitin.mappers.EntityDtoMapper;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing currency-related operations.
 * It acts as an intermediary between the DAO layer and the controllers,
 * handling business logic and exception mapping for currencies.
 */
public class CurrencyService {
    private final Dao<String, CurrencyEntity> currencyDao;
    private final EntityDtoMapper<CurrencyEntity, CurrencyDto> currencyToDtoMapper;

    public CurrencyService(Dao<String, CurrencyEntity> currencyDao,
                           EntityDtoMapper<CurrencyEntity, CurrencyDto> currencyToDtoMapper) {
        this.currencyDao = currencyDao;
        this.currencyToDtoMapper = currencyToDtoMapper;
    }

    /**
     * Retrieves a list of all supported currencies from the database.
     *
     * @return A list of {@link CurrencyEntity} objects.
     * @throws ServiceUnavailableException If the database is unreachable.
     */
    public List<CurrencyDto> getCurrencies() {
        try {
            return currencyDao.findAll().stream()
                    .map(currencyToDtoMapper::mapToDto)
                    .toList();
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
    public CurrencyDto getCurrencyByCode(String code) {
        try {
            Optional<CurrencyEntity> maybeCurrency = currencyDao.findById(code);
            CurrencyEntity currencyEntity = maybeCurrency.orElseThrow(() ->
                    new EntityNotFoundException("No currency with code " + code + " found"));
            return currencyToDtoMapper.mapToDto(currencyEntity);
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    /**
     * Validates and persists a new currency in the system.
     *
     * @param currencyDto The currencyDto to convert to currencyEntity and save.
     * @return The persisted {@link CurrencyEntity} with its generated ID.
     * @throws EntityAlreadyExistsException If the currency code is already taken.
     * @throws ServiceUnavailableException If a database error occurs.
     */
    public CurrencyDto saveCurrency(CurrencyDto currencyDto) {
        try {
            CurrencyEntity currency = currencyToDtoMapper.mapToEntity(currencyDto);
            CurrencyEntity savedCurrency = currencyDao.save(currency);
            return currencyToDtoMapper.mapToDto(savedCurrency);
        }catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(e.getMessage(), e);
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }
}

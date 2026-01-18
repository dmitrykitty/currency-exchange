package com.dnikitin.services;

import com.dnikitin.dao.Dao;
import com.dnikitin.dao.ExchangeRateDao;
import com.dnikitin.dto.CurrencyPairDto;
import com.dnikitin.dto.ExchangeRateDto;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.*;
import com.dnikitin.mappers.EntityDtoMapper;
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
    private final EntityDtoMapper<ExchangeRateEntity, ExchangeRateDto> exchangeRateDtoMapper;

    public ExchangeRateService(Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao,
                               EntityDtoMapper<ExchangeRateEntity, ExchangeRateDto> exchangeRateDtoMapper) {
        this.exchangeRateDao = exchangeRateDao;
        this.exchangeRateDtoMapper = exchangeRateDtoMapper;
    }

    /**
     * Retrieves all exchange rates currently stored in the database.
     *
     * @return A list of {@link ExchangeRateEntity} objects.
     */
    public List<ExchangeRateDto> getExchangeRates() {
        try {
            return exchangeRateDao.findAll().stream()
                    .map(exchangeRateDtoMapper::mapToDto)
                    .toList();
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    public ExchangeRateDto getExchangeRateByCodes(CurrencyPairDto currencyPairDto) {
        try {
            CurrencyPair currencyPair = new CurrencyPair(currencyPairDto.baseCurrency(), currencyPairDto.targetCurrency());
            Optional<ExchangeRateEntity> maybeExchangeRate = exchangeRateDao.findById(currencyPair);
            ExchangeRateEntity exchangeRateEntity = maybeExchangeRate.orElseThrow(() ->
                    new EntityNotFoundException("No exchange rate for currency pair " + currencyPair + " found"));
            return exchangeRateDtoMapper.mapToDto(exchangeRateEntity);
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    public ExchangeRateDto saveExchangeRate(ExchangeRateDto exchangeRate) {
        try {
            ExchangeRateEntity exchangeRateEntity = exchangeRateDtoMapper.mapToEntity(exchangeRate);
            ExchangeRateEntity savedExchangeRate = exchangeRateDao.save(exchangeRateEntity);
            return exchangeRateDtoMapper.mapToDto(savedExchangeRate);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(e.getMessage(), e);
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    /**
     * Updates the rate value for an existing currency pair.
     *
     * @param currencyPairDto The pair identifying the rate to be updated.
     * @param rate         The new rate value.
     * @return The updated {@link ExchangeRateEntity}.
     * @throws EntityNotFoundException If the currency pair does not exist.
     */
    public ExchangeRateDto updateExchangeRate(CurrencyPairDto currencyPairDto, BigDecimal rate) {
        try {
            CurrencyPair currencyPair = new CurrencyPair(currencyPairDto.baseCurrency(), currencyPairDto.targetCurrency());

            ExchangeRateDao exchangeRateDaoNew = (ExchangeRateDao) exchangeRateDao;
            ExchangeRateEntity updatedEntity = exchangeRateDaoNew.update(currencyPair, rate);
            return exchangeRateDtoMapper.mapToDto(updatedEntity);
        } catch (DataNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }
}

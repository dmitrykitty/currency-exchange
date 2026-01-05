package com.dnikitin.services;

import com.dnikitin.dao.Dao;
import com.dnikitin.dao.ExchangeRateDao;
import com.dnikitin.dto.response.ExchangeRateResponseDto;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.*;
import com.dnikitin.vo.CurrencyPair;

import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private final Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao = ExchangeRateDao.getInstance();
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    private ExchangeRateService() {
    }

    public List<ExchangeRateResponseDto> getExchangeRates() {
        try {
            List<ExchangeRateEntity> exchangeRates = exchangeRateDao.findAll();
            if (exchangeRates.isEmpty()) {
                throw new EntityNotFoundException("No exchange rates found");

            }
            return exchangeRates.stream()
                    .map(exchangeRate -> new ExchangeRateResponseDto(
                            exchangeRate.baseCurrency().code(),
                            exchangeRate.targetCurrency().code(),
                            exchangeRate.rate()
                    )).toList();
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e);
        }
    }

    public ExchangeRateResponseDto getExchangeRateByCodes(String baseCurrency, String targetCurrency) {
        try {
            Optional<ExchangeRateEntity> maybeExchangeRate = exchangeRateDao.findById(new CurrencyPair(baseCurrency, targetCurrency));
            ExchangeRateEntity exchangeRateEntity = maybeExchangeRate.orElseThrow(
                    () -> new EntityNotFoundException("No exchange rate found"));
            return new ExchangeRateResponseDto(
                    exchangeRateEntity.baseCurrency().code(),
                    exchangeRateEntity.targetCurrency().code(),
                    exchangeRateEntity.rate());
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e);
        }
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}

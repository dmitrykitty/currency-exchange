package com.dnikitin.services;

import com.dnikitin.dao.Dao;
import com.dnikitin.dto.CurrencyDto;
import com.dnikitin.dto.CurrencyPairDto;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.EntityNotFoundException;
import com.dnikitin.vo.CurrencyPair;
import com.dnikitin.dto.ExchangeValueDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * High-level service for currency conversion calculations.
 * This service implements the logic for finding exchange rates through different paths:
 * <ul>
 * <li>Direct rate (A -> B)</li>
 * <li>Reverse rate (B -> A)</li>
 * <li>Cross rate via USD (A -> USD -> B)</li>
 * </ul>
 */
public class ExchangeService {
    private final static String USD = "USD";
    private final static int SCALE = 6;

    private final Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao;
    private final CurrencyService currencyService;

    public ExchangeService(Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao, CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyService = currencyService;
    }

    /**
     * Performs a currency exchange calculation for a specific amount.
     *
     * @param currencyPairDto The pair defining the base and target currencies.
     * @param amount          The amount of base currency to be converted.
     * @return An {@link ExchangeValueDto} containing the conversion details.
     * @throws com.dnikitin.exceptions.EntityNotFoundException If no exchange path (direct, reverse, or cross) is found.
     */
    public ExchangeValueDto exchange(CurrencyPairDto currencyPairDto, BigDecimal amount) {
        CurrencyPair currencyPair = new CurrencyPair(currencyPairDto.baseCurrency(), currencyPairDto.targetCurrency());
        BigDecimal exchangeRate = findExchangeRate(currencyPair);

        CurrencyDto baseCurrencyDto = currencyService.getCurrencyByCode(currencyPair.baseCurrency());
        CurrencyDto targetCurrencyDto = currencyService.getCurrencyByCode(currencyPair.targetCurrency());

        BigDecimal convertedAmount = getConvertedAmount(exchangeRate, amount);

        return ExchangeValueDto.builder()
                .baseCurrency(baseCurrencyDto)
                .targetCurrency(targetCurrencyDto)
                .rate(exchangeRate)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }

    /**
     * Internal logic to determine the exchange rate.
     * Uses a scale of 6 for rate calculations and {@link java.math.RoundingMode#HALF_UP}.
     */
    private BigDecimal findExchangeRate(CurrencyPair currencyPair) {

        Optional<ExchangeRateEntity> maybeRate = exchangeRateDao.findById(currencyPair);
        if (maybeRate.isPresent()) {
            return maybeRate.get().rate();
        }

        CurrencyPair reversedPair = new CurrencyPair(currencyPair.targetCurrency(), currencyPair.baseCurrency());
        maybeRate = exchangeRateDao.findById(reversedPair);
        if (maybeRate.isPresent()) {
            if (maybeRate.get().rate().compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Divide by zero: exchange rate is 0.0");
            }
            return BigDecimal.ONE.divide(maybeRate.get().rate(), SCALE, RoundingMode.HALF_UP);
        }

        CurrencyPair crossBaseUsd = new CurrencyPair(currencyPair.baseCurrency(), USD);
        CurrencyPair crossUSDTarget = new CurrencyPair(USD, currencyPair.targetCurrency());

        Optional<ExchangeRateEntity> maybeBaseUSDRate = exchangeRateDao.findById(crossBaseUsd);
        Optional<ExchangeRateEntity> maybeUSDTargetRate = exchangeRateDao.findById(crossUSDTarget);

        if (maybeBaseUSDRate.isPresent() && maybeUSDTargetRate.isPresent()) {
            return maybeBaseUSDRate.get().rate().multiply(maybeUSDTargetRate.get().rate())
                    .setScale(SCALE, RoundingMode.HALF_UP);
        }

        throw new EntityNotFoundException("No exchange path for " + currencyPair);
    }

    /**
     * Calculates the final converted amount.
     * Rounds the result to 2 decimal places using {@link java.math.RoundingMode#HALF_UP}.
     */
    private BigDecimal getConvertedAmount(BigDecimal rate, BigDecimal amount) {
        return rate.multiply(amount).setScale(2, RoundingMode.HALF_UP);
    }
}
 
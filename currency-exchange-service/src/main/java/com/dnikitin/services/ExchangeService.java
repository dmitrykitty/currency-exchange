package com.dnikitin.services;

import com.dnikitin.dao.Dao;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.entity.ExchangeRateEntity;
import com.dnikitin.exceptions.EntityNotFoundException;
import com.dnikitin.vo.CurrencyPair;
import com.dnikitin.dto.ExchangeValue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
    private final static String USD = "USD";
    private final static int SCALE = 6;

    private final Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao;
    private final CurrencyService currencyService;

    public ExchangeService(Dao<CurrencyPair, ExchangeRateEntity> exchangeRateDao,CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyService = currencyService;
    }
    
    public ExchangeValue exchange(CurrencyPair currencyPair, BigDecimal amount){
        BigDecimal exchangeRate = findExchangeRate(currencyPair);

        CurrencyEntity baseCurrency = currencyService.getCurrencyByCode(currencyPair.baseCurrency());
        CurrencyEntity targetCurrency = currencyService.getCurrencyByCode(currencyPair.targetCurrency());

        BigDecimal convertedAmount = getConvertedAmount(exchangeRate,amount);

        return ExchangeValue.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(exchangeRate)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }
    
    private BigDecimal findExchangeRate(CurrencyPair currencyPair){
        
        Optional<ExchangeRateEntity> maybeRate = exchangeRateDao.findById(currencyPair);
        if(maybeRate.isPresent()){
            return maybeRate.get().rate();
        }
        
        CurrencyPair reversedPair = new  CurrencyPair(currencyPair.targetCurrency(), currencyPair.baseCurrency());
        maybeRate = exchangeRateDao.findById(reversedPair);
        if(maybeRate.isPresent()){
            if (maybeRate.get().rate().compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Divide by zero: exchange rate is 0.0");
            }
            return BigDecimal.ONE.divide(maybeRate.get().rate(), SCALE, RoundingMode.HALF_UP);
        }
        
        CurrencyPair crossBaseUsd =  new CurrencyPair(currencyPair.baseCurrency(), USD);
        CurrencyPair crossUSDTarget =  new CurrencyPair(USD, currencyPair.targetCurrency());

        Optional<ExchangeRateEntity> maybeBaseUSDRate = exchangeRateDao.findById(crossBaseUsd);
        Optional<ExchangeRateEntity> maybeUSDTargetRate = exchangeRateDao.findById(crossUSDTarget);

        if(maybeBaseUSDRate.isPresent() && maybeUSDTargetRate.isPresent()){
            return maybeBaseUSDRate.get().rate().multiply(maybeUSDTargetRate.get().rate())
                    .setScale(SCALE, RoundingMode.HALF_UP);
        }

        throw new EntityNotFoundException("No exchange path for " + currencyPair);
    }

    private BigDecimal getConvertedAmount(BigDecimal rate, BigDecimal amount){
        return rate.multiply(amount).setScale(2, RoundingMode.HALF_UP);
    }
}
 
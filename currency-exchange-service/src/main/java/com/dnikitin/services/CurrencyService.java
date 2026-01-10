package com.dnikitin.services;

import com.dnikitin.dao.CurrencyDao;
import com.dnikitin.dao.Dao;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.DatabaseException;
import com.dnikitin.exceptions.EntityNotFoundException;
import com.dnikitin.exceptions.ServiceUnavailableException;

import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final Dao<String, CurrencyEntity> currencyDao = CurrencyDao.getInstance();
    private final static CurrencyService INSTANCE = new CurrencyService();

    private CurrencyService() {
    }

    public List<CurrencyEntity> getCurrencies() {
        try {
            return currencyDao.findAll();
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e);
        }
    }

    public CurrencyEntity getCurrencyByCode(String code) {
        try {
            Optional<CurrencyEntity> maybeCurrency = currencyDao.findById(code);
            return maybeCurrency.orElseThrow(() ->
                    new EntityNotFoundException("No currency with code " + code + " found"));
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e);
        }
    }


    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}

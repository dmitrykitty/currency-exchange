package com.dnikitin.services;

import com.dnikitin.dao.CurrencyDao;
import com.dnikitin.dao.Dao;
import com.dnikitin.entity.CurrencyEntity;
import com.dnikitin.exceptions.*;

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
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    public CurrencyEntity getCurrencyByCode(String code) {
        try {
            Optional<CurrencyEntity> maybeCurrency = currencyDao.findById(code);
            return maybeCurrency.orElseThrow(() ->
                    new EntityNotFoundException("No currency with code " + code + " found"));
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }

    public CurrencyEntity saveCurrency(CurrencyEntity currency) {
        try {
            return currencyDao.save(currency);
        }catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(e.getMessage(), e);
        } catch (DatabaseException e) {
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
    }


    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}

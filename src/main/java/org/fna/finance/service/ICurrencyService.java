package org.fna.finance.service;

import org.fna.finance.exception.CurrencyNotFoundException;
import org.fna.finance.model.Currency;

import java.util.List;

public interface ICurrencyService {
    List<Currency> getAllCurrencies();

    Currency getCurrency(String id) throws CurrencyNotFoundException;
}

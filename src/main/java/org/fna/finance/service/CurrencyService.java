package org.fna.finance.service;

import org.fna.finance.exception.CurrencyNotFoundException;
import org.fna.finance.model.Currency;
import org.fna.finance.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository categoryRepository) {
        this.currencyRepository = categoryRepository;
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Currency getCurrency(String id) throws CurrencyNotFoundException {
        Optional<Currency> currency = currencyRepository.findById(id);
        if (currency.isPresent()) {
            return currency.get();
        } else {
            throw new CurrencyNotFoundException(id);
        }
    }

}

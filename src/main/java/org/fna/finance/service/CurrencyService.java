package org.fna.finance.service;

import org.fna.finance.exception.CurrencyNotFoundException;
import org.fna.finance.model.Currency;
import org.fna.finance.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService implements ICurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository categoryRepository) {
        this.currencyRepository = categoryRepository;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public Currency getCurrency(String id) throws CurrencyNotFoundException {
        Optional<Currency> currency = currencyRepository.findByName(id);
        if (currency.isPresent()) {
            return currency.get();
        } else {
            throw new CurrencyNotFoundException(id);
        }
    }

}

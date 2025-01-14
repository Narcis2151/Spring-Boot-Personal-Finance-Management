package org.fna.finance.service;

import org.fna.finance.exception.CurrencyNotFoundException;
import org.fna.finance.model.Currency;
import org.fna.finance.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCurrencies_Success() {
        List<Currency> currencies = Arrays.asList(
                new Currency("USD", 4.11),
                new Currency("EUR", 4.92)
        );

        when(currencyRepository.findAll()).thenReturn(currencies);

        List<Currency> result = currencyService.getAllCurrencies();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(currencyRepository).findAll();
    }

    @Test
    void getCurrency_Success() throws CurrencyNotFoundException {

        Currency currency = new Currency("USD", 4.11);

        when(currencyRepository.findByName("USD")).thenReturn(Optional.of(currency));

        Currency result = currencyService.getCurrency("USD");

        assertNotNull(result);
        assertEquals("USD", result.getName());
        verify(currencyRepository).findByName("USD");
    }

    @Test
    void getCurrency_CurrencyNotFoundException() {

        when(currencyRepository.findByName("USD")).thenReturn(Optional.empty());

        assertThrows(CurrencyNotFoundException.class, () -> currencyService.getCurrency("USD"));

        verify(currencyRepository).findByName("USD");
    }
}
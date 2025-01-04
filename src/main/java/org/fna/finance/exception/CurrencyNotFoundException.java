package org.fna.finance.exception;

public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException(String id) {
        super("Currency with id " + id + " doesn't exist ");
    }
}

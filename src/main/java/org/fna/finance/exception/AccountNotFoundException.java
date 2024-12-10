package org.fna.finance.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Account with id " + id + " doesn't exist ");
    }
}

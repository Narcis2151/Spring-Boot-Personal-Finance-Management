package org.fna.finance.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(Long id) {
        super("Transaction with id " + id + " doesn't exist ");
    }
}

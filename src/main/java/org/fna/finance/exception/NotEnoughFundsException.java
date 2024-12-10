package org.fna.finance.exception;

public class NotEnoughFundsException extends RuntimeException {

    public NotEnoughFundsException(Double balance, Double amount) {
        super("Not enough funds in the account. Current balance: " + balance + ", requested amount: " + amount);
    }
}

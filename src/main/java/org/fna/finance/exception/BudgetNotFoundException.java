package org.fna.finance.exception;

public class BudgetNotFoundException extends RuntimeException {

    public BudgetNotFoundException(Long id) {
        super("Budget with id " + id + " doesn't exist ");
    }
}

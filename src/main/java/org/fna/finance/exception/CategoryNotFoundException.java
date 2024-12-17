package org.fna.finance.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Category with id " + id + " doesn't exist ");
    }
}

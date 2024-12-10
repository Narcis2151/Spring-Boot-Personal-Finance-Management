package org.fna.finance.dto;

import jakarta.validation.constraints.*;
import org.fna.finance.model.Currency;

public class CreateAccountRequest {
    @NotBlank
    private String name;

    @NotNull
    private Currency currency;

    @Positive
    private Double balance;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(String name, Currency currency, Double balance) {
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

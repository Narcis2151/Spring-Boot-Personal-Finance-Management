package org.fna.finance.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateAccountBalanceRequest {
    @NotNull
    private Double balance;

    public UpdateAccountBalanceRequest() {
    }

    public UpdateAccountBalanceRequest(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

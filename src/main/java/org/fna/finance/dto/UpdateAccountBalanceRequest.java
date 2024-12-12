package org.fna.finance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAccountBalanceRequest {
    @NotNull
    private Double balance;

    public UpdateAccountBalanceRequest() {
    }

    public UpdateAccountBalanceRequest(Double balance) {
        this.balance = balance;
    }

}

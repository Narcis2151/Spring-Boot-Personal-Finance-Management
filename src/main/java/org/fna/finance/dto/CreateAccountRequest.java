package org.fna.finance.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.fna.finance.model.Currency;

@Setter
@Getter
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

}

package org.fna.finance.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountResponse {
    private Long id;
    private String name;
    private String currency;
    private Double balance;

    public AccountResponse() {
    }

    public AccountResponse(Long id, String name, String currency, Double balance) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

}

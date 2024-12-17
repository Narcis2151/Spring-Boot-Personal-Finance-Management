package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Account Response Details")
public class AccountResponse {
    @Schema(description = "Account ID", example = "1")
    private Long id;

    @Schema(description = "Account Name", example = "Savings")
    private String name;

    @Schema(description = "Account Currency", example = "RON")
    private String currency;

    @Schema(description = "Account Balance", example = "1000.0")
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

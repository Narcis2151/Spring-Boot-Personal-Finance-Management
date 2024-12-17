package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.fna.finance.model.Currency;

@Setter
@Getter
@Schema(description = "Create Account Details")
public class CreateAccountRequest {
    @NotBlank
    @Schema(description = "Account Name", example = "Test Account")
    private String name;

    @NotNull
    @Schema(description = "Account Currency", example = "RON")
    private Currency currency;

    @Positive
    @Schema(description = "Account Balance", example = "1000")
    private Double balance;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(String name, Currency currency, Double balance) {
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

}

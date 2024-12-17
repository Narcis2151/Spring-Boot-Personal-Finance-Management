package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Update Account Balance Details")
public class UpdateAccountBalanceRequest {
    @NotNull
    @Schema(description = "Balance Of The Account", example = "1000")
    private Double balance;

    public UpdateAccountBalanceRequest() {
    }

    public UpdateAccountBalanceRequest(Double balance) {
        this.balance = balance;
    }

}

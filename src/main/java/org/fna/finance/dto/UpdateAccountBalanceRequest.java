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
    @Schema(description = "Amount To Add Or Subtract", example = "1000")
    private Double amount;

    public UpdateAccountBalanceRequest() {
    }

    public UpdateAccountBalanceRequest(Double amount) {
        this.amount = amount;
    }

}

package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Create Budget Details")
public class UpdateBudgetRequest {
    @Positive
    @NotNull
    @Schema(description = "Amount Available in RON", example = "1000")
    private Double amountAvailable;


    public UpdateBudgetRequest() {
    }

    public UpdateBudgetRequest(Double amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

}

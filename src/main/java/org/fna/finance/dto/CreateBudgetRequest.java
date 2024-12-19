package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Create Budget Details")
public class CreateBudgetRequest {
    @Positive
    @Schema(description = "Amount Available in RON", example = "1000")
    private Double amountAvailable;

    @Positive
    @Schema(description = "Category Id", example = "1")
    private Long categoryId;


    public CreateBudgetRequest() {
    }

    public CreateBudgetRequest(Double amountAvailable, Long categoryId) {
        this.amountAvailable = amountAvailable;
        this.categoryId = categoryId;
    }

}

package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Update Transaction Category Request Details")
public class UpdateTransactionCategoryRequest {
    @Positive
    @Schema(description = "Category Id", example = "1")
    private Long categoryId;

    public UpdateTransactionCategoryRequest() {
    }

    public UpdateTransactionCategoryRequest(Long categoryId) {
        this.categoryId = categoryId;
    }


}

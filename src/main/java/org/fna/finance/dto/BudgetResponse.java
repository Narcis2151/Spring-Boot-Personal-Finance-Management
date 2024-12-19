package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Schema(description = "Budget Response Details")
public class BudgetResponse {
    @Schema(description = "Budget ID", example = "1")
    private Long id;

    @Schema(description = "Amount Available", example = "1000.0")
    private Double amountAvailable;

    @Schema(description = "Amount Spent", example = "450.00")
    private Double amountSpent;

    @Schema(description = "Start Date", example = "2024-12-01")
    private Date startDate;

    @Schema(description = "End Date", example = "2025-01-01")
    private Date endDate;

    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    public BudgetResponse() {
    }

    public BudgetResponse(Long id, Double amountAvailable, Double amountSpent, Date startDate, Date endDate, Long categoryId) {
        this.id = id;
        this.amountAvailable = amountAvailable;
        this.amountSpent = amountSpent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = categoryId;
    }

    public BudgetResponse(Long id, Double amountAvailable, Date startDate, Date endDate, Long categoryId) {
        this.id = id;
        this.amountAvailable = amountAvailable;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = categoryId;
    }

    public BudgetResponse(Double amountAvailable, Double amountSpent, Date startDate, Date endDate) {
        this.amountAvailable = amountAvailable;
        this.amountSpent = amountSpent;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public BudgetResponse(Double amountAvailable, Double amountSpent) {
        this.amountAvailable = amountAvailable;
        this.amountSpent = amountSpent;
    }


}

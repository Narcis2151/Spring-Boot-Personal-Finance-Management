package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.fna.finance.model.DebitCredit;

import java.util.Date;

@Setter
@Getter
@Schema(description = "Create Transaction Request Details")
public class CreateTransactionRequest {
    @NotNull
    @Schema(description = "Debit Or Credit", example = "DEBIT")
    private DebitCredit debitCredit;

    @Positive
    @Schema(description = "Amount Of The Transaction", example = "100.0")
    private Double amount;

    @NotBlank
    @Schema(description = "Party Involved In The Transaction", example = "Amazon")
    private String party;

    @NotNull
    @Schema(description = "Date Of The Transaction", example = "2025-01-10T09:46:21.488Z")
    private Date datePosted;

    @NotNull
    @Schema(description = "Account Id", example = "1")
    private Long accountId;

    @NotNull
    @Schema(description = "Category Id", example = "1")
    private Long categoryId;

    public CreateTransactionRequest() {
    }

    public CreateTransactionRequest(DebitCredit debitCredit, Double amount, String party, Date datePosted, Long accountId, Long categoryId) {
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.party = party;
        this.datePosted = datePosted;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

    public CreateTransactionRequest(DebitCredit debitCredit, Double amount, String party, Long accountId, Long categoryId) {
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.party = party;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }


}

package org.fna.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.fna.finance.model.DebitCredit;

import java.util.Date;

@Getter
@Setter
@Schema(description = "Update Transaction Request Details")
public class UpdateTransactionRequest {
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
    @Schema(description = "Date Of The Transaction", example = "2021-12-12")
    private Date datePosted;

    public UpdateTransactionRequest() {
    }

    public UpdateTransactionRequest(DebitCredit debitCredit, String party, Double balance, Date datePosted) {
        this.debitCredit = debitCredit;
        this.party = party;
        this.amount = balance;
        this.datePosted = datePosted;
    }

    public UpdateTransactionRequest(DebitCredit debitCredit, String party, Double balance) {
        this.debitCredit = debitCredit;
        this.party = party;
        this.amount = balance;
    }


}

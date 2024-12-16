package org.fna.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.fna.finance.model.DebitCredit;

import java.util.Date;

@Setter
@Getter
public class CreateTransactionRequest {
    @NotNull
    private DebitCredit debitCredit;

    @Positive
    private Double amount;

    @NotBlank
    private String party;

    @NotNull
    private Date datePosted = new Date();

    @NotNull
    private Long accountId;

    public CreateTransactionRequest() {
    }

    public CreateTransactionRequest(DebitCredit debitCredit, Double amount, String party, Date datePosted, Long accountId) {
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.party = party;
        this.datePosted = datePosted;
        this.accountId = accountId;
    }

    public CreateTransactionRequest(DebitCredit debitCredit, Double amount, String party, Long accountId) {
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.party = party;
        this.accountId = accountId;
    }


}

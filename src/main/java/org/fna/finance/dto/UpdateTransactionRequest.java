package org.fna.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.fna.finance.model.DebitCredit;

import java.util.Date;

@Getter
@Setter
public class UpdateTransactionRequest {
    @NotNull
    private DebitCredit debitCredit;

    @Positive
    private Double amount;

    @NotBlank
    private String party;

    @Positive
    private Double balance;

    @NotNull
    private Date datePosted = new Date();

    public UpdateTransactionRequest() {
    }

    public UpdateTransactionRequest(DebitCredit debitCredit, Double amount, String party, Double balance, Date datePosted) {
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.party = party;
        this.balance = balance;
        this.datePosted = datePosted;
    }

    public UpdateTransactionRequest(DebitCredit debitCredit, Double amount, String party, Double balance) {
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.party = party;
        this.balance = balance;
    }


}

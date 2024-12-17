package org.fna.finance.dto;

import lombok.Getter;
import lombok.Setter;
import org.fna.finance.model.DebitCredit;

import java.util.Date;

@Getter
@Setter
public class TransactionResponse {
    private Long id;
    private DebitCredit debitCredit;
    private Double amount;
    private String party;
    private Date datePosted;
    private Long accountId;
    private Long categoryId;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, DebitCredit debitCredit, Double amount, String party, Date datePosted, Long accountId, Long categoryId) {
        this.id = id;
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.party = party;
        this.datePosted = datePosted;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

}

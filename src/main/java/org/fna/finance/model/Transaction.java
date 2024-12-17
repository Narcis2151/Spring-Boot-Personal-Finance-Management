package org.fna.finance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "debit_credit")
    private DebitCredit debitCredit;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String party;

    @Column(nullable = false, name = "date_posted")
    private Date datePosted;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Transaction() {
    }

    public Transaction(Long id, DebitCredit debitCredit, Double amount, String party, Date datePosted, User user, Account account, Category category) {
        this.id = id;
        this.debitCredit = debitCredit;
        this.amount = amount;
        this.party = party;
        this.datePosted = datePosted;
        this.user = user;
        this.account = account;
        this.category = category;
    }

    public Transaction(double amount, DebitCredit debitCredit, String party, Date datePosted, User user, Account account, Category category) {
        this.amount = amount;
        this.debitCredit = debitCredit;
        this.party = party;
        this.datePosted = datePosted;
        this.user = user;
        this.account = account;
        this.category = category;
    }

    public Transaction(double amount, DebitCredit debitCredit, String party, Date datePosted) {
        this.amount = amount;
        this.debitCredit = debitCredit;
        this.party = party;
        this.datePosted = datePosted;
    }

    @PrePersist
    protected void onCreate() {
        datePosted = new Date();
    }

}

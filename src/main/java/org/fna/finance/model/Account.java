package org.fna.finance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;

    @Column(nullable = false)
    private double balance;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Account() {
    }

    public Account(Long id, String name, Currency currency, double balance, User user, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
        this.user = user;
        this.transactions = transactions;
    }

    public Account(Long id, String name, Currency currency, double balance, User user) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
        this.user = user;
    }

    public Account(String name, Currency currency, double balance, User user) {
        this.name = name;
        this.currency = currency;
        this.balance = balance;
        this.user = user;
    }

    public Account(String name, Currency currency, double balance) {
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
}

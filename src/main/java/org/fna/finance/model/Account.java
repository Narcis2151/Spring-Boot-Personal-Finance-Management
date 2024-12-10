package org.fna.finance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Account() {
    }

    public Account(Long id, String name, Currency currency, double balance, User user) {
        this.id = id;
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

    public Account(String name, Currency currency, double balance, User user) {
        this.name = name;
        this.currency = currency;
        this.balance = balance;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

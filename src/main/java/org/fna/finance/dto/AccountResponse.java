package org.fna.finance.dto;

public class AccountResponse {
    private Long id;
    private String name;
    private String currency;
    private Double balance;

    public AccountResponse() {
    }

    public AccountResponse(Long id, String name, String currency, Double balance) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

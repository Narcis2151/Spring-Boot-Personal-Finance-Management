package org.fna.finance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "currency")
public class Currency {
    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount_in_ron", nullable = false)
    private Double amountInRon;

    @OneToMany(mappedBy = "currency")
    private List<Account> accounts;

    public Currency() {
    }

    public Currency(String name, Double amountInRon) {
        this.name = name;
        this.amountInRon = amountInRon;
    }

}

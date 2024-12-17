package org.fna.finance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Category() {
    }

    public Category(Long id, String name, User user, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.transactions = transactions;
    }

    public Category(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Category(String name) {
        this.name = name;
    }
}

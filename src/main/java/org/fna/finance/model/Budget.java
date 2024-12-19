package org.fna.finance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, name = "amount_available")
    private Double amountAvailable;

    @Column(nullable = false, name = "start_date")
    private Date startDate;

    @Column(nullable = false, name = "end_date")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Budget() {
    }

    public Budget(Double amountAvailable, Category category, User user) {
        this.amountAvailable = amountAvailable;
        this.category = category;
        this.user = user;
    }

    public Budget(Double amountAvailable) {
        this.amountAvailable = amountAvailable;
    }


    @PrePersist
    protected void onCreate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        startDate = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        endDate = cal.getTime();
    }
}

package org.fna.finance.repository;

import org.fna.finance.model.Transaction;
import org.fna.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);

    Optional<Transaction> findByUserAndId(User user, Long id);

    @Query("SELECT COALESCE(SUM(t.amount * t.account.currency.amountInRon), 0) " +
            "FROM Transaction t " +
            "WHERE t.debitCredit = 'DEBIT' " +
            "AND t.user = ?1 AND t.category.id = ?2 " +
            "AND t.datePosted >= ?3 " +
            "AND t.datePosted <= ?4")
    Double getSpentAmountWithinPeriodByCategory(User user, Long categoryId, Date startDate, Date endDate);
}

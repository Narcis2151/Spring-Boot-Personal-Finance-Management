package org.fna.finance.repository;

import org.fna.finance.model.Account;
import org.fna.finance.model.Transaction;
import org.fna.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);

    Optional<Transaction> findByUserAndId(User user, Long id);
}

package org.fna.finance.repository;

import org.fna.finance.model.Budget;
import org.fna.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    public List<Budget> findAllByUser(User user);

    public Optional<Budget> findByUserAndId(User user, Long id);
}

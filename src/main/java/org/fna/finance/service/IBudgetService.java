package org.fna.finance.service;

import org.fna.finance.exception.BudgetNotFoundException;
import org.fna.finance.model.Budget;
import org.fna.finance.model.User;

import java.util.List;

public interface IBudgetService {
    List<Budget> getAllBudgets(User user);

    Budget createBudget(Budget budget);

    Budget getBudget(User user, Long id) throws BudgetNotFoundException;

    Budget updateBudgetAmountAvailable(User user, Long id, Double amountAvailable) throws BudgetNotFoundException;

    void deleteBudget(User user, Long id) throws BudgetNotFoundException;
}

package org.fna.finance.service;

import org.fna.finance.exception.BudgetNotFoundException;
import org.fna.finance.model.Budget;
import org.fna.finance.model.User;
import org.fna.finance.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService implements IBudgetService {
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public List<Budget> getAllBudgets(User user) {
        return budgetRepository.findAllByUser(user);
    }

    @Override
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public Budget getBudget(User user, Long id) throws BudgetNotFoundException {
        return budgetRepository
                .findByUserAndId(user, id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
    }

    @Override
    public Budget updateBudgetAmountAvailable(User user, Long id, Double amountAvailable) throws BudgetNotFoundException {
        Budget budget = budgetRepository.findByUserAndId(user, id).orElse(null);
        if (budget != null) {
            budget.setAmountAvailable(amountAvailable);
            return budgetRepository.save(budget);
        } else {
            throw new BudgetNotFoundException(id);
        }
    }

    @Override
    public void deleteBudget(User user, Long id) throws BudgetNotFoundException {
        Budget budget = budgetRepository.findByUserAndId(user, id).orElse(null);
        if (budget != null) {
            budgetRepository.delete(budget);
        } else {
            throw new BudgetNotFoundException(id);
        }
    }

}

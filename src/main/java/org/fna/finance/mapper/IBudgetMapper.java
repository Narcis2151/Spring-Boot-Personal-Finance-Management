package org.fna.finance.mapper;

import org.fna.finance.dto.BudgetResponse;
import org.fna.finance.dto.CreateBudgetRequest;
import org.fna.finance.model.Budget;

import java.util.List;

public interface IBudgetMapper {
    Budget createBudgetRequestToBudget(CreateBudgetRequest createBudgetRequest);

    BudgetResponse budgetToBudgetResponse(Budget budget);

    List<BudgetResponse> budgetListToBudgetResponseList(List<Budget> budgetList);
}

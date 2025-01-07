package org.fna.finance.mapper;

import org.fna.finance.dto.*;
import org.fna.finance.model.Budget;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BudgetMapper {
    public Budget createBudgetRequestToBudget(CreateBudgetRequest createBudgetRequest) {
        return new Budget(createBudgetRequest.getAmountAvailable());
    }

    public BudgetResponse budgetToBudgetResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getAmountAvailable(),
                budget.getStartDate().toString(),
                budget.getEndDate().toString(),
                budget.getCategory().getId()
        );
    }

    public List<BudgetResponse> budgetListToBudgetResponseList(List<Budget> budgetList) {
        return budgetList.stream().map(this::budgetToBudgetResponse).collect(Collectors.toList());
    }


}

package org.fna.finance.mapper;

import org.fna.finance.dto.*;
import org.fna.finance.model.Budget;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BudgetMapper implements IBudgetMapper {
    @Override
    public Budget createBudgetRequestToBudget(CreateBudgetRequest createBudgetRequest) {
        return new Budget(createBudgetRequest.getAmountAvailable());
    }

    @Override
    public BudgetResponse budgetToBudgetResponse(Budget budget) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return new BudgetResponse(
                budget.getId(),
                budget.getAmountAvailable(),
                simpleDateFormat.format(budget.getStartDate()),
                simpleDateFormat.format(budget.getEndDate()),
                budget.getCategory().getId()
        );
    }

    @Override
    public List<BudgetResponse> budgetListToBudgetResponseList(List<Budget> budgetList) {
        return budgetList.stream().map(this::budgetToBudgetResponse).collect(Collectors.toList());
    }


}

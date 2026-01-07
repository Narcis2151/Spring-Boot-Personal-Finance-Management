package org.fna.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.fna.finance.dto.BudgetResponse;
import org.fna.finance.dto.CreateBudgetRequest;
import org.fna.finance.dto.UpdateBudgetRequest;
import org.fna.finance.mapper.BudgetMapper;
import org.fna.finance.model.Budget;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.fna.finance.service.BudgetService;
import org.fna.finance.service.CategoryService;
import org.fna.finance.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/budget")
@RestController
@Tag(name = "Budgets", description = "Endpoints for budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final TransactionService transactionService;
    private final BudgetMapper budgetMapper;
    private final CategoryService categoryService;

    public BudgetController(BudgetService budgetService, TransactionService transactionService, BudgetMapper budgetMapper, CategoryService categoryService) {
        this.budgetService = budgetService;
        this.transactionService = transactionService;
        this.budgetMapper = budgetMapper;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all budgets", description = "Get all budgets for the authenticated user")
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(@AuthenticationPrincipal User user) {
        List<Budget> budgets = budgetService.getAllBudgets(user);

        List<Double> amountsSpent = new ArrayList<>();
        budgets.forEach(budget -> {
            double amountSpent = transactionService.getSpentAmountWithinPeriodByCategory(
                    user, budget.getCategory().getId(), budget.getStartDate(), budget.getEndDate()
            );
            amountsSpent.add(amountSpent);
        });

        List<BudgetResponse> budgetResponses = budgetMapper.budgetListToBudgetResponseList(budgetService.getAllBudgets(user));
        budgetResponses.forEach(budgetResponse -> budgetResponse.setAmountSpent(amountsSpent.get(budgetResponses.indexOf(budgetResponse))));
        return ResponseEntity.ok(budgetResponses);
    }

    @PostMapping
    @Operation(summary = "Create budget", description = "Create a new budget for the authenticated user")
    public ResponseEntity<BudgetResponse> createBudget(@Valid @RequestBody CreateBudgetRequest createBudgetRequest,
                                       @AuthenticationPrincipal User user) {
        Budget budget = budgetMapper.createBudgetRequestToBudget(createBudgetRequest);
        budget.setUser(user);

        Category category = categoryService.getCategory(user, createBudgetRequest.getCategoryId());
        budget.setCategory(category);

        Budget createdBudget = budgetService.createBudget(budget);
        double amountSpent = transactionService.getSpentAmountWithinPeriodByCategory(
                user, createdBudget.getCategory().getId(), createdBudget.getStartDate(), createdBudget.getEndDate()
        );
        BudgetResponse budgetResponse = budgetMapper.budgetToBudgetResponse(createdBudget);
        budgetResponse.setAmountSpent(amountSpent);

        return ResponseEntity.ok(budgetResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get budget", description = "Get a budget by id for the authenticated user")
    public ResponseEntity<BudgetResponse> getBudget(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Budget budget = budgetService.getBudget(user, id);
        double amountSpent = transactionService.getSpentAmountWithinPeriodByCategory(
                user, budget.getCategory().getId(), budget.getStartDate(), budget.getEndDate()
        );
        BudgetResponse budgetResponse = budgetMapper.budgetToBudgetResponse(budget);
        budgetResponse.setAmountSpent(amountSpent);
        return ResponseEntity.ok(budgetResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update budget", description = "Update a budget by id for the authenticated user")
    public ResponseEntity<BudgetResponse> updateBudget(@PathVariable Long id,
                                       @Valid @RequestBody UpdateBudgetRequest updateBudgetRequest,
                                       @AuthenticationPrincipal User user) {
        Budget updatedBudget = budgetService.updateBudgetAmountAvailable(
                user, id, updateBudgetRequest.getAmountAvailable()
        );
        double amountSpent = transactionService.getSpentAmountWithinPeriodByCategory(
                user, updatedBudget.getCategory().getId(), updatedBudget.getStartDate(), updatedBudget.getEndDate()
        );
        BudgetResponse budgetResponse = budgetMapper.budgetToBudgetResponse(updatedBudget);
        budgetResponse.setAmountSpent(amountSpent);
        return ResponseEntity.ok(budgetResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete budget", description = "Delete a budget by id for the authenticated user")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id, @AuthenticationPrincipal User user) {
        budgetService.deleteBudget(user, id);
        return ResponseEntity.noContent().build();
    }

}

package org.fna.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.fna.finance.dto.BudgetResponse;
import org.fna.finance.dto.CreateBudgetRequest;
import org.fna.finance.dto.UpdateBudgetRequest;
import org.fna.finance.mapper.BudgetMapper;
import org.fna.finance.model.Budget;
import org.fna.finance.model.User;
import org.fna.finance.service.BudgetService;
import org.fna.finance.service.TransactionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/budget")
@RestController
@Tag(name = "Budgets", description = "Endpoints for budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final TransactionService transactionService;
    private final BudgetMapper budgetMapper;

    public BudgetController(BudgetService budgetService, TransactionService transactionService, BudgetMapper budgetMapper) {
        this.budgetService = budgetService;
        this.transactionService = transactionService;
        this.budgetMapper = budgetMapper;
    }

    @GetMapping
    @Operation(summary = "Get all budgets", description = "Get all budgets for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Budgets retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
    })
    public List<BudgetResponse> getAllBudgets(@AuthenticationPrincipal User user) {
        List<BudgetResponse> budgetResponses = budgetMapper.budgetListToBudgetResponseList(budgetService.getAllBudgets(user));
        budgetResponses.forEach(budgetResponse -> {
            budgetResponse.setAmountSpent(transactionService.getSpentAmountWithinPeriodByCategory(user, budgetResponse.getCategoryId(), budgetResponse.getStartDate(), budgetResponse.getEndDate()));
        });
        return budgetResponses;
    }

    @PostMapping
    @Operation(summary = "Create budget", description = "Create a new budget for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Budget created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
    })
    public BudgetResponse createBudget(@RequestBody CreateBudgetRequest createBudgetRequest, @AuthenticationPrincipal User user) {
        Budget budget = budgetMapper.createBudgetRequestToBudget(createBudgetRequest);
        Budget createdBudget = budgetService.createBudget(budget);
        BudgetResponse budgetResponse = budgetMapper.budgetToBudgetResponse(createdBudget);
        budgetResponse.setAmountSpent(
                transactionService.getSpentAmountWithinPeriodByCategory(
                        user,
                        budgetResponse.getCategoryId(),
                        budgetResponse.getStartDate(),
                        budgetResponse.getEndDate()
                )
        );
        return budgetResponse;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get budget", description = "Get a budget by id for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Budget retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Budget not found", useReturnTypeSchema = false),
    })
    public BudgetResponse getBudget(@PathVariable Long id, @AuthenticationPrincipal User user) {
        BudgetResponse budgetResponse = budgetMapper.budgetToBudgetResponse(budgetService.getBudget(user, id));
        budgetResponse.setAmountSpent(
                transactionService.getSpentAmountWithinPeriodByCategory(
                        user,
                        budgetResponse.getCategoryId(),
                        budgetResponse.getStartDate(),
                        budgetResponse.getEndDate()
                )
        );
        return budgetResponse;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update budget", description = "Update a budget by id for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Budget updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Budget not found", useReturnTypeSchema = false),
    })
    public BudgetResponse updateBudget(@PathVariable Long id, @RequestBody UpdateBudgetRequest updateBudgetRequest, @AuthenticationPrincipal User user) {
        Budget updatedBudget = budgetService.updateBudgetAmountAvailable(user, id, updateBudgetRequest.getAmountAvailable());
        BudgetResponse budgetResponse = budgetMapper.budgetToBudgetResponse(updatedBudget);
        budgetResponse.setAmountSpent(
                transactionService.getSpentAmountWithinPeriodByCategory(
                        user,
                        budgetResponse.getCategoryId(),
                        budgetResponse.getStartDate(),
                        budgetResponse.getEndDate()
                )
        );
        return budgetResponse;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete budget", description = "Delete a budget by id for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Budget deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Budget not found", useReturnTypeSchema = false),
    })
    public void deleteBudget(@PathVariable Long id, @AuthenticationPrincipal User user) {
        budgetService.deleteBudget(user, id);
    }

}

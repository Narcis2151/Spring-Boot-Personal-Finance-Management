package org.fna.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.fna.finance.dto.*;
import org.fna.finance.mapper.TransactionMapper;
import org.fna.finance.model.DebitCredit;
import org.fna.finance.model.Transaction;
import org.fna.finance.model.User;
import org.fna.finance.service.AccountService;
import org.fna.finance.service.CategoryService;
import org.fna.finance.service.TransactionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transaction")
@RestController
@Tag(name = "Transactions", description = "Endpoints for managing transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, AccountService accountService, CategoryService categoryService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Get all transactions for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
    })
    public List<TransactionResponse> getAllTransactions(@AuthenticationPrincipal User user) {
        return transactionMapper.transactionsToTransactionResponse(
                transactionService.getAllTransactions(user)
        );
    }

    @PostMapping
    @Operation(summary = "Create transaction", description = "Create a new transaction", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
    })
    public TransactionResponse createTransaction(@Valid @RequestBody CreateTransactionRequest createTransactionRequest,
                                                 @AuthenticationPrincipal User user) {
        Transaction transaction = transactionMapper.createTransactionRequestToTransaction(createTransactionRequest);
        transaction.setAccount(
                accountService.getAccount(user, createTransactionRequest.getAccountId())
        );
        transaction.setCategory(
                categoryService.getCategory(user, createTransactionRequest.getCategoryId())
        );
        transaction.setUser(user);
        accountService.updateAccountBalance(
                user,
                createTransactionRequest.getAccountId(),
                createTransactionRequest.getDebitCredit().equals(DebitCredit.DEBIT) ? -createTransactionRequest.getAmount() : createTransactionRequest.getAmount()
        );
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return transactionMapper.transactionToTransactionResponse(createdTransaction);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction", description = "Get a transaction by id", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found", useReturnTypeSchema = false),
    })
    public TransactionResponse getTransaction(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return transactionMapper.transactionToTransactionResponse(
                transactionService.getTransaction(user, id)
        );
    }

    @PutMapping("/{id}/category")
    @Operation(summary = "Update transaction category", description = "Update the category of a transaction", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction category updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found", useReturnTypeSchema = false),
    })
    public TransactionResponse updateTransactionCategory(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateTransactionCategoryRequest updateTransactionCategoryRequest,
                                                         @AuthenticationPrincipal User user) {
        Transaction transaction = transactionService.getTransaction(user, id);
        transaction.setCategory(
                categoryService.getCategory(user, updateTransactionCategoryRequest.getCategoryId())
        );
        return transactionMapper.transactionToTransactionResponse(
                transactionService.updateTransaction(
                        user,
                        id,
                        transaction
                )
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction", description = "Update a transaction", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found", useReturnTypeSchema = false),
    })
    public TransactionResponse updateTransaction(@PathVariable Long id,
                                                 @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest,
                                                 @AuthenticationPrincipal User user) {
        Transaction initialTransaction = transactionService.getTransaction(user, id);
        Transaction updatedTransaction = transactionMapper.updateTransactionRequestToTransaction(updateTransactionRequest);
        updatedTransaction.setUser(user);
        updatedTransaction.setAccount(initialTransaction.getAccount());
        Double transactionDifference = transactionService.getTransactionDifference(initialTransaction, updatedTransaction);
        accountService.updateAccountBalance(user, initialTransaction.getAccount().getId(), transactionDifference);

        return transactionMapper.transactionToTransactionResponse(
                transactionService.updateTransaction(
                        user,
                        id,
                        updatedTransaction
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Delete a transaction by id", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", useReturnTypeSchema = false),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found", useReturnTypeSchema = false),
    })
    public void deleteTransaction(@PathVariable Long id, @AuthenticationPrincipal User user) {
        transactionService.deleteTransaction(user, id);
    }

}

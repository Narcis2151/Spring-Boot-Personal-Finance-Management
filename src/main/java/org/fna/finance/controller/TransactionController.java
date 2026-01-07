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
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Get all transactions", description = "Get all transactions for the authenticated user")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionMapper.transactionsToTransactionResponse(
                transactionService.getAllTransactions(user))
        );
    }

    @PostMapping
    @Operation(summary = "Create transaction", description = "Create a new transaction")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest createTransactionRequest,
                                                 @AuthenticationPrincipal User user) {
        Transaction transaction = transactionMapper.createTransactionRequestToTransaction(createTransactionRequest);
        transaction.setUser(user);

        transaction.setCategory(
                categoryService.getCategory(user, createTransactionRequest.getCategoryId())
        );

        transaction.setAccount(
                accountService.getAccount(user, createTransactionRequest.getAccountId())
        );

        accountService.updateAccountBalance(
                user,
                createTransactionRequest.getAccountId(),
                createTransactionRequest.getDebitCredit().equals(DebitCredit.DEBIT) ? -createTransactionRequest.getAmount() : createTransactionRequest.getAmount()
        );

        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(transactionMapper.transactionToTransactionResponse(createdTransaction));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction", description = "Get a transaction by id")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionMapper.transactionToTransactionResponse(
                transactionService.getTransaction(user, id))
        );
    }

    @PutMapping("/{id}/category")
    @Operation(summary = "Update transaction category", description = "Update the category of a transaction")
    public ResponseEntity<TransactionResponse> updateTransactionCategory(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateTransactionCategoryRequest updateTransactionCategoryRequest,
                                                         @AuthenticationPrincipal User user) {
        Transaction transaction = transactionService.getTransaction(user, id);
        transaction.setCategory(
                categoryService.getCategory(user, updateTransactionCategoryRequest.getCategoryId())
        );
        return ResponseEntity.ok(transactionMapper.transactionToTransactionResponse(
                transactionService.updateTransaction(
                        user,
                        id,
                        transaction
                ))
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction", description = "Update a transaction")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id,
                                                 @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest,
                                                 @AuthenticationPrincipal User user) {
        Transaction initialTransaction = transactionService.getTransaction(user, id);
        Transaction updatedTransaction = transactionMapper.updateTransactionRequestToTransaction(updateTransactionRequest);
        updatedTransaction.setUser(user);
        updatedTransaction.setAccount(initialTransaction.getAccount());
        updatedTransaction.setCategory(initialTransaction.getCategory());
        Double transactionDifference = transactionService.getTransactionDifference(initialTransaction, updatedTransaction);
        accountService.updateAccountBalance(user, initialTransaction.getAccount().getId(), transactionDifference);

        return ResponseEntity.ok(transactionMapper.transactionToTransactionResponse(
                transactionService.updateTransaction(
                        user,
                        id,
                        updatedTransaction
                ))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Delete a transaction by id")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, @AuthenticationPrincipal User user) {
        transactionService.deleteTransaction(user, id);
        return ResponseEntity.ok().build();
    }

}

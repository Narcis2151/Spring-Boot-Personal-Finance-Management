package org.fna.finance.controller;

import jakarta.validation.Valid;
import org.fna.finance.dto.*;
import org.fna.finance.mapper.TransactionMapper;
import org.fna.finance.model.DebitCredit;
import org.fna.finance.model.Transaction;
import org.fna.finance.model.User;
import org.fna.finance.service.AccountService;
import org.fna.finance.service.TransactionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transaction")
@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, AccountService accountService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions(@AuthenticationPrincipal User user) {
        return transactionMapper.transactionsToTransactionResponse(
                transactionService.getAllTransactions(user)
        );
    }

    @PostMapping
    public TransactionResponse createTransaction(@Valid @RequestBody CreateTransactionRequest createTransactionRequest,
                                                 @AuthenticationPrincipal User user) {
        Transaction transaction = transactionMapper.createTransactionRequestToTransaction(createTransactionRequest);
        transaction.setAccount(
                accountService.getAccount(user, createTransactionRequest.getAccountId())
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
    public TransactionResponse getTransaction(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return transactionMapper.transactionToTransactionResponse(
                transactionService.getTransaction(user, id)
        );
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(@PathVariable Long id,
                                                 @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest,
                                                 @AuthenticationPrincipal User user) {
        Transaction initialTransaction = transactionService.getTransaction(user, id);
        Transaction updatedTransaction = transactionMapper.updateTransactionRequestToTransaction(updateTransactionRequest);
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
    public void deleteTransaction(@PathVariable Long id, @AuthenticationPrincipal User user) {
        transactionService.deleteTransaction(user, id);
    }

}

package org.fna.finance.mapper;

import org.fna.finance.dto.CreateTransactionRequest;
import org.fna.finance.dto.TransactionResponse;
import org.fna.finance.dto.UpdateTransactionRequest;
import org.fna.finance.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {
    public Transaction createTransactionRequestToTransaction(CreateTransactionRequest createTransactionRequest) {
        return new Transaction(
                createTransactionRequest.getAmount(),
                createTransactionRequest.getDebitCredit(),
                createTransactionRequest.getParty(),
                createTransactionRequest.getDatePosted()
        );
    }

    public Transaction updateTransactionRequestToTransaction(UpdateTransactionRequest updateTransactionRequest) {
        return new Transaction(
                updateTransactionRequest.getAmount(),
                updateTransactionRequest.getDebitCredit(),
                updateTransactionRequest.getParty(),
                updateTransactionRequest.getDatePosted()
        );
    }

    public TransactionResponse transactionToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDebitCredit(),
                transaction.getAmount(),
                transaction.getParty(),
                transaction.getDatePosted(),
                transaction.getAccount().getId(),
                transaction.getCategory().getId()
        );
    }

    public List<TransactionResponse> transactionsToTransactionResponse(List<Transaction> transactions) {
        return transactions.stream()
                .map(
                        transaction -> new TransactionResponse(
                                transaction.getId(),
                                transaction.getDebitCredit(),
                                transaction.getAmount(),
                                transaction.getParty(),
                                transaction.getDatePosted(),
                                transaction.getAccount().getId(),
                                transaction.getCategory().getId()
                        )
                )
                .collect(Collectors.toList());
    }
}

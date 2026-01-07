package org.fna.finance.mapper;

import org.fna.finance.dto.CreateTransactionRequest;
import org.fna.finance.dto.TransactionResponse;
import org.fna.finance.dto.UpdateTransactionRequest;
import org.fna.finance.model.Transaction;

import java.util.List;

public interface ITransactionMapper {
    Transaction createTransactionRequestToTransaction(CreateTransactionRequest createTransactionRequest);

    Transaction updateTransactionRequestToTransaction(UpdateTransactionRequest updateTransactionRequest);

    TransactionResponse transactionToTransactionResponse(Transaction transaction);

    List<TransactionResponse> transactionsToTransactionResponse(List<Transaction> transactions);
}

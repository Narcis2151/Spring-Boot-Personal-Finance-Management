package org.fna.finance.service;

import org.fna.finance.exception.TransactionNotFoundException;
import org.fna.finance.model.Transaction;
import org.fna.finance.model.User;

import java.util.Date;
import java.util.List;

public interface ITransactionService {
    List<Transaction> getAllTransactions(User user);

    Transaction createTransaction(Transaction transaction);

    Transaction getTransaction(User user, Long id) throws TransactionNotFoundException;

    Transaction updateTransaction(User user, Long id, Transaction transaction) throws TransactionNotFoundException;

    Double getSpentAmountWithinPeriodByCategory(User user, Long categoryId, Date startDate, Date endDate);

    Double getTransactionDifference(Transaction initialTransaction, Transaction updatedTransaction);

    void deleteTransaction(User user, Long id) throws TransactionNotFoundException;
}

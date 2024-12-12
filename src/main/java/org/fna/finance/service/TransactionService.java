package org.fna.finance.service;

import org.fna.finance.exception.TransactionNotFoundException;
import org.fna.finance.model.DebitCredit;
import org.fna.finance.model.Transaction;
import org.fna.finance.model.User;
import org.fna.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions(User user) {
        return transactionRepository.findAllByUser(user);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(User user, Long id) throws TransactionNotFoundException {
        Optional<Transaction> transaction = transactionRepository.findByUserAndId(user, id);
        if (transaction.isPresent()) {
            return transaction.get();
        } else {
            throw new TransactionNotFoundException(id);
        }
    }

    public Transaction updateTransaction(User user, Long id, Transaction transaction) {
        Transaction initialTransaction = getTransaction(user, id);
        transaction.setAccount(initialTransaction.getAccount());
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    public Double getTransactionDifference(Transaction initialTransaction, Transaction updatedTransaction) {
        switch (initialTransaction.getDebitCredit()) {
            case DEBIT:
                if (updatedTransaction.getDebitCredit() == DebitCredit.DEBIT) {
                    return initialTransaction.getAmount() - updatedTransaction.getAmount();
                } else {
                    return initialTransaction.getAmount() + updatedTransaction.getAmount();
                }
            case CREDIT:
                if (updatedTransaction.getDebitCredit() == DebitCredit.CREDIT) {
                    return updatedTransaction.getAmount() - initialTransaction.getAmount();
                } else {
                    return - initialTransaction.getAmount() - updatedTransaction.getAmount();
                }
            default:
                throw new IllegalArgumentException("Unexpected value: " + initialTransaction.getDebitCredit());
        }
    }

    public void deleteTransaction(User user, Long id) throws TransactionNotFoundException {
        Optional<Transaction> transaction = transactionRepository.findByUserAndId(user, id);
        if (transaction.isPresent()) {
            transactionRepository.delete(transaction.get());
        } else {
            throw new TransactionNotFoundException(id);
        }
    }


}

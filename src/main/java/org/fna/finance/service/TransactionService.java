package org.fna.finance.service;

import org.fna.finance.exception.TransactionNotFoundException;
import org.fna.finance.model.DebitCredit;
import org.fna.finance.model.Transaction;
import org.fna.finance.model.User;
import org.fna.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions(User user) {
        return transactionRepository.findAllByUser(user);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransaction(User user, Long id) throws TransactionNotFoundException {
        Optional<Transaction> transaction = transactionRepository.findByUserAndId(user, id);
        if (transaction.isPresent()) {
            return transaction.get();
        } else {
            throw new TransactionNotFoundException(id);
        }
    }

    @Override
    public Transaction updateTransaction(User user, Long id, Transaction transaction) throws TransactionNotFoundException {
        Transaction initialTransaction = transactionRepository.findByUserAndId(user, id).orElse(null);
        if (transaction != null && initialTransaction != null) {
            initialTransaction.setAmount(transaction.getAmount());
            initialTransaction.setDebitCredit(transaction.getDebitCredit());
            initialTransaction.setDatePosted(transaction.getDatePosted());
            initialTransaction.setParty(transaction.getParty());
            initialTransaction.setUser(transaction.getUser());
            initialTransaction.setAccount(transaction.getAccount());
            initialTransaction.setCategory(transaction.getCategory());
            return transactionRepository.save(initialTransaction);
        } else {
            throw new TransactionNotFoundException(id);
        }
    }

    @Override
    public Double getSpentAmountWithinPeriodByCategory(User user, Long categoryId, Date startDate, Date endDate) {
        return transactionRepository.getSpentAmountWithinPeriodByCategory(user, categoryId, startDate, endDate);
    }

    @Override
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
                    return -initialTransaction.getAmount() - updatedTransaction.getAmount();
                }
            default:
                throw new IllegalArgumentException("Unexpected value: " + initialTransaction.getDebitCredit());
        }
    }

    @Override
    public void deleteTransaction(User user, Long id) throws TransactionNotFoundException {
        Optional<Transaction> transaction = transactionRepository.findByUserAndId(user, id);
        if (transaction.isPresent()) {
            transactionRepository.delete(transaction.get());
        } else {
            throw new TransactionNotFoundException(id);
        }
    }


}

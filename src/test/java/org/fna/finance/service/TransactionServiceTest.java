package org.fna.finance.service;

import org.fna.finance.exception.TransactionNotFoundException;
import org.fna.finance.model.*;
import org.fna.finance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTransactions_Success() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, DebitCredit.DEBIT, 100.00, "Mega Image", new Date(), user, new Account(), new Category()),
                new Transaction(2L, DebitCredit.CREDIT, 200.00, "Salary", new Date(), user, new Account(), new Category())
        );

        when(transactionRepository.findAllByUser(user)).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionRepository).findAllByUser(user);
    }

    @Test
    void createTransaction_Success() {
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 100.00, "Mega Image", new Date(), new User(), new Account(), new Category());
        Transaction savedTransaction = new Transaction(1L, DebitCredit.DEBIT, 100.00, "Mega Image", new Date(), new User(), new Account(), new Category());

        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);

        Transaction result = transactionService.createTransaction(transaction);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void getTransaction_Success() throws TransactionNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 100.00, "Mega Image", new Date(), user, new Account(), new Category());

        when(transactionRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransaction(user, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(transactionRepository).findByUserAndId(user, 1L);
    }

    @Test
    void getTransaction_TransactionNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(transactionRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransaction(user, 1L));

        verify(transactionRepository).findByUserAndId(user, 1L);
    }

    @Test
    void updateTransaction_Success() throws TransactionNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 200.00, "Mega Image", new Date(), user, new Account(), new Category());

        when(transactionRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.updateTransaction(user, 1L, transaction);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(200.0, result.getAmount());
        verify(transactionRepository).findByUserAndId(user, 1L);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void updateTransaction_TransactionNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 200.00, "Mega Image", new Date(), user, new Account(), new Category());

        when(transactionRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction(user, 1L, transaction));

        verify(transactionRepository).findByUserAndId(user, 1L);
    }

    @Test
    void getSpentAmountWithinPeriodByCategory_Success() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Date startDate = new Date();
        Date endDate = new Date();

        when(transactionRepository.getSpentAmountWithinPeriodByCategory(user, 1L, startDate, endDate)).thenReturn(100.00);

        Double result = transactionService.getSpentAmountWithinPeriodByCategory(user, 1L, startDate, endDate);

        assertNotNull(result);
        assertEquals(100.00, result);
        verify(transactionRepository).getSpentAmountWithinPeriodByCategory(user, 1L, startDate, endDate);
    }

    @Test
    void getTransactionDifference_DebitCredit() {
        Transaction initialTransaction = new Transaction(100.00, DebitCredit.DEBIT, "Mega Image", new Date());
        Transaction updatedTransaction = new Transaction(50.00, DebitCredit.CREDIT, "Mega Image", new Date());

        Double result = transactionService.getTransactionDifference(initialTransaction, updatedTransaction);

        assertEquals(150.00, result);
    }

    @Test
    void getTransactionDifference_DebitDebit() {
        Transaction initialTransaction = new Transaction(100.00, DebitCredit.DEBIT, "Mega Image", new Date());
        Transaction updatedTransaction = new Transaction(150.00, DebitCredit.DEBIT, "Mega Image", new Date());

        Double result = transactionService.getTransactionDifference(initialTransaction, updatedTransaction);

        assertEquals(-50.00, result);
    }

    @Test
    void getTransactionDifference_CreditCredit() {
        Transaction initialTransaction = new Transaction(100.00, DebitCredit.CREDIT, "Mega Image", new Date());
        Transaction updatedTransaction = new Transaction(150.00, DebitCredit.CREDIT, "Mega Image", new Date());

        Double result = transactionService.getTransactionDifference(initialTransaction, updatedTransaction);

        assertEquals(50.00, result);
    }

    @Test
    void getTransactionDifference_CreditDebit() {
        Transaction initialTransaction = new Transaction(100.00, DebitCredit.CREDIT, "Mega Image", new Date());
        Transaction updatedTransaction = new Transaction(50.00, DebitCredit.DEBIT, "Mega Image", new Date());

        Double result = transactionService.getTransactionDifference(initialTransaction, updatedTransaction);

        assertEquals(-150.00, result);
    }

    @Test
    void deleteTransaction_Success() throws TransactionNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 100.00, "Mega Image", new Date(), user, new Account(), new Category());

        when(transactionRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(user, 1L);

        verify(transactionRepository).findByUserAndId(user, 1L);
        verify(transactionRepository).delete(transaction);
    }

    @Test
    void deleteTransaction_TransactionNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(transactionRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(user, 1L));

        verify(transactionRepository).findByUserAndId(user, 1L);
        verifyNoMoreInteractions(transactionRepository);
    }
}
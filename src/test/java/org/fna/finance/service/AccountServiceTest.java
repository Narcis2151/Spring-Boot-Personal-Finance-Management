package org.fna.finance.service;

import org.fna.finance.exception.AccountNotFoundException;
import org.fna.finance.exception.NotEnoughFundsException;
import org.fna.finance.model.Account;
import org.fna.finance.model.Currency;
import org.fna.finance.model.User;
import org.fna.finance.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAccounts_Success() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        List<Account> accounts = Arrays.asList(
                new Account("Savings", new Currency(), 1000.0, user),
                new Account("Checking", new Currency(), 500.0, user)
        );

        when(accountRepository.findAllByUser(user)).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository).findAllByUser(user);
    }

    @Test
    void createAccount_Success() {
        Account account = new Account(1L, "Savings", new Currency(), 1000.0, new User());
        Account savedAccount = new Account(1L, "Savings", new Currency(), 1000.0, new User());

        when(accountRepository.save(account)).thenReturn(savedAccount);

        Account result = accountService.createAccount(account);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(accountRepository).save(account);
    }

    @Test
    void getAccount_Success() throws AccountNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Account account = new Account(1L, "Savings", new Currency(), 1000.0, user);

        when(accountRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccount(user, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(accountRepository).findByUserAndId(user, 1L);
    }

    @Test
    void getAccount_AccountNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(accountRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(user, 1L));

        verify(accountRepository).findByUserAndId(user, 1L);
    }

    @Test
    void updateAccountBalance_Success() throws AccountNotFoundException, NotEnoughFundsException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Account account = new Account(1L, "Savings", new Currency(), 1000.0, user);

        when(accountRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.updateAccountBalance(user, 1L, 500.0);

        assertNotNull(result);
        assertEquals(1500.0, result.getBalance());
        verify(accountRepository).findByUserAndId(user, 1L);
        verify(accountRepository).save(account);
    }

    @Test
    void updateAccountBalance_NotEnoughFundsException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Account account = new Account(1L, "Savings", new Currency(), 100.0, user);

        when(accountRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(account));

        assertThrows(NotEnoughFundsException.class, () -> accountService.updateAccountBalance(user, 1L, -200.0));

        verify(accountRepository).findByUserAndId(user, 1L);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void updateAccountBalance_AccountNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(accountRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccountBalance(user, 1L, 500.0));

        verify(accountRepository).findByUserAndId(user, 1L);
    }

    @Test
    void deleteAccount_Success() throws AccountNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Account account = new Account(1L, "Savings", new Currency(), 1000.0, user);

        when(accountRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(account));

        accountService.deleteAccount(user, 1L);

        verify(accountRepository).findByUserAndId(user, 1L);
        verify(accountRepository).delete(account);
    }

    @Test
    void deleteAccount_AccountNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(accountRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(user, 1L));

        verify(accountRepository).findByUserAndId(user, 1L);
        verifyNoMoreInteractions(accountRepository);
    }
}
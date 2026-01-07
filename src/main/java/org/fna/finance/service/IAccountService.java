package org.fna.finance.service;

import org.fna.finance.exception.AccountNotFoundException;
import org.fna.finance.exception.NotEnoughFundsException;
import org.fna.finance.model.Account;
import org.fna.finance.model.User;

import java.util.List;

public interface IAccountService {
    List<Account> getAllAccounts(User user);

    Account createAccount(Account account);

    Account getAccount(User user, Long id) throws AccountNotFoundException;

    Account updateAccountBalance(User user, Long id, double amount) throws AccountNotFoundException, NotEnoughFundsException;

    void deleteAccount(User user, Long id) throws AccountNotFoundException;
}

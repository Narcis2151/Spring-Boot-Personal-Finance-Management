package org.fna.finance.service;

import org.fna.finance.exception.AccountNotFoundException;
import org.fna.finance.exception.NotEnoughFundsException;
import org.fna.finance.model.*;
import org.fna.finance.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts(User user) {
        return accountRepository.findAllByUser(user);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccount(User user, Long id) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findByUserAndId(user, id);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new AccountNotFoundException(id);
        }
    }

    public Account updateAccountBalance(User user, Long id, double amount) throws AccountNotFoundException, NotEnoughFundsException {
        Account account = accountRepository.findByUserAndId(user, id).orElse(null);
        if (account != null) {
            if (account.getBalance() + amount >= 0) {
                account.setBalance(account.getBalance() + amount);
                return accountRepository.save(account);
            } else {
                throw new NotEnoughFundsException(account.getBalance(), amount);
            }
        } else {
            throw new AccountNotFoundException(id);
        }
    }

    public void deleteAccount(User user, Long id) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findByUserAndId(user, id);
        if (account.isPresent()) {
            accountRepository.delete(account.get());
        } else {
            throw new AccountNotFoundException(id);
        }
    }


}

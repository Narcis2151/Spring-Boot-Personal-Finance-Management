package org.fna.finance.mapper;

import org.fna.finance.dto.CreateAccountRequest;
import org.fna.finance.dto.AccountResponse;
import org.fna.finance.model.Account;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AccountMapper {
    public Account accountRequestToAccount(CreateAccountRequest createAccountRequest) {
        return new Account(createAccountRequest.getName(), createAccountRequest.getBalance());
    }

    public AccountResponse accountToAccountResponse(Account account) {
        return new AccountResponse(account.getId(), account.getName(), account.getCurrency().getName(), account.getBalance());
    }

    public List<AccountResponse> accountsToAccountResponses(List<Account> accounts) {
        return accounts.stream()
                .map(account -> new AccountResponse(account.getId(), account.getName(), account.getCurrency().getName(), account.getBalance()))
                .collect(Collectors.toList());
    }
}

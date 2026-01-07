package org.fna.finance.mapper;

import org.fna.finance.dto.AccountResponse;
import org.fna.finance.dto.CreateAccountRequest;
import org.fna.finance.model.Account;

import java.util.List;

public interface IAccountMapper {
    Account accountRequestToAccount(CreateAccountRequest createAccountRequest);

    AccountResponse accountToAccountResponse(Account account);

    List<AccountResponse> accountsToAccountResponses(List<Account> accounts);
}

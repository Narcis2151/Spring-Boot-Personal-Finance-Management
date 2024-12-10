package org.fna.finance.controller;

import jakarta.validation.Valid;
import org.fna.finance.dto.AccountResponse;
import org.fna.finance.dto.CreateAccountRequest;
import org.fna.finance.dto.UpdateAccountBalanceRequest;
import org.fna.finance.mapper.AccountMapper;
import org.fna.finance.model.*;
import org.fna.finance.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/account")
@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts(@AuthenticationPrincipal User user) {
        return accountMapper.accountsToAccountResponses(
                accountService.getAllAccounts(user)
        );
    }

    @PostMapping
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest,
                                         @AuthenticationPrincipal User user) {
        Account account = accountMapper.accountRequestToAccount(createAccountRequest);
        account.setUser(user);
        Account createdAccount = accountService.createAccount(account);
        return accountMapper.accountToAccountResponse(createdAccount);
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return accountMapper.accountToAccountResponse(
                accountService.getAccount(user, id)
        );
    }

    @PutMapping("/{id}")
    public AccountResponse updateAccountBalance(@PathVariable Long id,
                                                @Valid @RequestBody UpdateAccountBalanceRequest balance,
                                                @AuthenticationPrincipal User user) {
        return accountMapper.accountToAccountResponse(
                accountService.updateAccountBalance(user, id, balance.getBalance())
        );
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id, @AuthenticationPrincipal User user) {
        accountService.deleteAccount(user, id);
    }

}

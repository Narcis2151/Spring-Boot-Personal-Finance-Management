package org.fna.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.fna.finance.dto.AccountResponse;
import org.fna.finance.dto.CreateAccountRequest;
import org.fna.finance.dto.UpdateAccountBalanceRequest;
import org.fna.finance.mapper.AccountMapper;
import org.fna.finance.model.*;
import org.fna.finance.service.AccountService;
import org.fna.finance.service.CurrencyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/account")
@RestController
@Tag(name = "Accounts", description = "Endpoints for accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final CurrencyService currencyService;

    public AccountController(AccountService accountService, AccountMapper accountMapper, CurrencyService currencyService) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.currencyService = currencyService;
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Get all accounts for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public List<AccountResponse> getAllAccounts(@AuthenticationPrincipal User user) {
        return accountMapper.accountsToAccountResponses(
                accountService.getAllAccounts(user)
        );
    }

    @PostMapping
    @Operation(summary = "Create account", description = "Create a new account for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Account created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest,
                                         @AuthenticationPrincipal User user) {
        Account account = accountMapper.accountRequestToAccount(createAccountRequest);
        account.setUser(user);
        account.setCurrency(
                currencyService.getCurrency(createAccountRequest.getCurrency())
        );
        Account createdAccount = accountService.createAccount(account);
        return accountMapper.accountToAccountResponse(createdAccount);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account", description = "Get an account by id for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Account not found"),
    })
    public AccountResponse getAccount(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return accountMapper.accountToAccountResponse(
                accountService.getAccount(user, id)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account amount", description = "Update the amount of an account by id for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Account amount updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Account not found"),
    })
    public AccountResponse updateAccountBalance(@PathVariable Long id,
                                                @Valid @RequestBody UpdateAccountBalanceRequest balance,
                                                @AuthenticationPrincipal User user) {
        return accountMapper.accountToAccountResponse(
                accountService.updateAccountBalance(user, id, balance.getAmount())
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account", description = "Delete an account by id for the authenticated user", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Account deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Account not found"),

    })
    public void deleteAccount(@PathVariable Long id, @AuthenticationPrincipal User user) {
        accountService.deleteAccount(user, id);
    }

}

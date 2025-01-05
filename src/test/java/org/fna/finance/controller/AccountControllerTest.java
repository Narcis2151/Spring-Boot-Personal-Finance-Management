package org.fna.finance.controller;

import org.fna.finance.dto.AccountResponse;
import org.fna.finance.exception.AccountNotFoundException;
import org.fna.finance.mapper.AccountMapper;
import org.fna.finance.model.Account;
import org.fna.finance.model.Currency;
import org.fna.finance.model.User;
import org.fna.finance.service.AccountService;
import org.fna.finance.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AccountMapper accountMapper;

    @MockitoBean
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    public void getAllAccounts_Success() throws Exception {
        List<Account> accounts = new ArrayList<>();
        Currency currency = new Currency("USD", 4.11);
        accounts.add(new Account(1L, "Test Account", currency, 1000.0, new User()));
        accounts.add(new Account(2L, "Test Account 2", currency, 2000.0, new User()));

        List<AccountResponse> accountResponses = new ArrayList<>();
        accountResponses.add(new AccountResponse(1L, "Test Account", "USD", 1000.0));
        accountResponses.add(new AccountResponse(2L, "Test Account 2", "USD", 2000.0));

        when(accountService.getAllAccounts(any(User.class))).thenReturn(accounts);
        when(accountMapper.accountsToAccountResponses(any())).thenReturn(accountResponses);

        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Account"))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].balance").value(1000.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Test Account 2"))
                .andExpect(jsonPath("$[1].currency").value("USD"))
                .andExpect(jsonPath("$[1].balance").value(2000.0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void createAccount_Success() throws Exception {
        Currency currency = new Currency("USD", 4.11);
        Account account = new Account(1L, "Test Account", currency, 1000.0, new User());
        AccountResponse accountResponse = new AccountResponse(1L, "Test Account", "USD", 1000.0);

        when(accountMapper.accountRequestToAccount(any())).thenReturn(account);
        when(currencyService.getCurrency("USD")).thenReturn(currency);
        when(accountService.createAccount(any())).thenReturn(account);
        when(accountMapper.accountToAccountResponse(any())).thenReturn(accountResponse);

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content("{\"name\": \"Test Account\", \"currency\": \"USD\", \"balance\": 1000.0}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.balance").value(1000.0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void createAccount_BadRequest() throws Exception {
        when(accountMapper.accountRequestToAccount(any())).thenThrow(new IllegalArgumentException("Invalid input"));

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content("{\"name\": \"Test Account\", \"currency\": \"USD\"}")
                )
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void getAccount_Success() throws Exception {
        Currency currency = new Currency("USD", 4.11);
        Account account = new Account(1L, "Test Account", currency, 1000.0, new User());
        AccountResponse accountResponse = new AccountResponse(1L, "Test Account", "USD", 1000.0);

        when(accountService.getAccount(any(), any())).thenReturn(account);
        when(accountMapper.accountToAccountResponse(any())).thenReturn(accountResponse);

        mockMvc.perform(get("/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.balance").value(1000.0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void getAccount_AccountNotFound() throws Exception {
        when(accountService.getAccount(any(), any())).thenThrow(new AccountNotFoundException(1L));

        mockMvc.perform(get("/account/1"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void updateAccountBalance_Success() throws Exception {
        Currency currency = new Currency("USD", 4.11);
        Account account = new Account(1L, "Test Account", currency, 1000.0, new User());
        AccountResponse accountResponse = new AccountResponse(1L, "Test Account", "USD", 2000.0);

        when(accountService.updateAccountBalance(any(User.class), any(Long.class), any(double.class))).thenReturn(account);
        when(accountMapper.accountToAccountResponse(any())).thenReturn(accountResponse);

        mockMvc.perform(put("/account/1")
                        .contentType("application/json")
                        .content("{\"balance\": 2000.0}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.balance").value(2000.0))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void updateAccountBalance_AccountNotFound() throws Exception {
        when(accountService.updateAccountBalance(any(), any(), anyDouble())).thenThrow(new AccountNotFoundException(1L));

        mockMvc.perform(put("/account/1")
                        .contentType("application/json")
                        .content("{\"balance\": 2000.0}")
                )
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @WithMockUser
    public void deleteAccount_Success() throws Exception {
        mockMvc.perform(delete("/account/1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void deleteAccount_AccountNotFound() throws Exception {
        doThrow(new AccountNotFoundException(1L)).when(accountService).deleteAccount(any(), any());

        mockMvc.perform(delete("/account/1"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}

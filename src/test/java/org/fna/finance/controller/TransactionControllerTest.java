package org.fna.finance.controller;

import org.fna.finance.dto.TransactionResponse;
import org.fna.finance.exception.AccountNotFoundException;
import org.fna.finance.exception.CategoryNotFoundException;
import org.fna.finance.exception.TransactionNotFoundException;
import org.fna.finance.mapper.TransactionMapper;
import org.fna.finance.model.*;
import org.fna.finance.service.AccountService;
import org.fna.finance.service.CategoryService;
import org.fna.finance.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
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
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private TransactionMapper transactionMapper;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    public void getAllTransactions_Success() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), new User(), new Account(), new Category()));
        transactions.add(new Transaction(2L, DebitCredit.DEBIT, 2000.0, "Test Party", new Date(), new User(), new Account(), new Category()));

        List<TransactionResponse> transactionResponses = new ArrayList<>();
        transactionResponses.add(new TransactionResponse(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), 1L, 1L));
        transactionResponses.add(new TransactionResponse(2L, DebitCredit.DEBIT, 2000.0, "Test Party", new Date(), 1L, 1L));

        when(transactionService.getAllTransactions(any())).thenReturn(transactions);
        when(transactionMapper.transactionsToTransactionResponse(any())).thenReturn(transactionResponses);

        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].debitCredit").value("DEBIT"))
                .andExpect(jsonPath("$[0].amount").value(1000.0))
                .andExpect(jsonPath("$[0].party").value("Test Party"))
                .andExpect(jsonPath("$[0].datePosted").exists())
                .andExpect(jsonPath("$[0].accountId").value(1))
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].debitCredit").value("DEBIT"))
                .andExpect(jsonPath("$[1].amount").value(2000.0))
                .andExpect(jsonPath("$[1].party").value("Test Party"))
                .andExpect(jsonPath("$[1].datePosted").exists())
                .andExpect(jsonPath("$[1].accountId").value(1))
                .andExpect(jsonPath("$[1].categoryId").value(1));
    }

    @Test
    @WithMockUser
    public void createTransaction_Success() throws Exception {
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), new User(), new Account(), new Category());
        TransactionResponse transactionResponse = new TransactionResponse(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), 1L, 1L);

        when(transactionMapper.createTransactionRequestToTransaction(any())).thenReturn(transaction);
        when(transactionService.createTransaction(any())).thenReturn(transaction);
        when(accountService.getAccount(any(), any())).thenReturn(new Account(1L, "Test Account", new Currency("USD", 4.11), 1000.0, new User()));
        when(categoryService.getCategory(any(), any())).thenReturn(new Category(1L, "Test Category", new User()));
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(transactionResponse);

        mockMvc.perform(post("/transaction")
                        .contentType("application/json")
                        .content("{\"amount\":1000.0,\"debitCredit\":\"DEBIT\",\"party\":\"Test Party\",\"accountId\":1,\"categoryId\":1, \"datePosted\":\"2025-01-12T09:46:21.488Z\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.debitCredit").value("DEBIT"))
                .andExpect(jsonPath("$.amount").value(1000.0))
                .andExpect(jsonPath("$.party").value("Test Party"))
                .andExpect(jsonPath("$.datePosted").exists())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    @Test
    @WithMockUser
    public void getTransaction_Success() throws Exception {
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), new User(), new Account(), new Category());
        TransactionResponse transactionResponse = new TransactionResponse(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), 1L, 1L);

        when(transactionService.getTransaction(any(), any())).thenReturn(transaction);
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(transactionResponse);

        mockMvc.perform(get("/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.debitCredit").value("DEBIT"))
                .andExpect(jsonPath("$.amount").value(1000.0))
                .andExpect(jsonPath("$.party").value("Test Party"))
                .andExpect(jsonPath("$.datePosted").exists())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    @Test
    @WithMockUser
    public void getTransaction_NotFound() throws Exception {
        doThrow(new TransactionNotFoundException(1L)).when(transactionService).getTransaction(any(), any());

        mockMvc.perform(get("/transaction/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void updateTransaction_Success() throws Exception {
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), new User(), new Account(), new Category());
        TransactionResponse transactionResponse = new TransactionResponse(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), 1L, 1L);

        when(transactionService.getTransaction(any(), any())).thenReturn(transaction);
        when(transactionMapper.updateTransactionRequestToTransaction(any())).thenReturn(transaction);
        when(accountService.getAccount(any(), any())).thenReturn(new Account(1L, "Test Account", new Currency("USD", 4.11), 1000.0, new User()));
        when(transactionService.getTransactionDifference(any(), any())).thenReturn(1000.0);
        when(accountService.updateAccountBalance(any(), any(), anyDouble())).thenReturn(new Account(1L, "Test Account", new Currency("USD", 4.11), 2000.0, new User()));
        when(transactionService.updateTransaction(any(), any(), any())).thenReturn(transaction);
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(transactionResponse);

        mockMvc.perform(put("/transaction/1")
                        .contentType("application/json")
                        .content("{\"amount\":1000.0,\"debitCredit\":\"DEBIT\",\"party\":\"Test Party\",\"accountId\": 1,\"datePosted\":\"2025-01-12T09:46:21.488Z\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.debitCredit").value("DEBIT"))
                .andExpect(jsonPath("$.amount").value(1000.0))
                .andExpect(jsonPath("$.party").value("Test Party"))
                .andExpect(jsonPath("$.datePosted").exists())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    @Test
    @WithMockUser
    public void updateTransaction_NotFound() throws Exception {
        when(transactionService.getTransaction(any(), any())).thenThrow(new TransactionNotFoundException(1L));

        mockMvc.perform(put("/transaction/1")
                        .contentType("application/json")
                        .content("{\"amount\":1000.0,\"debitCredit\":\"DEBIT\",\"party\":\"Test Party\",\"accountId\": 1,\"datePosted\":\"2025-01-12T09:46:21.488Z\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void updateTransaction_AccountNotFound() throws Exception {
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), new User(), new Account(), new Category());

        when(transactionMapper.updateTransactionRequestToTransaction(any())).thenReturn(transaction);
        when(accountService.getAccount(any(), any())).thenThrow(new AccountNotFoundException(1L));
        when(transactionService.getTransaction(any(), any())).thenReturn(transaction);
        when(transactionService.getTransactionDifference(any(), any())).thenThrow(new AccountNotFoundException(1L));

        mockMvc.perform(put("/transaction/1")
                        .contentType("application/json")
                        .content("{\"amount\":1000.0,\"debitCredit\":\"DEBIT\",\"party\":\"Test Party\",\"accountId\": 1,\"datePosted\":\"2025-01-12T09:46:21.488Z\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void updateTransactionCategory_Success() throws Exception {
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), new User(), new Account(), new Category());
        TransactionResponse transactionResponse = new TransactionResponse(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), 1L, 1L);

        when(transactionService.getTransaction(any(), any())).thenReturn(transaction);
        when(categoryService.getCategory(any(), any())).thenReturn(new Category(1L, "Test Category", new User()));
        when(transactionService.updateTransaction(any(), any(), any())).thenReturn(transaction);
        when(transactionMapper.transactionToTransactionResponse(any())).thenReturn(transactionResponse);

        mockMvc.perform(put("/transaction/1/category")
                        .contentType("application/json")
                        .content("{\"categoryId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.debitCredit").value("DEBIT"))
                .andExpect(jsonPath("$.amount").value(1000.0))
                .andExpect(jsonPath("$.party").value("Test Party"))
                .andExpect(jsonPath("$.datePosted").exists())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    @Test
    @WithMockUser
    public void updateTransactionCategory_NotFound() throws Exception {
        when(transactionService.getTransaction(any(), any())).thenThrow(new TransactionNotFoundException(1L));

        mockMvc.perform(put("/transaction/1/category")
                        .contentType("application/json")
                        .content("{\"categoryId\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void updateTransactionCategory_CategoryNotFound() throws Exception {
        Transaction transaction = new Transaction(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), new User(), new Account(), new Category());
        TransactionResponse transactionResponse = new TransactionResponse(1L, DebitCredit.DEBIT, 1000.0, "Test Party", new Date(), 1L, 1L);

        when(transactionService.getTransaction(any(), any())).thenReturn(transaction);
        when(categoryService.getCategory(any(), any())).thenThrow(new CategoryNotFoundException(1L));

        mockMvc.perform(put("/transaction/1/category")
                        .contentType("application/json")
                        .content("{\"categoryId\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void deleteTransaction_Success() throws Exception {
        mockMvc.perform(delete("/transaction/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void deleteTransaction_NotFound() throws Exception {
        doThrow(new TransactionNotFoundException(1L)).when(transactionService).deleteTransaction(any(), any());

        mockMvc.perform(delete("/transaction/1"))
                .andExpect(status().isNotFound());
    }
}

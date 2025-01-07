package org.fna.finance.controller;

import org.fna.finance.dto.BudgetResponse;
import org.fna.finance.exception.BudgetNotFoundException;
import org.fna.finance.mapper.BudgetMapper;
import org.fna.finance.model.Budget;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.fna.finance.service.BudgetService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BudgetService budgetService;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BudgetMapper budgetMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    public void getAllBudgets_Success() throws Exception {
        List<Budget> budgets = new ArrayList<>();
        budgets.add(new Budget(1L, 1000.0, new Category(1L, "Test Category", new User()), new User()));
        budgets.add(new Budget(2L, 2000.0, new Category(2L, "Test Category 2", new User()), new User()));
        Date budgetStartDate = new Date();
        Date budgetEndDate = new Date();

        List<BudgetResponse> budgetResponses = new ArrayList<>();
        budgetResponses.add(new BudgetResponse(1L, 1000.0, 550.0, budgetStartDate.toString(), budgetEndDate.toString(), 1L));
        budgetResponses.add(new BudgetResponse(2L, 2000.0, 0.0, budgetStartDate.toString(), budgetEndDate.toString(), 2L));

        when(budgetService.getAllBudgets(any())).thenReturn(budgets);
        when(budgetMapper.budgetListToBudgetResponseList(any())).thenReturn(budgetResponses);

        mockMvc.perform(get("/budget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amountAvailable").value(1000.0))
                .andExpect(jsonPath("$[0].amountSpent").value(0.0))
                .andExpect(jsonPath("$[0].startDate").value(budgetStartDate.toString()))
                .andExpect(jsonPath("$[0].endDate").value(budgetEndDate.toString()))
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].amountAvailable").value(2000.0))
                .andExpect(jsonPath("$[1].amountSpent").value(0.0))
                .andExpect(jsonPath("$[1].startDate").value(budgetStartDate.toString()))
                .andExpect(jsonPath("$[1].endDate").value(budgetEndDate.toString()))
                .andExpect(jsonPath("$[1].categoryId").value(2))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void createBudget_Success() throws Exception {
        Budget budget = new Budget(1L, 1000.0, new Category(1L, "Test Category", new User()), new User());
        BudgetResponse budgetResponse = new BudgetResponse(1L, 1000.0, 0.0, "2024-12-01", "2025-01-01", 1L);

        when(budgetMapper.createBudgetRequestToBudget(any())).thenReturn(budget);
        when(categoryService.getCategory(any(), anyLong())).thenReturn(new Category(1L, "Test Category", new User()));
        when(budgetService.createBudget(any())).thenReturn(budget);
        when(budgetMapper.budgetToBudgetResponse(any())).thenReturn(budgetResponse);

        mockMvc.perform(post("/budget")
                        .contentType("application/json")
                        .content("{\"amountAvailable\": 1000.0, \"categoryId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amountAvailable").value(1000.0))
                .andExpect(jsonPath("$.amountSpent").value(0.0))
                .andExpect(jsonPath("$.startDate").value("2024-12-01"))
                .andExpect(jsonPath("$.endDate").value("2025-01-01"))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void createBudget_BadRequest() throws Exception {
        mockMvc.perform(post("/budget")
                        .contentType("application/json")
                        .content("{\"amountAvailable\": 1000.0}"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void getBudget_Success() throws Exception {
        Budget budget = new Budget(1L, 1000.0, new Category(1L, "Test Category", new User()), new User());
        Date budgetStartDate = new Date();
        Date budgetEndDate = new Date();
        BudgetResponse budgetResponse = new BudgetResponse(1L, 1000.0, 0.0, budgetStartDate.toString(), budgetEndDate.toString(), 1L);

        when(budgetService.getBudget(any(), anyLong())).thenReturn(budget);
        when(budgetMapper.budgetToBudgetResponse(any())).thenReturn(budgetResponse);

        mockMvc.perform(get("/budget/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amountAvailable").value(1000.0))
                .andExpect(jsonPath("$.amountSpent").value(0.0))
                .andExpect(jsonPath("$.startDate").value(budgetStartDate.toString()))
                .andExpect(jsonPath("$.endDate").value(budgetEndDate.toString()))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void getBudget_BudgetNotFoundException() throws Exception {
        doThrow(new BudgetNotFoundException(1L)).when(budgetService).getBudget(any(), anyLong());

        mockMvc.perform(get("/budget/1"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void updateBudget_Success() throws Exception {
        Budget updatedBudget = new Budget(1L, 1000.0, new Category(1L, "Test Category", new User()), new User());
        Date budgetStartDate = new Date();
        Date budgetEndDate = new Date();
        BudgetResponse budgetResponse = new BudgetResponse(1L, 1000.0, 0.0, budgetStartDate.toString(), budgetEndDate.toString(), 1L);

        when(budgetService.updateBudgetAmountAvailable(any(), anyLong(), anyDouble())).thenReturn(updatedBudget);
        when(budgetMapper.budgetToBudgetResponse(any())).thenReturn(budgetResponse);

        mockMvc.perform(put("/budget/1")
                        .contentType("application/json")
                        .content("{\"amountAvailable\": 1000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amountAvailable").value(1000.0))
                .andExpect(jsonPath("$.amountSpent").value(0.0))
                .andExpect(jsonPath("$.startDate").value(budgetStartDate.toString()))
                .andExpect(jsonPath("$.endDate").value(budgetEndDate.toString()))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void updateBudget_BadRequest() throws Exception {
        mockMvc.perform(put("/budget/1")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void updateBudget_BudgetNotFoundException() throws Exception {
        doThrow(new BudgetNotFoundException(1L)).when(budgetService).updateBudgetAmountAvailable(any(), anyLong(), anyDouble());

        mockMvc.perform(put("/budget/1")
                        .contentType("application/json")
                        .content("{\"amountAvailable\": 1000.0}"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void deleteBudget_Success() throws Exception {
        mockMvc.perform(delete("/budget/1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void deleteBudget_BudgetNotFoundException() throws Exception {
        doThrow(new BudgetNotFoundException(1L)).when(budgetService).deleteBudget(any(), anyLong());

        mockMvc.perform(delete("/budget/1"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}

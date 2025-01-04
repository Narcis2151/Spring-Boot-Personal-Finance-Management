package org.fna.finance.service;

import org.fna.finance.exception.BudgetNotFoundException;
import org.fna.finance.model.Budget;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.fna.finance.repository.BudgetRepository;
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

class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_Success() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Category category1 = new Category("Food", user);
        Category category2 = new Category("Transport", user);
        List<Budget> categories = Arrays.asList(
                new Budget(1200.00, category1, user),
                new Budget(800.00, category2, user)
        );

        when(budgetRepository.findAllByUser(user)).thenReturn(categories);

        List<Budget> result = budgetService.getAllBudgets(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(budgetRepository).findAllByUser(user);
    }

    @Test
    void createBudget_Success() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Category category = new Category("Food", user);
        Budget budget = new Budget(1L, 1000.00, category, user);
        Budget savedBudget = new Budget(1L, 1000.00, category, user);

        when(budgetRepository.save(budget)).thenReturn(savedBudget);

        Budget result = budgetService.createBudget(budget);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(budgetRepository).save(budget);
    }

    @Test
    void getBudget_Success() throws BudgetNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Category category = new Category("Food", user);
        Budget budget = new Budget(1L, 1000.00, category, user);

        when(budgetRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(budget));

        Budget result = budgetService.getBudget(user, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(budgetRepository).findByUserAndId(user, 1L);
    }

    @Test
    void getBudget_BudgetNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(budgetRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(BudgetNotFoundException.class, () -> budgetService.getBudget(user, 1L));

        verify(budgetRepository).findByUserAndId(user, 1L);
    }

    @Test
    void updateBudget_Success() throws BudgetNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Category category = new Category("Food", user);
        Budget budget = new Budget(1L, 1000.00, category, user);

        when(budgetRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget result = budgetService.updateBudgetAmountAvailable(user, 1L, 1200.00);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1200.00, result.getAmountAvailable());
        verify(budgetRepository).findByUserAndId(user, 1L);
        verify(budgetRepository).save(budget);
    }

    @Test
    void updateBudget_BudgetNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(budgetRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(BudgetNotFoundException.class, () -> budgetService.updateBudgetAmountAvailable(user, 1L, 1200.00));

        verify(budgetRepository).findByUserAndId(user, 1L);
    }

    @Test
    void deleteBudget_Success() throws BudgetNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Category category = new Category("Food", user);
        Budget budget = new Budget(1L, 1000.00, category, user);

        when(budgetRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(budget));

        budgetService.deleteBudget(user, 1L);

        verify(budgetRepository).findByUserAndId(user, 1L);
        verify(budgetRepository).delete(budget);
    }

    @Test
    void deleteBudget_BudgetNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(budgetRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(BudgetNotFoundException.class, () -> budgetService.deleteBudget(user, 1L));

        verify(budgetRepository).findByUserAndId(user, 1L);
        verifyNoMoreInteractions(budgetRepository);
    }
}
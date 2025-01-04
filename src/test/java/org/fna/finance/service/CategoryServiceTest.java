package org.fna.finance.service;

import org.fna.finance.exception.CategoryNotFoundException;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.fna.finance.repository.CategoryRepository;
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

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_Success() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        List<Category> categories = Arrays.asList(
                new Category("Food", user),
                new Category("Transport", user)
        );

        when(categoryRepository.findAllByUser(user)).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository).findAllByUser(user);
    }

    @Test
    void createCategory_Success() {
        Category category = new Category("Food", new User());
        Category savedCategory = new Category(1L, "Food", new User());

        when(categoryRepository.save(category)).thenReturn(savedCategory);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository).save(category);
    }

    @Test
    void getCategory_Success() throws CategoryNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Category category = new Category(1L, "Food", user);

        when(categoryRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategory(user, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository).findByUserAndId(user, 1L);
    }

    @Test
    void getCategory_CategoryNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(categoryRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(user, 1L));

        verify(categoryRepository).findByUserAndId(user, 1L);
    }

    @Test
    void updateCategory_Success() throws CategoryNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Category category = new Category(1L, "Food", user);

        when(categoryRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.updateCategoryName(user, 1L, "Food And Drinks");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Food And Drinks", result.getName());
        verify(categoryRepository).findByUserAndId(user, 1L);
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategory_CategoryNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(categoryRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategoryName(user, 1L, "Food And Drinks"));

        verify(categoryRepository).findByUserAndId(user, 1L);
    }

    @Test
    void deleteCategory_Success() throws CategoryNotFoundException {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");
        Category category = new Category(1L, "Food", user);

        when(categoryRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(user, 1L);

        verify(categoryRepository).findByUserAndId(user, 1L);
        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_CategoryNotFoundException() {
        User user = new User("John Doe", "encodedPassword", "john.doe@example.com");

        when(categoryRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(user, 1L));

        verify(categoryRepository).findByUserAndId(user, 1L);
        verifyNoMoreInteractions(categoryRepository);
    }
}